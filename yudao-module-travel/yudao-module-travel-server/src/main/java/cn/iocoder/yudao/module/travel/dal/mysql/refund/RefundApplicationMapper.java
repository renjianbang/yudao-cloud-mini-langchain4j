package cn.iocoder.yudao.module.travel.dal.mysql.refund;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.travel.controller.admin.refund.vo.RefundApplicationPageReqVO;
import cn.iocoder.yudao.module.travel.dal.dataobject.refund.RefundApplicationDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 退票申请 Mapper
 * 
 * @author 芋道源码
 */
@Mapper
public interface RefundApplicationMapper extends BaseMapperX<RefundApplicationDO> {

    default PageResult<RefundApplicationDO> selectPage(RefundApplicationPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<RefundApplicationDO>()
                .likeIfPresent(RefundApplicationDO::getOrderNo, reqVO.getOrderNo())
                .likeIfPresent(RefundApplicationDO::getPassengerName, reqVO.getPassengerName())
                .eqIfPresent(RefundApplicationDO::getFlightNo, reqVO.getFlightNo())
                .eqIfPresent(RefundApplicationDO::getRefundType, reqVO.getRefundType())
                .eqIfPresent(RefundApplicationDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(RefundApplicationDO::getCreateTime, 
                        reqVO.getCreatedTimeStart(), reqVO.getCreatedTimeEnd())
                .orderByDesc(RefundApplicationDO::getId));
    }

    default List<RefundApplicationDO> selectByOrderId(Long orderId) {
        return selectList(RefundApplicationDO::getOrderId, orderId);
    }

    default RefundApplicationDO selectByOrderNoAndPassengerId(String orderNo, Long passengerId) {
        return selectOne(new LambdaQueryWrapperX<RefundApplicationDO>()
                .eq(RefundApplicationDO::getOrderNo, orderNo)
                .eq(RefundApplicationDO::getPassengerId, passengerId)
                .orderByDesc(RefundApplicationDO::getId)
                .last("LIMIT 1"));
    }

}