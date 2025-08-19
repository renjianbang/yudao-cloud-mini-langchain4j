package cn.iocoder.yudao.module.system.controller.admin.group.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - 用户组 Response VO")
@Data
@ExcelIgnoreUnannotated
public class GroupRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "9643")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "名字", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @ExcelProperty("名字")
    private String name;

    @Schema(description = "描述", example = "你说的对")
    @ExcelProperty("描述")
    private String description;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("状态")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}