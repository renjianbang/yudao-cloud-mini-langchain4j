package cn.iocoder.yudao.module.test.api.dto;

import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 测试模块 - 用户信息响应DTO
 *
 * @author iocoder.cn
 */
@Schema(description = "测试模块 - 用户信息响应 DTO")
@Data
public class TestUserInfoRespDTO {

    @Schema(description = "用户信息", requiredMode = Schema.RequiredMode.REQUIRED)
    private AdminUserRespDTO userInfo;

    @Schema(description = "调用来源", requiredMode = Schema.RequiredMode.REQUIRED, example = "test-server")
    private String source;

    @Schema(description = "调用时间戳", requiredMode = Schema.RequiredMode.REQUIRED, example = "1640995200000")
    private Long timestamp;

    @Schema(description = "测试描述", requiredMode = Schema.RequiredMode.REQUIRED, example = "通过test-server调用system-server获取的用户信息")
    private String description;
}