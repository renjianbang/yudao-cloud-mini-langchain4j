package cn.iocoder.yudao.module.travel.service.flight;

import cn.iocoder.yudao.module.travel.controller.admin.ticket.vo.FlightValidationRespVO;

/**
 * 航班 Service 接口
 *
 * @author 芋道源码
 */
public interface FlightService {

    /**
     * 验证航班信息
     *
     * @param airlineCode 航司二字码
     * @param flightNo 航班号
     * @param departureDate 出发日期
     * @return 航班验证结果
     */
    FlightValidationRespVO validateFlight(String airlineCode, String flightNo, String departureDate);

}