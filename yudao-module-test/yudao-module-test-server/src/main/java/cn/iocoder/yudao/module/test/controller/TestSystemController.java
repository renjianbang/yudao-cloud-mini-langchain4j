package cn.iocoder.yudao.module.test.controller;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import cn.iocoder.yudao.module.test.api.dto.TestUserInfoRespDTO;
import cn.iocoder.yudao.module.test.enums.ApiConstants;
import cn.iocoder.yudao.module.test.service.TestSystemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 * 测试模块 - 调用System模块的Controller
 *
 * @author iocoder.cn
 */
@RestController
@RequestMapping(ApiConstants.PREFIX)
@Tag(name = "测试模块 - 微服务调用测试")
@RequiredArgsConstructor
@Validated
public class TestSystemController {

    private final TestSystemService testSystemService;

    /**
     * 获取用户信息（测试调用system模块）
     */
    @GetMapping("/system/user/get")
    @Operation(summary = "通过用户 ID 查询用户信息")
    @Parameter(name = "id", description = "用户编号", example = "1", required = true)
    public CommonResult<TestUserInfoRespDTO> getUserInfo(@RequestParam("id") Long userId) {
        return success(testSystemService.getUserInfo(userId));
    }

    /**
     * 获取多个用户信息（测试批量调用）
     */
    @GetMapping("/system/user/list")
    @Operation(summary = "通过用户 ID 查询用户们")
    @Parameter(name = "ids", description = "用户编号列表", example = "1,2,3", required = true)
    public CommonResult<List<AdminUserRespDTO>> getUserList(@RequestParam("ids") List<Long> userIds) {
        return success(testSystemService.getUserList(userIds));
    }

    /**
     * 获取系统用户总数（测试聚合调用）
     */
    @GetMapping("/system/user/count")
    @Operation(summary = "获取系统用户总数")
    public CommonResult<Long> getUserCount() {
        return success(testSystemService.getUserCount());
    }

    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    @Operation(summary = "健康检查")
    public CommonResult<String> health() {
        return success("test-server is running!");
    }
}