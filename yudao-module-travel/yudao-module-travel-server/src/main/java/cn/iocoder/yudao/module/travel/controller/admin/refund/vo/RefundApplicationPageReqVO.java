package cn.iocoder.yudao.module.travel.controller.admin.refund.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 退票申请分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RefundApplicationPageReqVO extends PageParam {

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "乘客姓名")
    private String passengerName;

    @Schema(description = "航班号")
    private String flightNo;

    @Schema(description = "退票类型")
    private Integer refundType;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    public LocalDateTime getCreatedTimeStart() {
        return createTime != null && createTime.length >= 1 ? createTime[0] : null;
    }

    public LocalDateTime getCreatedTimeEnd() {
        return createTime != null && createTime.length >= 2 ? createTime[1] : null;
    }

}