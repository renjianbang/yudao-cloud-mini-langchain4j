package cn.iocoder.yudao.module.travel.service.flightsegments;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.travel.controller.admin.flightsegments.vo.*;
import cn.iocoder.yudao.module.travel.dal.dataobject.flightsegments.FlightSegmentsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.travel.dal.mysql.flightsegments.FlightSegmentsMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
//import static cn.iocoder.yudao.module.studio.enums.ErrorCodeConstants.*;

/**
 * 航班航段 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class FlightSegmentsServiceImpl implements FlightSegmentsService {

    @Resource
    private FlightSegmentsMapper flightSegmentsMapper;

    @Override
    public Long createFlightSegments(FlightSegmentsSaveReqVO createReqVO) {
        // 插入
        FlightSegmentsDO flightSegments = BeanUtils.toBean(createReqVO, FlightSegmentsDO.class);
        flightSegmentsMapper.insert(flightSegments);

        // 返回
        return flightSegments.getId();
    }

    @Override
    public void updateFlightSegments(FlightSegmentsSaveReqVO updateReqVO) {
        // 校验存在
        validateFlightSegmentsExists(updateReqVO.getId());
        // 更新
        FlightSegmentsDO updateObj = BeanUtils.toBean(updateReqVO, FlightSegmentsDO.class);
        flightSegmentsMapper.updateById(updateObj);
    }

    @Override
    public void deleteFlightSegments(Long id) {
        // 校验存在
        validateFlightSegmentsExists(id);
        // 删除
        flightSegmentsMapper.deleteById(id);
    }

    @Override
        public void deleteFlightSegmentsListByIds(List<Long> ids) {
        // 删除
        flightSegmentsMapper.deleteByIds(ids);
        }


    private void validateFlightSegmentsExists(Long id) {
        if (flightSegmentsMapper.selectById(id) == null) {
//            throw exception(FLIGHT_SEGMENTS_NOT_EXISTS);
        }
    }

    @Override
    public FlightSegmentsDO getFlightSegments(Long id) {
        return flightSegmentsMapper.selectById(id);
    }

    @Override
    public PageResult<FlightSegmentsDO> getFlightSegmentsPage(FlightSegmentsPageReqVO pageReqVO) {
        return flightSegmentsMapper.selectPage(pageReqVO);
    }

    @Override
    public List<FlightSegmentsDO> getFlightSegmentsByOrderId(Long orderId) {
        return flightSegmentsMapper.selectList(FlightSegmentsDO::getOrderId, orderId);
    }

    @Override
    public void updateSegmentStatus(Long segmentId, Integer status) {
        FlightSegmentsDO updateObj = new FlightSegmentsDO();
        updateObj.setId(segmentId);
        updateObj.setStatus(status);
        flightSegmentsMapper.updateById(updateObj);
    }

    @Override
    public Long createFlightSegments(FlightSegmentsDO segment) {
        flightSegmentsMapper.insert(segment);
        return segment.getId();
    }

}