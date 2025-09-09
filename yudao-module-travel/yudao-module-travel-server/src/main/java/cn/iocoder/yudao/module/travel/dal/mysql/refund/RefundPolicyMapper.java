package cn.iocoder.yudao.module.travel.dal.mysql.refund;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.travel.dal.dataobject.refund.RefundPolicyDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;

/**
 * 退票政策 Mapper
 * 
 * @author 芋道源码
 */
@Mapper
public interface RefundPolicyMapper extends BaseMapperX<RefundPolicyDO> {

    default RefundPolicyDO selectByAirlineAndCabinAndType(String airlineCode, String cabinClass, Integer refundType) {
        LocalDate today = LocalDate.now();
        return selectOne(new LambdaQueryWrapperX<RefundPolicyDO>()
                .eq(RefundPolicyDO::getAirlineCode, airlineCode)
                .eq(RefundPolicyDO::getCabinClass, cabinClass)
                .eq(RefundPolicyDO::getRefundType, refundType)
                .eq(RefundPolicyDO::getStatus, 1) // 启用状态
                .le(RefundPolicyDO::getEffectiveDate, today)
                .and(wrapper -> wrapper.isNull(RefundPolicyDO::getExpireDate)
                        .or()
                        .ge(RefundPolicyDO::getExpireDate, today))
                .orderByDesc(RefundPolicyDO::getEffectiveDate)
                .last("LIMIT 1"));
    }

}