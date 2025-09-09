package cn.iocoder.yudao.module.travel.dal.mysql.refund;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.travel.dal.dataobject.refund.RefundOperationLogDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 退票操作日志 Mapper
 * 
 * @author 芋道源码
 */
@Mapper
public interface RefundOperationLogMapper extends BaseMapperX<RefundOperationLogDO> {

    default List<RefundOperationLogDO> selectByRefundId(Long refundId) {
        return selectList(RefundOperationLogDO::getRefundId, refundId);
    }

}