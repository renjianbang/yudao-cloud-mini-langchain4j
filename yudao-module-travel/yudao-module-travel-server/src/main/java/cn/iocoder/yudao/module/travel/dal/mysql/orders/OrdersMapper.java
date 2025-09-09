package cn.iocoder.yudao.module.travel.dal.mysql.orders;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.travel.dal.dataobject.orders.OrdersDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.travel.controller.admin.orders.vo.*;

/**
 * 订单 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface OrdersMapper extends BaseMapperX<OrdersDO> {

    default PageResult<OrdersDO> selectPage(OrdersPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OrdersDO>()
                .likeIfPresent(OrdersDO::getOrderNo, reqVO.getOrderNo())
//                .eqIfPresent(OrdersDO::getUserId, reqVO.getUserId())
                .eqIfPresent(OrdersDO::getOrderStatus, reqVO.getOrderStatus())
                .eqIfPresent(OrdersDO::getPaymentStatus, reqVO.getPaymentStatus())
                .eqIfPresent(OrdersDO::getBookingType, reqVO.getBookingType())
                .likeIfPresent(OrdersDO::getContactName, reqVO.getContactName())
                .likeIfPresent(OrdersDO::getContactPhone, reqVO.getContactPhone())
                .betweenIfPresent(OrdersDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(OrdersDO::getId));
    }

    /**
     * 根据订单号查询订单
     */
    default OrdersDO selectByOrderNo(String orderNo) {
        return selectOne(OrdersDO::getOrderNo, orderNo);
    }

}