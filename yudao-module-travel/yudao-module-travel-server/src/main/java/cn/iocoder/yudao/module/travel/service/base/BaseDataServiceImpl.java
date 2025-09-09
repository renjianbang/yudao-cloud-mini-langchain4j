package cn.iocoder.yudao.module.travel.service.base;

import cn.iocoder.yudao.module.travel.controller.admin.ticket.vo.*;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础数据 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class BaseDataServiceImpl implements BaseDataService {

    @Override
    public List<AirportInfoVO> getAirports() {
        // 实际项目中应该从数据库或外部API获取
        // 这里提供一些常用机场的模拟数据
        List<AirportInfoVO> airports = new ArrayList<>();
        
        airports.add(buildAirport("PVG", "上海浦东国际机场", "上海", "CN", "Asia/Shanghai"));
        airports.add(buildAirport("PEK", "北京首都国际机场", "北京", "CN", "Asia/Shanghai"));
        airports.add(buildAirport("CAN", "广州白云国际机场", "广州", "CN", "Asia/Shanghai"));
        airports.add(buildAirport("SZX", "深圳宝安国际机场", "深圳", "CN", "Asia/Shanghai"));
        airports.add(buildAirport("CTU", "成都天府国际机场", "成都", "CN", "Asia/Shanghai"));
        airports.add(buildAirport("XIY", "西安咸阳国际机场", "西安", "CN", "Asia/Shanghai"));
        airports.add(buildAirport("KMG", "昆明长水国际机场", "昆明", "CN", "Asia/Shanghai"));
        airports.add(buildAirport("URC", "乌鲁木齐地窝堡国际机场", "乌鲁木齐", "CN", "Asia/Shanghai"));
        
        // 国际机场
        airports.add(buildAirport("LAX", "洛杉矶国际机场", "洛杉矶", "US", "America/Los_Angeles"));
        airports.add(buildAirport("JFK", "约翰·肯尼迪国际机场", "纽约", "US", "America/New_York"));
        airports.add(buildAirport("LHR", "伦敦希思罗机场", "伦敦", "GB", "Europe/London"));
        airports.add(buildAirport("CDG", "巴黎戴高乐机场", "巴黎", "FR", "Europe/Paris"));
        airports.add(buildAirport("NRT", "东京成田国际机场", "东京", "JP", "Asia/Tokyo"));
        airports.add(buildAirport("ICN", "首尔仁川国际机场", "首尔", "KR", "Asia/Seoul"));
        airports.add(buildAirport("SIN", "新加坡樟宜机场", "新加坡", "SG", "Asia/Singapore"));
        airports.add(buildAirport("BKK", "曼谷素万那普国际机场", "曼谷", "TH", "Asia/Bangkok"));
        
        return airports;
    }

    @Override
    public List<AirlineInfoVO> getAirlines() {
        // 实际项目中应该从数据库或外部API获取
        // 这里提供一些常用航空公司的模拟数据
        List<AirlineInfoVO> airlines = new ArrayList<>();
        
        // 中国航空公司
        airlines.add(buildAirline("MU", "中国东方航空", "China Eastern Airlines", "CN"));
        airlines.add(buildAirline("CA", "中国国际航空", "Air China", "CN"));
        airlines.add(buildAirline("CZ", "中国南方航空", "China Southern Airlines", "CN"));
        airlines.add(buildAirline("FM", "上海航空", "Shanghai Airlines", "CN"));
        airlines.add(buildAirline("HO", "吉祥航空", "Juneyao Airlines", "CN"));
        airlines.add(buildAirline("9C", "春秋航空", "Spring Airlines", "CN"));
        airlines.add(buildAirline("3U", "四川航空", "Sichuan Airlines", "CN"));
        airlines.add(buildAirline("MF", "厦门航空", "Xiamen Airlines", "CN"));
        airlines.add(buildAirline("8L", "祥鹏航空", "Lucky Air", "CN"));
        airlines.add(buildAirline("KN", "联合航空", "United Eagle Airlines", "CN"));
        
        // 国际航空公司
        airlines.add(buildAirline("UA", "美国联合航空", "United Airlines", "US"));
        airlines.add(buildAirline("AA", "美国航空", "American Airlines", "US"));
        airlines.add(buildAirline("DL", "达美航空", "Delta Air Lines", "US"));
        airlines.add(buildAirline("BA", "英国航空", "British Airways", "GB"));
        airlines.add(buildAirline("AF", "法国航空", "Air France", "FR"));
        airlines.add(buildAirline("LH", "汉莎航空", "Lufthansa", "DE"));
        airlines.add(buildAirline("JL", "日本航空", "Japan Airlines", "JP"));
        airlines.add(buildAirline("NH", "全日空", "All Nippon Airways", "JP"));
        airlines.add(buildAirline("KE", "大韩航空", "Korean Air", "KR"));
        airlines.add(buildAirline("OZ", "韩亚航空", "Asiana Airlines", "KR"));
        airlines.add(buildAirline("SQ", "新加坡航空", "Singapore Airlines", "SG"));
        airlines.add(buildAirline("TG", "泰国国际航空", "Thai Airways", "TH"));
        
        return airlines;
    }

    private AirportInfoVO buildAirport(String code, String name, String city, String country, String timezone) {
        AirportInfoVO airport = new AirportInfoVO();
        airport.setCode(code);
        airport.setName(name);
        airport.setCity(city);
        airport.setCountry(country);
        airport.setTimezone(timezone);
        return airport;
    }

    private AirlineInfoVO buildAirline(String code, String name, String fullName, String country) {
        AirlineInfoVO airline = new AirlineInfoVO();
        airline.setCode(code);
        airline.setName(name);
        airline.setFullName(fullName);
        airline.setCountry(country);
        return airline;
    }
}