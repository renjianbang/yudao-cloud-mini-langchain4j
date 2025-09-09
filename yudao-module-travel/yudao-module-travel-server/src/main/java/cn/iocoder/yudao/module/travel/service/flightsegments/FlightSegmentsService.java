package cn.iocoder.yudao.module.travel.service.flightsegments;

import java.util.*;
import jakarta.validation.*;
import cn.iocoder.yudao.module.travel.controller.admin.flightsegments.vo.*;
import cn.iocoder.yudao.module.travel.dal.dataobject.flightsegments.FlightSegmentsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 航班航段 Service 接口
 *
 * @author 芋道源码
 */
public interface FlightSegmentsService {

    /**
     * 创建航班航段
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createFlightSegments(@Valid FlightSegmentsSaveReqVO createReqVO);

    /**
     * 更新航班航段
     *
     * @param updateReqVO 更新信息
     */
    void updateFlightSegments(@Valid FlightSegmentsSaveReqVO updateReqVO);

    /**
     * 删除航班航段
     *
     * @param id 编号
     */
    void deleteFlightSegments(Long id);

    /**
    * 批量删除航班航段
    *
    * @param ids 编号
    */
    void deleteFlightSegmentsListByIds(List<Long> ids);

    /**
     * 获得航班航段
     *
     * @param id 编号
     * @return 航班航段
     */
    FlightSegmentsDO getFlightSegments(Long id);

    /**
     * 获得航班航段分页
     *
     * @param pageReqVO 分页查询
     * @return 航班航段分页
     */
    PageResult<FlightSegmentsDO> getFlightSegmentsPage(FlightSegmentsPageReqVO pageReqVO);

    /**
     * 根据订单ID获取航段列表
     *
     * @param orderId 订单ID
     * @return 航段列表
     */
    List<FlightSegmentsDO> getFlightSegmentsByOrderId(Long orderId);

    /**
     * 更新航段状态
     *
     * @param segmentId 航段ID
     * @param status 状态
     */
    void updateSegmentStatus(Long segmentId, Integer status);

    /**
     * 创建航段
     *
     * @param segment 航段信息
     * @return 航段ID
     */
    Long createFlightSegments(FlightSegmentsDO segment);

}