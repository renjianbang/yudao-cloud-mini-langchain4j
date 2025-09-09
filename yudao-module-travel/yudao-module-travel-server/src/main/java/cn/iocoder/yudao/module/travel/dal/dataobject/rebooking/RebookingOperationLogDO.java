package cn.iocoder.yudao.module.travel.dal.dataobject.rebooking;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 改签操作日志 DO
 *
 * @author 芋道源码
 */
@TableName("studio_rebooking_operation_log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RebookingOperationLogDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    
    /**
     * 改签申请ID
     */
    private Long rebookingId;
    
    /**
     * 操作类型
     */
    private String operationType;
    
    /**
     * 操作描述
     */
    private String operationDesc;
    
    /**
     * 操作人
     */
    private String operator;
    
    /**
     * 操作时间
     */
    private LocalDateTime operationTime;

}