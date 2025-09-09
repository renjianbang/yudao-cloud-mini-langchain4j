package cn.iocoder.yudao.module.travel.controller.admin.passengers.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.time.LocalDate;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 乘客分页 Request VO")
@Data
public class PassengersPageReqVO extends PageParam {

    @Schema(description = "关联订单ID", example = "12345")
    private Long orderId;

    @Schema(description = "乘客类型 (1: 成人, 2: 儿童, 3: 婴儿)", example = "1")
    private Integer passengerType;

    @Schema(description = "中文姓名")
    private String chineseName;

    @Schema(description = "英文姓名")
    private String englishName;

    @Schema(description = "性别 (MALE: 男, FEMALE: 女)")
    private String gender;

    @Schema(description = "证件类型 (ID_CARD: 身份证, PASSPORT: 护照, OTHER: 其他)")
    private String idType;

    @Schema(description = "证件号码")
    private String idNumber;

    @Schema(description = "国籍")
    private String nationality;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}