package cn.iocoder.yudao.module.travel.service.rebooking;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.travel.controller.admin.rebooking.vo.*;
import cn.iocoder.yudao.module.travel.dal.dataobject.flightsegments.FlightSegmentsDO;
import cn.iocoder.yudao.module.travel.dal.dataobject.orders.OrdersDO;
import cn.iocoder.yudao.module.travel.dal.dataobject.passengers.PassengersDO;
import cn.iocoder.yudao.module.travel.dal.dataobject.rebooking.RebookingApplicationDO;
import cn.iocoder.yudao.module.travel.dal.dataobject.rebooking.RebookingOperationLogDO;
import cn.iocoder.yudao.module.travel.dal.mysql.rebooking.RebookingApplicationMapper;
import cn.iocoder.yudao.module.travel.dal.mysql.rebooking.RebookingOperationLogMapper;
import cn.iocoder.yudao.module.travel.enums.rebooking.RebookingStatusEnum;
//import cn.iocoder.yudao.module.travel.mq.producer.TravelMessageProducer;
import cn.iocoder.yudao.module.travel.service.flightsegments.FlightSegmentsService;
import cn.iocoder.yudao.module.travel.service.orders.OrdersService;
import cn.iocoder.yudao.module.travel.service.passengers.PassengersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.travel.enums.ErrorCodeConstants.*;

/**
 * 改签服务实现类
 *
 * @author 芋道源码
 */
@Service
@Slf4j
public class RebookingServiceImpl implements RebookingService {

    @Autowired
    private RebookingApplicationMapper rebookingApplicationMapper;

    @Autowired
    private RebookingOperationLogMapper rebookingOperationLogMapper;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private PassengersService passengersService;

    @Autowired
    private FlightSegmentsService flightSegmentsService;

    @Override
    public RebookingInfoRespVO getRebookingInfo(Long orderId) {
        // 1. 获取订单信息
        OrdersDO order = ordersService.getOrders(orderId);
        if (order == null) {
            throw exception(ORDER_NOT_EXISTS);
        }

        // 2. 获取乘客列表
        List<PassengersDO> passengers = passengersService.getPassengersByOrderId(orderId);

        // 3. 获取航段列表
//        List<FlightSegmentsDO> segments = flightSegmentsService.getFlightSegmentsByOrderId(orderId);

        // 4. 构建响应对象
        RebookingInfoRespVO respVO = new RebookingInfoRespVO();
        
        // 订单信息
        RebookingInfoRespVO.OrderInfoVO orderInfo = new RebookingInfoRespVO.OrderInfoVO();
        orderInfo.setId(order.getId());
        orderInfo.setOrderNo(order.getOrderNo());
        orderInfo.setOrderStatus(order.getOrderStatus());
        orderInfo.setContactName(order.getContactName());
        orderInfo.setContactPhone(order.getContactPhone());
        respVO.setOrderInfo(orderInfo);

        // 乘客列表
        List<RebookingInfoRespVO.PassengerVO> passengerVOs = new ArrayList<>();
        for (PassengersDO passenger : passengers) {
            RebookingInfoRespVO.PassengerVO passengerVO = new RebookingInfoRespVO.PassengerVO();
            passengerVO.setId(passenger.getId());
            passengerVO.setChineseName(passenger.getChineseName());
            passengerVO.setEnglishName(passenger.getEnglishName());
            passengerVO.setIdNumber(passenger.getIdNumber());
            passengerVOs.add(passengerVO);
        }
        respVO.setPassengers(passengerVOs);

        // 航段列表
        List<RebookingInfoRespVO.FlightSegmentVO> segmentVOs = new ArrayList<>();
//        for (FlightSegmentsDO segment : segments) {
//            RebookingInfoRespVO.FlightSegmentVO segmentVO = new RebookingInfoRespVO.FlightSegmentVO();
//            segmentVO.setId(segment.getId());
//            segmentVO.setPassengerId(segment.getPassengerId());
//            segmentVO.setFlightNo(segment.getFlightNo());
//            segmentVO.setAirlineCode(segment.getAirlineCode());
//            segmentVO.setDepartureAirportCode(segment.getDepartureAirportCode());
//            segmentVO.setArrivalAirportCode(segment.getArrivalAirportCode());
//            segmentVO.setDepartureTime(segment.getDepartureTime().toString());
//            segmentVO.setArrivalTime(segment.getArrivalTime().toString());
//            segmentVO.setCabinClass(segment.getCabinClass());
//            segmentVO.setStatus(segment.getStatus());
//            segmentVOs.add(segmentVO);
//        }
        respVO.setSegments(segmentVOs);

        return respVO;
    }

    @Override
    public List<FlightSearchRespVO> searchFlights(FlightSearchReqVO reqVO) {
        // 模拟航班搜索
        List<FlightSearchRespVO> results = new ArrayList<>();
        
        // 这里应该调用第三方航班搜索API，现在先返回模拟数据
        FlightSearchRespVO flight1 = new FlightSearchRespVO();
        flight1.setFlightNo("MU585");
        flight1.setAirlineCode("MU");
        flight1.setAirlineName("中国东方航空");
        flight1.setDepartureAirportCode(reqVO.getDepartureAirportCode());
        flight1.setDepartureAirportName("上海浦东国际机场");
        flight1.setArrivalAirportCode(reqVO.getArrivalAirportCode());
        flight1.setArrivalAirportName("洛杉矶国际机场");
        flight1.setDepartureTime(reqVO.getDepartureDate().atTime(10, 30));
        flight1.setArrivalTime(reqVO.getDepartureDate().atTime(14, 45));
        flight1.setCabinClass("BUSINESS");
        flight1.setPrice(new BigDecimal("6200.00"));
        flight1.setAvailableSeats(8);
        results.add(flight1);

        FlightSearchRespVO flight2 = new FlightSearchRespVO();
        flight2.setFlightNo("CA987");
        flight2.setAirlineCode("CA");
        flight2.setAirlineName("中国国际航空");
        flight2.setDepartureAirportCode(reqVO.getDepartureAirportCode());
        flight2.setDepartureAirportName("上海浦东国际机场");
        flight2.setArrivalAirportCode(reqVO.getArrivalAirportCode());
        flight2.setArrivalAirportName("洛杉矶国际机场");
        flight2.setDepartureTime(reqVO.getDepartureDate().atTime(16, 20));
        flight2.setArrivalTime(reqVO.getDepartureDate().atTime(20, 35));
        flight2.setCabinClass("ECONOMY");
        flight2.setPrice(new BigDecimal("3800.00"));
        flight2.setAvailableSeats(15);
        results.add(flight2);

        return results;
    }

    @Override
    public RebookingFeeRespVO calculateFee(RebookingFeeReqVO reqVO) {
        // 1. 获取原航段信息
        FlightSegmentsDO originalSegment = flightSegmentsService.getFlightSegments(reqVO.getOriginalSegmentId());
        if (originalSegment == null) {
            throw exception(FLIGHT_SEGMENT_NOT_EXISTS);
        }

        // 2. 获取新航班价格
        BigDecimal newPrice = reqVO.getNewFlightInfo().getPrice();
        if (newPrice == null) {
            newPrice = new BigDecimal("3000.00"); // 默认价格
        }

        // 3. 计算改签手续费 (这里可以根据业务规则计算)
        BigDecimal changeFee = calculateChangeFee(originalSegment, reqVO.getNewFlightInfo());

        // 4. 计算票价差额 (假设原价格为2500)
        BigDecimal originalPrice = new BigDecimal("2500.00");
        BigDecimal fareDifference = newPrice.subtract(originalPrice);

        // 5. 计算服务费
        BigDecimal serviceFee = new BigDecimal("50.00");

        // 6. 计算总费用
        BigDecimal totalFee = changeFee.add(fareDifference.max(BigDecimal.ZERO)).add(serviceFee);

        return new RebookingFeeRespVO(changeFee, fareDifference, serviceFee, totalFee);
    }

    /**
     * 计算改签手续费
     */
    private BigDecimal calculateChangeFee(FlightSegmentsDO originalSegment, RebookingFeeReqVO.FlightInfoVO newFlightInfo) {
        // 简单的改签费用计算逻辑
        // 国内航班: 200元，国际航班: 500元
        boolean isInternational = isInternationalFlight(originalSegment.getDepartureAirportCode(), 
                                                       originalSegment.getArrivalAirportCode());
        return isInternational ? new BigDecimal("500.00") : new BigDecimal("200.00");
    }

    /**
     * 判断是否为国际航班
     */
    private boolean isInternationalFlight(String departureCode, String arrivalCode) {
        // 简单判断逻辑，实际应该根据机场代码库判断
        List<String> domesticAirports = List.of("PEK", "PVG", "CAN", "SZX", "CTU", "XIY", "KMG", "URC");
        return !(domesticAirports.contains(departureCode) && domesticAirports.contains(arrivalCode));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long submitRebooking(RebookingApplicationCreateReqVO createReqVO) {
        // 1. 创建改签申请
        RebookingApplicationDO application = BeanUtils.toBean(createReqVO, RebookingApplicationDO.class);
        application.setStatus(RebookingStatusEnum.PENDING.getStatus());
        rebookingApplicationMapper.insert(application);

        // 2. 记录操作日志
        logOperation(application.getId(), "提交申请", "改签申请已提交，等待审核");
        
        // 3. 发送异步消息
        sendRebookingAppliedMessages(application, createReqVO);

        return application.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveRebooking(RebookingApproveReqVO reqVO) {
        // 1. 获取改签申请
        RebookingApplicationDO application = rebookingApplicationMapper.selectById(reqVO.getId());
        if (application == null) {
            throw exception(REBOOKING_APPLICATION_NOT_EXISTS);
        }

        // 2. 验证状态
        if (!RebookingStatusEnum.PENDING.getStatus().equals(application.getStatus())) {
            throw exception(REBOOKING_APPLICATION_STATUS_ERROR);
        }

        // 3. 更新申请状态
        RebookingApplicationDO updateObj = new RebookingApplicationDO();
        updateObj.setId(reqVO.getId());
        updateObj.setStatus(reqVO.getStatus());
        updateObj.setApprover("admin"); // 这里应该获取当前登录用户
        updateObj.setApproveTime(LocalDateTime.now());
        updateObj.setRemarks(reqVO.getRemarks());
        rebookingApplicationMapper.updateById(updateObj);

        // 4. 记录操作日志
        String statusName = reqVO.getStatus().equals(RebookingStatusEnum.APPROVED.getStatus()) ? "审核通过" : "审核拒绝";
        logOperation(reqVO.getId(), "审核申请", statusName + "：" + reqVO.getRemarks());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void executeRebooking(Long id) {
        // 1. 获取改签申请
        RebookingApplicationDO application = rebookingApplicationMapper.selectById(id);
        if (application == null) {
            throw exception(REBOOKING_APPLICATION_NOT_EXISTS);
        }

        // 2. 验证状态
        if (!RebookingStatusEnum.APPROVED.getStatus().equals(application.getStatus())) {
            throw exception(REBOOKING_APPLICATION_STATUS_ERROR);
        }

        // 3. 更新原航段状态
        updateOriginalSegmentStatus(application.getOriginalSegmentId());

        // 4. 创建新航段
        createNewFlightSegment(application);

        // 5. 更新申请状态
        RebookingApplicationDO updateObj = new RebookingApplicationDO();
        updateObj.setId(id);
        updateObj.setStatus(RebookingStatusEnum.COMPLETED.getStatus());
        rebookingApplicationMapper.updateById(updateObj);

        // 6. 记录操作日志
        logOperation(id, "执行改签", "改签执行成功");
        
        // 7. 发送异步消息
        sendRebookingCompletedMessages(application);
    }

    /**
     * 更新原航段状态
     */
    private void updateOriginalSegmentStatus(Long segmentId) {
        // 更新原航段状态为已改签
//        flightSegmentsService.updateSegmentStatus(segmentId, 20); // 20: 已改签
    }

    /**
     * 创建新航段
     */
    private void createNewFlightSegment(RebookingApplicationDO application) {
        FlightSegmentsDO newSegment = new FlightSegmentsDO();
        newSegment.setOrderId(application.getOrderId());
        newSegment.setPassengerId(application.getPassengerId());
        newSegment.setSegmentType(1); // 假设是去程
        newSegment.setAirlineCode(application.getNewAirlineCode());
        newSegment.setFlightNo(application.getNewFlightNo());
        newSegment.setDepartureAirportCode(application.getNewDepartureAirportCode());
        newSegment.setArrivalAirportCode(application.getNewArrivalAirportCode());
        newSegment.setDepartureTime(application.getNewDepartureTime());
        newSegment.setArrivalTime(application.getNewArrivalTime());
        newSegment.setCabinClass(application.getNewCabinClass());
        newSegment.setStatus(10); // 10: 正常

//        flightSegmentsService.createFlightSegments(newSegment);
    }

    @Override
    public PageResult<RebookingApplicationRespVO> getRebookingPage(RebookingApplicationPageReqVO pageReqVO) {
        PageResult<RebookingApplicationDO> pageResult = rebookingApplicationMapper.selectPage(pageReqVO);
        return BeanUtils.toBean(pageResult, RebookingApplicationRespVO.class);
    }

    @Override
    public List<AirportInfoVO> getAirports() {
        // 返回常用机场信息，实际应用中应该从数据库或第三方API获取
        List<AirportInfoVO> airports = new ArrayList<>();
        
        // 添加常用国内机场
        airports.add(new AirportInfoVO("PEK", "北京首都国际机场", "Beijing Capital International Airport", "BJS", "北京", "CN", "中国"));
        airports.add(new AirportInfoVO("PVG", "上海浦东国际机场", "Shanghai Pudong International Airport", "SHA", "上海", "CN", "中国"));
        airports.add(new AirportInfoVO("SHA", "上海虹桥国际机场", "Shanghai Hongqiao International Airport", "SHA", "上海", "CN", "中国"));
        airports.add(new AirportInfoVO("CAN", "广州白云国际机场", "Guangzhou Baiyun International Airport", "CAN", "广州", "CN", "中国"));
        airports.add(new AirportInfoVO("SZX", "深圳宝安国际机场", "Shenzhen Bao'an International Airport", "SZX", "深圳", "CN", "中国"));
        airports.add(new AirportInfoVO("CTU", "成都双流国际机场", "Chengdu Shuangliu International Airport", "CTU", "成都", "CN", "中国"));
        airports.add(new AirportInfoVO("XIY", "西安咸阳国际机场", "Xi'an Xianyang International Airport", "SIA", "西安", "CN", "中国"));
        airports.add(new AirportInfoVO("KMG", "昆明长水国际机场", "Kunming Changshui International Airport", "KMG", "昆明", "CN", "中国"));
        airports.add(new AirportInfoVO("URC", "乌鲁木齐地窝堡国际机场", "Urumqi Diwopu International Airport", "URC", "乌鲁木齐", "CN", "中国"));
        
        // 添加常用国际机场
        airports.add(new AirportInfoVO("LAX", "洛杉矶国际机场", "Los Angeles International Airport", "LAX", "洛杉矶", "US", "美国"));
        airports.add(new AirportInfoVO("JFK", "约翰·肯尼迪国际机场", "John F. Kennedy International Airport", "NYC", "纽约", "US", "美国"));
        airports.add(new AirportInfoVO("NRT", "东京成田国际机场", "Narita International Airport", "NRT", "东京", "JP", "日本"));
        airports.add(new AirportInfoVO("ICN", "首尔仁川国际机场", "Incheon International Airport", "SEL", "首尔", "KR", "韩国"));
        airports.add(new AirportInfoVO("SIN", "新加坡樟宜机场", "Singapore Changi Airport", "SIN", "新加坡", "SG", "新加坡"));
        airports.add(new AirportInfoVO("LHR", "伦敦希思罗机场", "London Heathrow Airport", "LON", "伦敦", "GB", "英国"));
        airports.add(new AirportInfoVO("CDG", "巴黎戴高乐机场", "Charles de Gaulle Airport", "PAR", "巴黎", "FR", "法国"));
        airports.add(new AirportInfoVO("FRA", "法兰克福机场", "Frankfurt Airport", "FRA", "法兰克福", "DE", "德国"));
        
        return airports;
    }

    @Override
    public List<AirlineInfoVO> getAirlines() {
        // 返回常用航空公司信息，实际应用中应该从数据库或第三方API获取
        List<AirlineInfoVO> airlines = new ArrayList<>();
        
        // 添加常用国内航空公司
        airlines.add(new AirlineInfoVO("CA", "中国国际航空", "Air China", "CN", "中国"));
        airlines.add(new AirlineInfoVO("MU", "中国东方航空", "China Eastern Airlines", "CN", "中国"));
        airlines.add(new AirlineInfoVO("CZ", "中国南方航空", "China Southern Airlines", "CN", "中国"));
        airlines.add(new AirlineInfoVO("HU", "海南航空", "Hainan Airlines", "CN", "中国"));
        airlines.add(new AirlineInfoVO("3U", "四川航空", "Sichuan Airlines", "CN", "中国"));
        airlines.add(new AirlineInfoVO("FM", "上海航空", "Shanghai Airlines", "CN", "中国"));
        airlines.add(new AirlineInfoVO("9C", "春秋航空", "Spring Airlines", "CN", "中国"));
        airlines.add(new AirlineInfoVO("8L", "祥鹏航空", "Lucky Air", "CN", "中国"));
        
        // 添加常用国际航空公司
        airlines.add(new AirlineInfoVO("AA", "美国航空", "American Airlines", "US", "美国"));
        airlines.add(new AirlineInfoVO("DL", "达美航空", "Delta Air Lines", "US", "美国"));
        airlines.add(new AirlineInfoVO("UA", "美联航空", "United Airlines", "US", "美国"));
        airlines.add(new AirlineInfoVO("JL", "日本航空", "Japan Airlines", "JP", "日本"));
        airlines.add(new AirlineInfoVO("NH", "全日空", "All Nippon Airways", "JP", "日本"));
        airlines.add(new AirlineInfoVO("KE", "大韩航空", "Korean Air", "KR", "韩国"));
        airlines.add(new AirlineInfoVO("OZ", "韩亚航空", "Asiana Airlines", "KR", "韩国"));
        airlines.add(new AirlineInfoVO("SQ", "新加坡航空", "Singapore Airlines", "SG", "新加坡"));
        airlines.add(new AirlineInfoVO("BA", "英国航空", "British Airways", "GB", "英国"));
        airlines.add(new AirlineInfoVO("LH", "汉莎航空", "Lufthansa", "DE", "德国"));
        airlines.add(new AirlineInfoVO("AF", "法国航空", "Air France", "FR", "法国"));
        
        return airlines;
    }

    /**
     * 记录操作日志
     */
    private void logOperation(Long rebookingId, String operationType, String operationDesc) {
        RebookingOperationLogDO log = RebookingOperationLogDO.builder()
                .rebookingId(rebookingId)
                .operationType(operationType)
                .operationDesc(operationDesc)
                .operator("admin") // 这里应该获取当前登录用户
                .operationTime(LocalDateTime.now())
                .build();
        
        rebookingOperationLogMapper.insert(log);
    }

    /**
     * 发送改签申请相关消息
     */
    private void sendRebookingAppliedMessages(RebookingApplicationDO application, RebookingApplicationCreateReqVO createReqVO) {
        try {
            // 获取订单信息
            OrdersDO order = ordersService.getOrders(application.getOrderId());
            if (order == null) {
                log.warn("订单不存在，无法发送改签消息：orderId={}", application.getOrderId());
                return;
            }

            // 计算改签费用显示（使用默认值）
            BigDecimal rebookingFee = application.getChangeFee() != null ? application.getChangeFee() : new BigDecimal("200.00");

            // 发送改签申请消息（包含订单状态更新、审批流程、通知发送等）


        } catch (Exception e) {
            // 消息发送失败不影响主业务流程
            log.error("发送改签申请消息失败：rebookingId={}, orderId={}",
                     application.getId(), application.getOrderId(), e);
        }
    }

    /**
     * 发送改签完成相关消息
     */
    private void sendRebookingCompletedMessages(RebookingApplicationDO application) {
        try {
            // 获取订单信息
            OrdersDO order = ordersService.getOrders(application.getOrderId());
            if (order == null) {
                log.warn("订单不存在，无法发送改签完成消息：orderId={}", application.getOrderId());
                return;
            }

            // 计算改签费用
            BigDecimal rebookingFee = application.getChangeFee() != null ? application.getChangeFee() : new BigDecimal("200.00");

            // 构建新航班信息
            String newFlightInfo = String.format("%s %s %s-%s %s",
                application.getNewAirlineCode(),
                application.getNewFlightNo(),
                application.getNewDepartureAirportCode(),
                application.getNewArrivalAirportCode(),
                application.getNewDepartureTime() != null ? application.getNewDepartureTime().toString() : ""
            );

            // 发送改签完成消息（包含订单状态更新、财务处理、通知发送等）


        } catch (Exception e) {
            // 消息发送失败不影响主业务流程
            log.error("发送改签完成消息失败：rebookingId={}, orderId={}",
                     application.getId(), application.getOrderId(), e);
        }
    }

}