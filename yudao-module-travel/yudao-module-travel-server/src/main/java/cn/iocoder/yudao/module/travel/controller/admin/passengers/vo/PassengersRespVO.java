package cn.iocoder.yudao.module.travel.controller.admin.passengers.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.time.LocalDate;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - 乘客 Response VO")
@Data
@ExcelIgnoreUnannotated
public class PassengersRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "12345")
    @ExcelProperty("主键ID")
    private Long id;

    @Schema(description = "关联订单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "12345")
    @ExcelProperty("关联订单ID")
    private Long orderId;

    @Schema(description = "乘客类型 (1: 成人, 2: 儿童, 3: 婴儿)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("乘客类型")
    private Integer passengerType;

    @Schema(description = "中文姓名", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("中文姓名")
    private String chineseName;

    @Schema(description = "英文姓名", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("英文姓名")
    private String englishName;

    @Schema(description = "性别 (MALE: 男, FEMALE: 女)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("性别")
    private String gender;

    @Schema(description = "出生日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("出生日期")
    private LocalDate birthday;

    @Schema(description = "证件类型 (ID_CARD: 身份证, PASSPORT: 护照, OTHER: 其他)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("证件类型")
    private String idType;

    @Schema(description = "证件号码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("证件号码")
    private String idNumber;

    @Schema(description = "证件有效期")
    @ExcelProperty("证件有效期")
    private LocalDate idExpiryDate;

    @Schema(description = "国籍", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("国籍")
    private String nationality;

    @Schema(description = "联系电话")
    @ExcelProperty("联系电话")
    private String phone;

    @Schema(description = "邮箱")
    @ExcelProperty("邮箱")
    private String email;

    @Schema(description = "常旅客号")
    @ExcelProperty("常旅客号")
    private String frequentFlyerNo;

    @Schema(description = "餐食偏好")
    @ExcelProperty("餐食偏好")
    private String mealPreference;

    @Schema(description = "座位偏好")
    @ExcelProperty("座位偏好")
    private String seatPreference;

    @Schema(description = "特殊服务需求")
    @ExcelProperty("特殊服务需求")
    private String specialService;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("更新时间")
    private LocalDateTime updateTime;

}