package cn.iocoder.yudao.module.travel.dal.mysql.orderfees;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.travel.dal.dataobject.orderfees.OrderFeesDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.travel.controller.admin.orderfees.vo.*;

/**
 * 订单费用明细 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface OrderFeesMapper extends BaseMapperX<OrderFeesDO> {

    default PageResult<OrderFeesDO> selectPage(OrderFeesPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OrderFeesDO>()
                .eqIfPresent(OrderFeesDO::getOrderId, reqVO.getOrderId())
                .eqIfPresent(OrderFeesDO::getPassengerId, reqVO.getPassengerId())
                .eqIfPresent(OrderFeesDO::getFeeType, reqVO.getFeeType())
                .eqIfPresent(OrderFeesDO::getAmount, reqVO.getAmount())
                .eqIfPresent(OrderFeesDO::getCurrency, reqVO.getCurrency())
                .eqIfPresent(OrderFeesDO::getDescription, reqVO.getDescription())
                .eqIfPresent(OrderFeesDO::getCreatedAt, reqVO.getCreatedAt())
                .eqIfPresent(OrderFeesDO::getUpdatedAt, reqVO.getUpdatedAt())
                .orderByDesc(OrderFeesDO::getId));
    }

}