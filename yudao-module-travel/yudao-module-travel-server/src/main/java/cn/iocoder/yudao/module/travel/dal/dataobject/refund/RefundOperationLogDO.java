package cn.iocoder.yudao.module.travel.dal.dataobject.refund;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 退票操作日志 DO
 * 
 * @author 芋道源码
 */
@TableName("studio_refund_operation_log")
@KeySequence("studio_refund_operation_log_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundOperationLogDO extends BaseDO {

    @TableId
    private Long id;
    
    /** 退票申请ID */
    private Long refundId;
    
    /** 操作类型 (CREATE, APPROVE, REJECT, EXECUTE, CANCEL) */
    private String operationType;
    
    /** 操作描述 */
    private String operationDesc;
    
    /** 操作前的数据快照 */
    private String contentBefore;
    
    /** 操作后的数据快照 */
    private String contentAfter;
    
    /** 操作人ID */
    private Long operatorId;
    
    /** 操作人姓名 */
    private String operatorName;
    
    /** 操作IP */
    private String operatorIp;
    
    /** 操作备注 */
    private String remark;

}