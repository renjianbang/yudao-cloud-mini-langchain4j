package cn.iocoder.yudao.module.travel.controller.admin.rebooking.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 改签申请分页请求 VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RebookingApplicationPageReqVO extends PageParam {

    @Schema(description = "订单ID", example = "1")
    private Long orderId;

    @Schema(description = "乘客ID", example = "1")
    private Long passengerId;

    @Schema(description = "状态", example = "10")
    private Integer status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime[] createTime;

}