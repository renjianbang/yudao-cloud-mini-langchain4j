package cn.iocoder.yudao.module.travel.dal.mysql.flightsegments;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.travel.dal.dataobject.flightsegments.FlightSegmentsDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.travel.controller.admin.flightsegments.vo.*;

/**
 * 航班航段 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface FlightSegmentsMapper extends BaseMapperX<FlightSegmentsDO> {

    default PageResult<FlightSegmentsDO> selectPage(FlightSegmentsPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<FlightSegmentsDO>()
                .eqIfPresent(FlightSegmentsDO::getOrderId, reqVO.getOrderId())
                .eqIfPresent(FlightSegmentsDO::getPassengerId, reqVO.getPassengerId())
                .eqIfPresent(FlightSegmentsDO::getSegmentType, reqVO.getSegmentType())
                .eqIfPresent(FlightSegmentsDO::getAirlineCode, reqVO.getAirlineCode())
                .eqIfPresent(FlightSegmentsDO::getFlightNo, reqVO.getFlightNo())
                .eqIfPresent(FlightSegmentsDO::getDepartureAirportCode, reqVO.getDepartureAirportCode())
                .eqIfPresent(FlightSegmentsDO::getArrivalAirportCode, reqVO.getArrivalAirportCode())
                .betweenIfPresent(FlightSegmentsDO::getDepartureTime, reqVO.getDepartureTime())
                .betweenIfPresent(FlightSegmentsDO::getArrivalTime, reqVO.getArrivalTime())
                .eqIfPresent(FlightSegmentsDO::getCabinClass, reqVO.getCabinClass())
                .eqIfPresent(FlightSegmentsDO::getTicketNo, reqVO.getTicketNo())
                .eqIfPresent(FlightSegmentsDO::getStatus, reqVO.getStatus())
                .eqIfPresent(FlightSegmentsDO::getCreatedAt, reqVO.getCreatedAt())
                .eqIfPresent(FlightSegmentsDO::getUpdatedAt, reqVO.getUpdatedAt())
                .orderByDesc(FlightSegmentsDO::getId));
    }

}