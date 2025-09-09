package cn.iocoder.yudao.module.travel.service.ticket;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.travel.controller.admin.ticket.vo.*;
import cn.iocoder.yudao.module.travel.dal.dataobject.orders.OrdersDO;
import cn.iocoder.yudao.module.travel.dal.dataobject.passengers.PassengersDO;
import cn.iocoder.yudao.module.travel.dal.dataobject.flightsegments.FlightSegmentsDO;
import cn.iocoder.yudao.module.travel.dal.dataobject.orderfees.OrderFeesDO;
import cn.iocoder.yudao.module.travel.dal.mysql.orders.OrdersMapper;
import cn.iocoder.yudao.module.travel.dal.mysql.passengers.PassengersMapper;
import cn.iocoder.yudao.module.travel.dal.mysql.flightsegments.FlightSegmentsMapper;
import cn.iocoder.yudao.module.travel.dal.mysql.orderfees.OrderFeesMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 出票 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class TicketServiceImpl implements TicketService {

    @Resource
    private OrdersMapper ordersMapper;
    
    @Resource
    private PassengersMapper passengersMapper;
    
    @Resource
    private FlightSegmentsMapper flightSegmentsMapper;
    
    @Resource
    private OrderFeesMapper orderFeesMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TicketIssueRespVO issueTicket(TicketIssueReqVO reqVO) {
        // 1. 创建或更新订单
        OrdersDO order = createOrUpdateOrder(reqVO);
        
        // 2. 创建或更新乘客信息
        List<PassengersDO> passengers = createOrUpdatePassengers(reqVO, order.getId());
        
        // 3. 创建航段信息
        List<FlightSegmentsDO> flightSegments = createFlightSegments(reqVO, order.getId(), passengers);
        
        // 4. 创建费用信息
        createOrderFees(reqVO, order.getId(), passengers);
        
        // 5. 生成电子票号
        List<String> ticketNos = generateTicketNumbers(passengers, flightSegments);
        
        // 6. 更新航段的电子票号
        updateFlightSegmentsWithTicketNumbers(flightSegments, ticketNos);
        
        // 7. 更新订单状态为已出票
        updateOrderStatus(order.getId(), 30); // 30: 已出票
        
        // 8. 构建响应
        return buildTicketIssueResponse(order, ticketNos);
    }

    private OrdersDO createOrUpdateOrder(TicketIssueReqVO reqVO) {
        OrdersDO order;
        
        if (reqVO.getOrderId() != null) {
            // 更新现有订单
            order = ordersMapper.selectById(reqVO.getOrderId());
            if (order == null) {
                throw new IllegalArgumentException("订单不存在");
            }
            order.setTotalAmount(reqVO.getTotalAmount());
            order.setCurrency(reqVO.getCurrency());
            order.setRemark(reqVO.getRemark());
            order.setUpdatedAt(LocalDateTime.now());
            ordersMapper.updateById(order);
        } else if (StrUtil.isNotBlank(reqVO.getOrderNo())) {
            // 根据订单号查询订单
            order = ordersMapper.selectByOrderNo(reqVO.getOrderNo());
            if (order == null) {
                throw new IllegalArgumentException("订单号不存在");
            }
        } else {
            // 创建新订单
            order = OrdersDO.builder()
                    .orderNo(generateOrderNo())
                    .userId(1L) // 默认用户ID，实际应从登录用户获取
                    .totalAmount(reqVO.getTotalAmount())
                    .currency(reqVO.getCurrency())
                    .orderStatus(20) // 20: 已支付
                    .paymentStatus(20) // 20: 已支付
                    .bookingType("ONLINE")
                    .contactName("系统用户") // 实际应从乘客信息或用户信息获取
                    .contactPhone("13800138000") // 实际应从乘客信息或用户信息获取
                    .contactEmail("system@example.com")
                    .remark(reqVO.getRemark())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            ordersMapper.insert(order);
        }
        
        return order;
    }

    private List<PassengersDO> createOrUpdatePassengers(TicketIssueReqVO reqVO, Long orderId) {
        List<PassengersDO> passengers = new ArrayList<>();
        
        for (TicketIssueReqVO.PassengerInfo passengerInfo : reqVO.getPassengers()) {
            PassengersDO passenger;
            
            if (passengerInfo.getId() != null) {
                // 更新现有乘客
                passenger = passengersMapper.selectById(passengerInfo.getId());
                if (passenger == null) {
                    throw new IllegalArgumentException("乘客不存在");
                }
                updatePassengerInfo(passenger, passengerInfo);
                passengersMapper.updateById(passenger);
            } else {
                // 创建新乘客
                passenger = PassengersDO.builder()
                        .orderId(orderId)
                        .passengerType(passengerInfo.getPassengerType())
                        .chineseName(buildChineseName(passengerInfo.getLastNameCn(), passengerInfo.getFirstNameCn()))
                        .englishName(buildEnglishName(passengerInfo.getLastName(), passengerInfo.getFirstName()))
                        .gender(passengerInfo.getGender() == 1 ? "MALE" : "FEMALE")
                        .birthday(passengerInfo.getBirthday())
                        .idType(convertDocumentType(passengerInfo.getDocumentType()))
                        .idNumber(passengerInfo.getDocumentNo())
                        .idExpiryDate(passengerInfo.getDocumentExpiry())
                        .nationality(passengerInfo.getNationality())
                        .phone(passengerInfo.getMobile())
                        .email(passengerInfo.getEmail())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();
                passengersMapper.insert(passenger);
            }
            
            passengers.add(passenger);
        }
        
        return passengers;
    }

    private List<FlightSegmentsDO> createFlightSegments(TicketIssueReqVO reqVO, Long orderId, List<PassengersDO> passengers) {
        List<FlightSegmentsDO> flightSegments = new ArrayList<>();
        
        for (TicketIssueReqVO.FlightSegmentInfo segmentInfo : reqVO.getFlightSegments()) {
            for (PassengersDO passenger : passengers) {
                FlightSegmentsDO segment = FlightSegmentsDO.builder()
                        .orderId(orderId)
                        .passengerId(passenger.getId())
                        .segmentType(segmentInfo.getSegmentType())
                        .airlineCode(segmentInfo.getAirlineCode())
                        .flightNo(segmentInfo.getFlightNo())
                        .departureAirportCode(segmentInfo.getDepartureAirportCode())
                        .arrivalAirportCode(segmentInfo.getArrivalAirportCode())
                        .departureTime(segmentInfo.getDepartureTime())
                        .arrivalTime(segmentInfo.getArrivalTime())
                        .cabinClass(segmentInfo.getCabinClass())
                        .status(10) // 10: 正常
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();
                flightSegmentsMapper.insert(segment);
                flightSegments.add(segment);
            }
        }
        
        return flightSegments;
    }

    private void createOrderFees(TicketIssueReqVO reqVO, Long orderId, List<PassengersDO> passengers) {
        for (TicketIssueReqVO.FeeInfo feeInfo : reqVO.getFees()) {
            // 如果费用与特定乘客相关，为每个乘客创建费用记录
            if (feeInfo.getFeeType().equals("TICKET_PRICE")) {
                for (PassengersDO passenger : passengers) {
                    OrderFeesDO fee = OrderFeesDO.builder()
                            .orderId(orderId)
                            .passengerId(passenger.getId())
                            .feeType(feeInfo.getFeeType())
                            .amount(feeInfo.getAmount())
                            .currency(feeInfo.getCurrency())
                            .description(feeInfo.getDescription())
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();
                    orderFeesMapper.insert(fee);
                }
            } else {
                // 其他费用只创建一条记录
                OrderFeesDO fee = OrderFeesDO.builder()
                        .orderId(orderId)
                        .feeType(feeInfo.getFeeType())
                        .amount(feeInfo.getAmount())
                        .currency(feeInfo.getCurrency())
                        .description(feeInfo.getDescription())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();
                orderFeesMapper.insert(fee);
            }
        }
    }

    private List<String> generateTicketNumbers(List<PassengersDO> passengers, List<FlightSegmentsDO> flightSegments) {
        List<String> ticketNos = new ArrayList<>();
        
        // 为每个乘客的每个航段生成一个电子票号
        for (int i = 0; i < flightSegments.size(); i++) {
            String ticketNo = "781" + RandomUtil.randomNumbers(10); // 13位电子票号
            ticketNos.add(ticketNo);
        }
        
        return ticketNos;
    }

    private void updateFlightSegmentsWithTicketNumbers(List<FlightSegmentsDO> flightSegments, List<String> ticketNos) {
        for (int i = 0; i < flightSegments.size() && i < ticketNos.size(); i++) {
            FlightSegmentsDO segment = flightSegments.get(i);
            segment.setTicketNo(ticketNos.get(i));
            segment.setUpdatedAt(LocalDateTime.now());
            flightSegmentsMapper.updateById(segment);
        }
    }

    private void updateOrderStatus(Long orderId, Integer status) {
        OrdersDO order = ordersMapper.selectById(orderId);
        order.setOrderStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        ordersMapper.updateById(order);
    }

    private TicketIssueRespVO buildTicketIssueResponse(OrdersDO order, List<String> ticketNos) {
        TicketIssueRespVO response = new TicketIssueRespVO();
        response.setOrderId(order.getId());
        response.setOrderNo(order.getOrderNo());
        response.setTicketNos(ticketNos);
        response.setTotalAmount(order.getTotalAmount());
        response.setCurrency(order.getCurrency());
        response.setStatus(10); // 10: 出票成功
        response.setMessage("出票成功，请保存好电子票号");
        response.setIssueTime(LocalDateTime.now());
        
        return response;
    }

    // 辅助方法
    private String generateOrderNo() {
        return "ORD" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + 
               RandomUtil.randomNumbers(3);
    }

    private String buildChineseName(String lastNameCn, String firstNameCn) {
        if (StrUtil.isNotBlank(lastNameCn) && StrUtil.isNotBlank(firstNameCn)) {
            return lastNameCn + firstNameCn;
        }
        return null;
    }

    private String buildEnglishName(String lastName, String firstName) {
        return lastName + " " + firstName;
    }

    private String convertDocumentType(Integer documentType) {
        switch (documentType) {
            case 1: return "ID_CARD";
            case 2: return "PASSPORT";
            case 3: return "HK_MACAO_PERMIT";
            case 4: return "TAIWAN_PERMIT";
            default: return "OTHER";
        }
    }

    private void updatePassengerInfo(PassengersDO passenger, TicketIssueReqVO.PassengerInfo passengerInfo) {
        passenger.setPassengerType(passengerInfo.getPassengerType());
        passenger.setChineseName(buildChineseName(passengerInfo.getLastNameCn(), passengerInfo.getFirstNameCn()));
        passenger.setEnglishName(buildEnglishName(passengerInfo.getLastName(), passengerInfo.getFirstName()));
        passenger.setGender(passengerInfo.getGender() == 1 ? "MALE" : "FEMALE");
        passenger.setBirthday(passengerInfo.getBirthday());
        passenger.setIdType(convertDocumentType(passengerInfo.getDocumentType()));
        passenger.setIdNumber(passengerInfo.getDocumentNo());
        passenger.setIdExpiryDate(passengerInfo.getDocumentExpiry());
        passenger.setNationality(passengerInfo.getNationality());
        passenger.setPhone(passengerInfo.getMobile());
        passenger.setEmail(passengerInfo.getEmail());
        passenger.setUpdatedAt(LocalDateTime.now());
    }
}