package cn.iocoder.yudao.module.travel.service.refund.impl;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.travel.controller.admin.refund.vo.*;
import cn.iocoder.yudao.module.travel.dal.dataobject.flightsegments.FlightSegmentsDO;
import cn.iocoder.yudao.module.travel.dal.dataobject.orderfees.OrderFeesDO;
import cn.iocoder.yudao.module.travel.dal.dataobject.orders.OrdersDO;
import cn.iocoder.yudao.module.travel.dal.dataobject.passengers.PassengersDO;
import cn.iocoder.yudao.module.travel.dal.dataobject.refund.RefundApplicationDO;
import cn.iocoder.yudao.module.travel.dal.dataobject.refund.RefundOperationLogDO;
import cn.iocoder.yudao.module.travel.dal.dataobject.refund.RefundPolicyDO;
import cn.iocoder.yudao.module.travel.dal.mysql.flightsegments.FlightSegmentsMapper;
import cn.iocoder.yudao.module.travel.dal.mysql.orderfees.OrderFeesMapper;
import cn.iocoder.yudao.module.travel.dal.mysql.orders.OrdersMapper;
import cn.iocoder.yudao.module.travel.dal.mysql.passengers.PassengersMapper;
import cn.iocoder.yudao.module.travel.dal.mysql.refund.RefundApplicationMapper;
import cn.iocoder.yudao.module.travel.dal.mysql.refund.RefundOperationLogMapper;
import cn.iocoder.yudao.module.travel.dal.mysql.refund.RefundPolicyMapper;
import cn.iocoder.yudao.module.travel.enums.refund.RefundStatusEnum;
import cn.iocoder.yudao.module.travel.enums.refund.RefundTypeEnum;
import cn.iocoder.yudao.module.travel.service.refund.RefundApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.travel.enums.ErrorCodeConstants.*;

/**
 * 退票申请 Service 实现类
 * 
 * @author 芋道源码
 */
@Service
@Slf4j
public class RefundApplicationServiceImpl implements RefundApplicationService {

    @Autowired
    private RefundApplicationMapper refundApplicationMapper;
    
    @Autowired
    private RefundOperationLogMapper refundOperationLogMapper;
    
    @Autowired
    private RefundPolicyMapper refundPolicyMapper;
    
    @Autowired
    private OrdersMapper ordersMapper;
    
    @Autowired
    private PassengersMapper passengersMapper;
    
    @Autowired
    private FlightSegmentsMapper flightSegmentsMapper;
    
    @Autowired
    private OrderFeesMapper orderFeesMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long submitRefund(RefundApplicationCreateReqVO createReqVO) {
        log.info("开始提交退票申请：orderId={}, segmentId={}, passengerId={}", 
                createReqVO.getOrderId(), createReqVO.getSegmentId(), createReqVO.getPassengerId());
        
        // 1. 数据验证
        validateRefundApplication(createReqVO);
        
        // 2. 构建DO对象
        RefundApplicationDO refundApplication = BeanUtils.toBean(createReqVO, RefundApplicationDO.class);
        refundApplication.setStatus(RefundStatusEnum.PENDING.getStatus());
        refundApplication.setCurrency("CNY");
        
        // 3. 补充订单和乘客信息
        enrichRefundApplicationInfo(refundApplication, createReqVO);
        
        // 4. 保存到数据库
        refundApplicationMapper.insert(refundApplication);
        
        // 5. 记录操作日志
        logRefundOperation(refundApplication.getId(), "CREATE", "提交退票申请", null, refundApplication);
        
        log.info("退票申请提交成功，ID: {}, 订单号: {}", refundApplication.getId(), refundApplication.getOrderNo());
        return refundApplication.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveRefund(RefundApplicationApproveReqVO approveReqVO) {
        log.info("开始审核退票申请：id={}, approved={}", approveReqVO.getId(), approveReqVO.getApproved());
        
        // 1. 获取退票申请
        RefundApplicationDO refundApplication = getRefundApplicationById(approveReqVO.getId());
        
        // 2. 验证状态
        if (!RefundStatusEnum.PENDING.getStatus().equals(refundApplication.getStatus())) {
            throw exception(REFUND_STATUS_NOT_ALLOW_APPROVE);
        }
        
        // 3. 更新状态
        RefundApplicationDO updateObj = new RefundApplicationDO();
        updateObj.setId(approveReqVO.getId());
        updateObj.setStatus(approveReqVO.getApproved() ? 
                RefundStatusEnum.APPROVED.getStatus() : RefundStatusEnum.REJECTED.getStatus());
        updateObj.setApprover(getLoginUserNickname());
        updateObj.setApproveTime(LocalDateTime.now());
        updateObj.setApproveRemarks(approveReqVO.getRemarks());
        
        refundApplicationMapper.updateById(updateObj);
        
        // 4. 记录操作日志
        String operationType = approveReqVO.getApproved() ? "APPROVE" : "REJECT";
        String operationDesc = approveReqVO.getApproved() ? "审核通过" : "审核拒绝";
        logRefundOperation(approveReqVO.getId(), operationType, operationDesc, 
                refundApplication, updateObj);
        
        log.info("退票申请审核完成，ID: {}, 结果: {}", approveReqVO.getId(), 
                approveReqVO.getApproved() ? "通过" : "拒绝");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void executeRefund(Long id) {
        log.info("开始执行退票：id={}", id);
        
        // 1. 获取退票申请
        RefundApplicationDO refundApplication = getRefundApplicationById(id);
        
        // 2. 验证状态
        if (!RefundStatusEnum.APPROVED.getStatus().equals(refundApplication.getStatus())) {
            throw exception(REFUND_STATUS_NOT_ALLOW_EXECUTE);
        }
        
        // 3. 执行退票业务逻辑
        executeRefundBusiness(refundApplication);
        
        // 4. 更新状态为已完成
        RefundApplicationDO updateObj = new RefundApplicationDO();
        updateObj.setId(id);
        updateObj.setStatus(RefundStatusEnum.COMPLETED.getStatus());
        
        refundApplicationMapper.updateById(updateObj);
        
        // 5. 记录操作日志
        logRefundOperation(id, "EXECUTE", "执行退票", refundApplication, updateObj);
        
        log.info("退票执行完成，ID: {}, 退款金额: {}", id, refundApplication.getActualRefundAmount());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelRefund(RefundApplicationCancelReqVO cancelReqVO) {
        log.info("开始取消退票申请：id={}, reason={}", cancelReqVO.getId(), cancelReqVO.getReason());
        
        // 1. 获取退票申请
        RefundApplicationDO refundApplication = getRefundApplicationById(cancelReqVO.getId());
        
        // 2. 验证状态 - 只有待审核状态才能取消
        if (!RefundStatusEnum.PENDING.getStatus().equals(refundApplication.getStatus())) {
            throw exception(REFUND_STATUS_NOT_ALLOW_CANCEL);
        }
        
        // 3. 更新状态为已取消
        RefundApplicationDO updateObj = new RefundApplicationDO();
        updateObj.setId(cancelReqVO.getId());
        updateObj.setStatus(RefundStatusEnum.CANCELLED.getStatus());
        updateObj.setRemarks(cancelReqVO.getReason());
        
        refundApplicationMapper.updateById(updateObj);
        
        // 4. 记录操作日志
        logRefundOperation(cancelReqVO.getId(), "CANCEL", "取消申请：" + cancelReqVO.getReason(), 
                refundApplication, updateObj);
        
        log.info("退票申请已取消，ID: {}", cancelReqVO.getId());
    }

    @Override
    public RefundFeeRespVO calculateRefundFee(RefundFeeCalculateReqVO calculateReqVO) {
        log.info("开始计算退票费用：segmentId={}, passengerId={}, refundType={}", 
                calculateReqVO.getSegmentId(), calculateReqVO.getPassengerId(), calculateReqVO.getRefundType());
        
        // 1. 验证退票类型
        if (RefundTypeEnum.valueOf(calculateReqVO.getRefundType()) == null) {
            throw exception(REFUND_TYPE_INVALID);
        }
        
        // 2. 获取退票政策
        RefundPolicyDO policy = getRefundPolicy(calculateReqVO.getFlightNo(), 
                calculateReqVO.getCabinClass(), calculateReqVO.getRefundType());
        
        if (policy == null) {
            throw exception(REFUND_POLICY_NOT_FOUND);
        }
        
        // 3. 获取原票价
        BigDecimal originalPrice = getOriginalTicketPrice(calculateReqVO.getSegmentId(), 
                calculateReqVO.getPassengerId());
        
        // 4. 计算退票费
        BigDecimal refundFee = calculateRefundFeeAmount(originalPrice, policy);
        
        // 5. 计算实际退款金额
        BigDecimal actualRefundAmount = originalPrice.subtract(refundFee);
        
        // 6. 构建响应
        RefundFeeRespVO response = new RefundFeeRespVO();
        response.setOriginalTicketPrice(originalPrice);
        response.setRefundFee(refundFee);
        response.setRefundAmount(actualRefundAmount);
        response.setActualRefundAmount(actualRefundAmount);
        response.setCurrency("CNY");
        
        // 7. 构建费用明细
        List<RefundFeeRespVO.FeeDetail> feeDetails = new ArrayList<>();
        feeDetails.add(createFeeDetail("原票价", originalPrice, "票面价格"));
        feeDetails.add(createFeeDetail("退票费", refundFee, getRefundFeeDescription(calculateReqVO.getRefundType())));
        response.setFeeDetails(feeDetails);
        
        log.info("退票费用计算完成：原票价={}, 退票费={}, 实际退款={}", originalPrice, refundFee, actualRefundAmount);
        return response;
    }

    @Override
    public RefundApplicationRespVO getRefundApplication(Long id) {
        RefundApplicationDO refundApplication = getRefundApplicationById(id);
        return BeanUtils.toBean(refundApplication, RefundApplicationRespVO.class);
    }

    @Override
    public PageResult<RefundApplicationRespVO> getRefundPage(RefundApplicationPageReqVO pageReqVO) {
        PageResult<RefundApplicationDO> pageResult = refundApplicationMapper.selectPage(pageReqVO);
        return BeanUtils.toBean(pageResult, RefundApplicationRespVO.class);
    }

    @Override
    public RefundInfoRespVO getRefundInfo(Long orderId) {
        log.info("获取可退票信息：orderId={}", orderId);
        
        // 1. 获取订单信息
        OrdersDO order = ordersMapper.selectById(orderId);
        if (order == null) {
            log.warn("订单不存在，orderId={}", orderId);
            throw exception(REFUND_ORDER_NOT_EXISTS);
        }
        
        log.info("订单信息：orderNo={}, status={}", order.getOrderNo(), order.getOrderStatus());
        
        // 2. 验证订单状态
        if (!isOrderRefundable(order.getOrderStatus())) {
            log.warn("订单状态不允许退票，orderId={}, status={}", orderId, order.getOrderStatus());
            throw exception(REFUND_ORDER_STATUS_NOT_ALLOW);
        }
        
        // 3. 获取乘客列表
        List<PassengersDO> passengers = passengersMapper.selectList(PassengersDO::getOrderId, orderId);
        
        // 4. 获取航段列表
        List<FlightSegmentsDO> segments = flightSegmentsMapper.selectList(FlightSegmentsDO::getOrderId, orderId);
        
        // 5. 构建响应
        RefundInfoRespVO response = new RefundInfoRespVO();
        response.setOrderId(order.getId());
        response.setOrderNo(order.getOrderNo());
        
        // 构建可退票航段
        List<RefundInfoRespVO.RefundableSegment> refundableSegments = new ArrayList<>();
        for (FlightSegmentsDO segment : segments) {
            if (isSegmentRefundable(segment)) {
                RefundInfoRespVO.RefundableSegment refundableSegment = new RefundInfoRespVO.RefundableSegment();
                refundableSegment.setId(segment.getId());
                refundableSegment.setFlightNo(segment.getFlightNo());
                refundableSegment.setAirlineCode(segment.getAirlineCode());
                refundableSegment.setAirlineName(getAirlineName(segment.getAirlineCode()));
                refundableSegment.setDepartureAirportCode(segment.getDepartureAirportCode());
                refundableSegment.setDepartureAirportName(getAirportName(segment.getDepartureAirportCode()));
                refundableSegment.setArrivalAirportCode(segment.getArrivalAirportCode());
                refundableSegment.setArrivalAirportName(getAirportName(segment.getArrivalAirportCode()));
                refundableSegment.setDepartureTime(segment.getDepartureTime().toString());
                refundableSegment.setArrivalTime(segment.getArrivalTime().toString());
                refundableSegment.setCabinClass(segment.getCabinClass());
                refundableSegment.setTicketPrice(getSegmentTicketPrice(segment.getId()));
                refundableSegment.setCanRefund(true);
                
                refundableSegments.add(refundableSegment);
            }
        }
        response.setSegments(refundableSegments);
        
        // 构建乘客信息
        List<RefundInfoRespVO.PassengerInfo> passengerInfos = new ArrayList<>();
        for (PassengersDO passenger : passengers) {
            RefundInfoRespVO.PassengerInfo passengerInfo = new RefundInfoRespVO.PassengerInfo();
            passengerInfo.setId(passenger.getId());
            passengerInfo.setName(passenger.getChineseName());
            passengerInfo.setDocumentType(getDocumentTypeCode(passenger.getIdType()));
            passengerInfo.setDocumentNo(passenger.getIdNumber());
            passengerInfo.setPhoneNumber(passenger.getPhone());
            
            passengerInfos.add(passengerInfo);
        }
        response.setPassengers(passengerInfos);
        
        return response;
    }

    // ==================== 私有方法 ====================

    private void validateRefundApplication(RefundApplicationCreateReqVO createReqVO) {
        // 1. 验证订单状态
        validateOrderStatus(createReqVO.getOrderId());
        
        // 2. 验证航班时间
        validateFlightTime(createReqVO.getDepartureTime());
        
        // 3. 验证退票类型
        if (RefundTypeEnum.valueOf(createReqVO.getRefundType()) == null) {
            throw exception(REFUND_TYPE_INVALID);
        }
        
        // 4. 验证是否已有退票申请
        List<RefundApplicationDO> existingApplications = refundApplicationMapper.selectList(
                RefundApplicationDO::getOrderId, createReqVO.getOrderId());
        for (RefundApplicationDO existingApplication : existingApplications) {
            if (existingApplication.getPassengerId().equals(createReqVO.getPassengerId()) &&
                (RefundStatusEnum.PENDING.getStatus().equals(existingApplication.getStatus()) ||
                 RefundStatusEnum.APPROVED.getStatus().equals(existingApplication.getStatus()))) {
                throw exception(REFUND_APPLICATION_ALREADY_EXISTS);
            }
        }
    }

    private void validateOrderStatus(Long orderId) {
        OrdersDO order = ordersMapper.selectById(orderId);
        if (order == null) {
            throw exception(REFUND_ORDER_NOT_EXISTS);
        }
        
        // 只有已出票状态才能退票
        if (!order.getOrderStatus().equals(30)) { // 30: 已出票
            throw exception(REFUND_ORDER_STATUS_NOT_ALLOW);
        }
    }

    private void validateFlightTime(LocalDateTime departureTime) {
        // 验证航班是否已起飞
        if (departureTime.isBefore(LocalDateTime.now())) {
            throw exception(REFUND_FLIGHT_ALREADY_DEPARTED);
        }
    }

    private void enrichRefundApplicationInfo(RefundApplicationDO refundApplication, RefundApplicationCreateReqVO createReqVO) {
        // 获取订单信息
        OrdersDO order = ordersMapper.selectById(createReqVO.getOrderId());
        if (order != null) {
            refundApplication.setOrderNo(order.getOrderNo());
        }
        
        // 获取乘客信息
        PassengersDO passenger = passengersMapper.selectById(createReqVO.getPassengerId());
        if (passenger != null) {
            refundApplication.setPassengerName(passenger.getChineseName());
        }
        
        // 补充机场名称
        refundApplication.setDepartureAirportName(getAirportName(createReqVO.getDepartureAirportCode()));
        refundApplication.setArrivalAirportName(getAirportName(createReqVO.getArrivalAirportCode()));
        refundApplication.setAirlineName(getAirlineName(createReqVO.getAirlineCode()));
        
        // 计算退票金额
        refundApplication.setRefundAmount(createReqVO.getOriginalTicketPrice().subtract(createReqVO.getRefundFee()));
    }

    private BigDecimal calculateRefundFeeAmount(BigDecimal originalPrice, RefundPolicyDO policy) {
        // 1. 按费率计算
        BigDecimal feeAmount = originalPrice.multiply(policy.getFeeRate())
                .setScale(2, RoundingMode.HALF_UP);
        
        // 2. 检查最低费用
        if (policy.getMinFee() != null && feeAmount.compareTo(policy.getMinFee()) < 0) {
            feeAmount = policy.getMinFee();
        }
        
        // 3. 检查最高费用
        if (policy.getMaxFee() != null && feeAmount.compareTo(policy.getMaxFee()) > 0) {
            feeAmount = policy.getMaxFee();
        }
        
        return feeAmount;
    }

    private RefundPolicyDO getRefundPolicy(String flightNo, String cabinClass, Integer refundType) {
        // 从航班号提取航司代码
        String airlineCode = extractAirlineCode(flightNo);
        
        // 查询退票政策
        return refundPolicyMapper.selectByAirlineAndCabinAndType(airlineCode, cabinClass, refundType);
    }

    private String extractAirlineCode(String flightNo) {
        if (StrUtil.isBlank(flightNo) || flightNo.length() < 2) {
            return "";
        }
        // 提取前2位作为航司代码
        return flightNo.substring(0, 2).toUpperCase();
    }

    private BigDecimal getOriginalTicketPrice(Long segmentId, Long passengerId) {
        // 从订单费用表中获取票价
        FlightSegmentsDO segment = flightSegmentsMapper.selectById(segmentId);
        if (segment == null) {
            throw exception(REFUND_SEGMENT_NOT_EXISTS);
        }
        
        List<OrderFeesDO> fees = orderFeesMapper.selectList(
                OrderFeesDO::getOrderId, segment.getOrderId());
        fees = fees.stream()
                .filter(fee -> fee.getPassengerId() != null && fee.getPassengerId().equals(passengerId))
                .filter(fee -> "TICKET_PRICE".equals(fee.getFeeType()))
                .collect(Collectors.toList());
        
        if (fees.isEmpty()) {
            // 如果没有找到特定乘客的票价，返回默认票价
            return new BigDecimal("2000.00");
        }
        
        return fees.get(0).getAmount();
    }

    private void executeRefundBusiness(RefundApplicationDO refundApplication) {
        // 1. 调用支付系统退款
        executePaymentRefund(refundApplication);
        
        // 2. 更新航段状态
        updateSegmentStatus(refundApplication.getSegmentId());
        
        // 3. 更新订单状态（如果所有航段都退票了）
        updateOrderStatusIfNeeded(refundApplication.getOrderId());
        
        // 4. 发送退票通知
        sendRefundNotification(refundApplication);
    }

    private void executePaymentRefund(RefundApplicationDO refundApplication) {
        // 模拟调用支付系统退款
        log.info("执行支付退款：订单号={}, 退款金额={}", 
                refundApplication.getOrderNo(), refundApplication.getActualRefundAmount());
        
        // 这里应该集成真实的支付系统
        // 例如：paymentService.refund(refundApplication.getOrderNo(), refundApplication.getActualRefundAmount());
    }

    private void updateSegmentStatus(Long segmentId) {
        FlightSegmentsDO segment = new FlightSegmentsDO();
        segment.setId(segmentId);
        segment.setStatus(30); // 30: 已退票
        flightSegmentsMapper.updateById(segment);
    }

    private void updateOrderStatusIfNeeded(Long orderId) {
        // 检查是否所有航段都已退票
        List<FlightSegmentsDO> segments = flightSegmentsMapper.selectList(FlightSegmentsDO::getOrderId, orderId);
        boolean allRefunded = segments.stream().allMatch(segment -> segment.getStatus().equals(30));
        
        if (allRefunded) {
            OrdersDO order = new OrdersDO();
            order.setId(orderId);
            order.setOrderStatus(90); // 90: 已取消
            order.setPaymentStatus(40); // 40: 全额退款
            ordersMapper.updateById(order);
        }
    }

    private void sendRefundNotification(RefundApplicationDO refundApplication) {
        // 发送退票通知
        log.info("发送退票通知：订单号={}, 乘客={}", 
                refundApplication.getOrderNo(), refundApplication.getPassengerName());
        
        // 这里应该集成消息通知服务
        // 例如：notificationService.sendRefundNotification(refundApplication);
    }

    private RefundApplicationDO getRefundApplicationById(Long id) {
        RefundApplicationDO refundApplication = refundApplicationMapper.selectById(id);
        if (refundApplication == null) {
            throw exception(REFUND_APPLICATION_NOT_EXISTS);
        }
        return refundApplication;
    }

    private void logRefundOperation(Long refundId, String operationType, String operationDesc, 
                                   RefundApplicationDO before, RefundApplicationDO after) {
        RefundOperationLogDO log = RefundOperationLogDO.builder()
                .refundId(refundId)
                .operationType(operationType)
                .operationDesc(operationDesc)
                .contentBefore(before != null ? before.toString() : null)
                .contentAfter(after != null ? after.toString() : null)
                .operatorId(SecurityFrameworkUtils.getLoginUserId())
                .operatorName(getLoginUserNickname())
                .operatorIp(getClientIP())
                .build();
        
        refundOperationLogMapper.insert(log);
    }

    private RefundFeeRespVO.FeeDetail createFeeDetail(String type, BigDecimal amount, String description) {
        RefundFeeRespVO.FeeDetail detail = new RefundFeeRespVO.FeeDetail();
        detail.setType(type);
        detail.setAmount(amount);
        detail.setDescription(description);
        return detail;
    }

    private String getRefundFeeDescription(Integer refundType) {
        return RefundTypeEnum.VOLUNTARY.getType().equals(refundType) ? 
                "自愿退票费" : "非自愿退票费";
    }

    private String getLoginUserNickname() {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        return userId != null ? "用户" + userId : "系统";
    }

    private String getClientIP() {
        return "127.0.0.1"; // 这里应该获取真实的客户端IP
    }

    private boolean isOrderRefundable(Integer orderStatus) {
        // 30: 已出票状态才可以退票
        return orderStatus.equals(30);
    }

    private boolean isSegmentRefundable(FlightSegmentsDO segment) {
        // 10: 正常状态且未起飞才可以退票
        return segment.getStatus().equals(10) && segment.getDepartureTime().isAfter(LocalDateTime.now());
    }

    private String getAirlineName(String airlineCode) {
        // 这里应该从航司字典表获取，暂时硬编码
        switch (airlineCode) {
            case "CA": return "中国国际航空";
            case "MU": return "中国东方航空";
            case "CZ": return "中国南方航空";
            default: return airlineCode;
        }
    }

    private String getAirportName(String airportCode) {
        // 这里应该从机场字典表获取，暂时硬编码
        switch (airportCode) {
            case "PEK": return "北京首都国际机场";
            case "PVG": return "上海浦东国际机场";
            case "CAN": return "广州白云国际机场";
            case "LAX": return "洛杉矶国际机场";
            case "ICN": return "首尔仁川国际机场";
            case "SIN": return "新加坡樟宜机场";
            default: return airportCode;
        }
    }

    private BigDecimal getSegmentTicketPrice(Long segmentId) {
        FlightSegmentsDO segment = flightSegmentsMapper.selectById(segmentId);
        if (segment == null) {
            return BigDecimal.ZERO;
        }
        
        List<OrderFeesDO> fees = orderFeesMapper.selectList(
                OrderFeesDO::getOrderId, segment.getOrderId());
        fees = fees.stream()
                .filter(fee -> "TICKET_PRICE".equals(fee.getFeeType()))
                .collect(Collectors.toList());
        
        return fees.isEmpty() ? new BigDecimal("2000.00") : fees.get(0).getAmount();
    }

    private Integer getDocumentTypeCode(String idType) {
        switch (idType) {
            case "ID_CARD": return 1;
            case "PASSPORT": return 2;
            case "HK_MACAO_PERMIT": return 3;
            case "TAIWAN_PERMIT": return 4;
            default: return 5;
        }
    }

}