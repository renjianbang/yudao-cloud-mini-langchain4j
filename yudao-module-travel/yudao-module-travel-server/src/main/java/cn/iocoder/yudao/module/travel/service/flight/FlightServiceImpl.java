package cn.iocoder.yudao.module.travel.service.flight;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.iocoder.yudao.module.travel.controller.admin.ticket.vo.FlightValidationRespVO;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 航班 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class FlightServiceImpl implements FlightService {

    // 模拟航班数据，实际项目中应该从外部API或数据库获取
    private static final Map<String, FlightTemplate> FLIGHT_TEMPLATES = new HashMap<>();

    static {
        // 初始化一些航班模板数据
        FLIGHT_TEMPLATES.put("MU583", new FlightTemplate("MU", "MU583", "PVG", "LAX", 
                LocalTime.of(8, 0), LocalTime.of(12, 0), "B777-300ER"));
        FLIGHT_TEMPLATES.put("MU584", new FlightTemplate("MU", "MU584", "LAX", "PVG", 
                LocalTime.of(14, 30), LocalTime.of(19, 30), "B777-300ER"));
        FLIGHT_TEMPLATES.put("CA981", new FlightTemplate("CA", "CA981", "PEK", "JFK", 
                LocalTime.of(9, 15), LocalTime.of(13, 45), "B747-8"));
        FLIGHT_TEMPLATES.put("CA982", new FlightTemplate("CA", "CA982", "JFK", "PEK", 
                LocalTime.of(15, 30), LocalTime.of(18, 15), "B747-8"));
        FLIGHT_TEMPLATES.put("CZ327", new FlightTemplate("CZ", "CZ327", "CAN", "SIN", 
                LocalTime.of(10, 30), LocalTime.of(14, 45), "A350-900"));
        FLIGHT_TEMPLATES.put("CZ328", new FlightTemplate("CZ", "CZ328", "SIN", "CAN", 
                LocalTime.of(16, 20), LocalTime.of(20, 35), "A350-900"));
        FLIGHT_TEMPLATES.put("MU211", new FlightTemplate("MU", "MU211", "PVG", "NRT", 
                LocalTime.of(11, 0), LocalTime.of(15, 30), "A330-300"));
        FLIGHT_TEMPLATES.put("MU212", new FlightTemplate("MU", "MU212", "NRT", "PVG", 
                LocalTime.of(17, 15), LocalTime.of(19, 45), "A330-300"));
        FLIGHT_TEMPLATES.put("CA123", new FlightTemplate("CA", "CA123", "PEK", "ICN", 
                LocalTime.of(13, 20), LocalTime.of(16, 40), "A321-200"));
        FLIGHT_TEMPLATES.put("CA124", new FlightTemplate("CA", "CA124", "ICN", "PEK", 
                LocalTime.of(18, 30), LocalTime.of(19, 50), "A321-200"));
    }

    @Override
    public FlightValidationRespVO validateFlight(String airlineCode, String flightNo, String departureDate) {
        FlightValidationRespVO response = new FlightValidationRespVO();
        
        try {
            // 验证日期格式
            LocalDate date = LocalDate.parse(departureDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            
            // 检查航班是否存在
            FlightTemplate template = FLIGHT_TEMPLATES.get(flightNo);
            
            if (template == null || !template.getAirlineCode().equals(airlineCode)) {
                response.setValid(false);
                response.setReason("FLIGHT_NOT_FOUND");
                return response;
            }
            
            // 检查是否是历史日期
            if (date.isBefore(LocalDate.now())) {
                response.setValid(false);
                response.setReason("PAST_DATE");
                return response;
            }
            
            // 构建航班信息
            FlightValidationRespVO.FlightInfo flightInfo = new FlightValidationRespVO.FlightInfo();
            flightInfo.setAirlineCode(template.getAirlineCode());
            flightInfo.setFlightNo(template.getFlightNo());
            flightInfo.setDepartureAirport(template.getDepartureAirport());
            flightInfo.setArrivalAirport(template.getArrivalAirport());
            flightInfo.setDepartureTime(LocalDateTime.of(date, template.getDepartureTime()));
            flightInfo.setArrivalTime(LocalDateTime.of(date, template.getArrivalTime()));
            flightInfo.setAircraft(template.getAircraft());
            flightInfo.setAvailableSeats(RandomUtil.randomInt(50, 300)); // 随机可用座位数
            flightInfo.setStatus("NORMAL");
            
            response.setValid(true);
            response.setFlightInfo(flightInfo);
            
        } catch (Exception e) {
            response.setValid(false);
            response.setReason("INVALID_DATE_FORMAT");
        }
        
        return response;
    }

    // 航班模板类
    private static class FlightTemplate {
        private String airlineCode;
        private String flightNo;
        private String departureAirport;
        private String arrivalAirport;
        private LocalTime departureTime;
        private LocalTime arrivalTime;
        private String aircraft;

        public FlightTemplate(String airlineCode, String flightNo, String departureAirport, 
                            String arrivalAirport, LocalTime departureTime, LocalTime arrivalTime, String aircraft) {
            this.airlineCode = airlineCode;
            this.flightNo = flightNo;
            this.departureAirport = departureAirport;
            this.arrivalAirport = arrivalAirport;
            this.departureTime = departureTime;
            this.arrivalTime = arrivalTime;
            this.aircraft = aircraft;
        }

        // Getters
        public String getAirlineCode() { return airlineCode; }
        public String getFlightNo() { return flightNo; }
        public String getDepartureAirport() { return departureAirport; }
        public String getArrivalAirport() { return arrivalAirport; }
        public LocalTime getDepartureTime() { return departureTime; }
        public LocalTime getArrivalTime() { return arrivalTime; }
        public String getAircraft() { return aircraft; }
    }
}