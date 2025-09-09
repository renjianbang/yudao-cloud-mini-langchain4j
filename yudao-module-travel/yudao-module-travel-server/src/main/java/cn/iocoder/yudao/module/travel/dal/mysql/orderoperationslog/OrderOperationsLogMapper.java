package cn.iocoder.yudao.module.travel.dal.mysql.orderoperationslog;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.travel.dal.dataobject.orderoperationslog.OrderOperationsLogDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.travel.controller.admin.orderoperationslog.vo.*;

/**
 * 订单操作日志 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface OrderOperationsLogMapper extends BaseMapperX<OrderOperationsLogDO> {

    default PageResult<OrderOperationsLogDO> selectPage(OrderOperationsLogPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OrderOperationsLogDO>()
                .eqIfPresent(OrderOperationsLogDO::getOrderId, reqVO.getOrderId())
                .eqIfPresent(OrderOperationsLogDO::getOperationType, reqVO.getOperationType())
                .eqIfPresent(OrderOperationsLogDO::getContentBefore, reqVO.getContentBefore())
                .eqIfPresent(OrderOperationsLogDO::getContentAfter, reqVO.getContentAfter())
                .eqIfPresent(OrderOperationsLogDO::getOperatorId, reqVO.getOperatorId())
                .likeIfPresent(OrderOperationsLogDO::getOperatorName, reqVO.getOperatorName())
                .eqIfPresent(OrderOperationsLogDO::getRemark, reqVO.getRemark())
                .eqIfPresent(OrderOperationsLogDO::getCreatedAt, reqVO.getCreatedAt())
                .orderByDesc(OrderOperationsLogDO::getId));
    }

}