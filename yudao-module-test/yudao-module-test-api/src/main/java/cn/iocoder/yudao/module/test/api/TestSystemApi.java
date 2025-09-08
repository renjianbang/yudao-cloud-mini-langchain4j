package cn.iocoder.yudao.module.test.api;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 测试模块 - 调用System模块的API接口
 *
 * @author iocoder.cn
 */
@FeignClient(name = "system-server")
@Tag(name = "测试模块 - System服务调用")
public interface TestSystemApi {

    String PREFIX = "/test/system";

    /**
     * 获取用户信息（测试调用system模块的接口）
     */
    @GetMapping(PREFIX + "/user/get")
    @Operation(summary = "通过用户 ID 查询用户")
    @Parameter(name = "id", description = "用户编号", example = "1", required = true)
    CommonResult<AdminUserRespDTO> getUser(@RequestParam("id") Long id);

    /**
     * 获取多个用户信息（测试批量调用）
     */
    @GetMapping(PREFIX + "/user/list")
    @Operation(summary = "通过用户 ID 查询用户们")
    @Parameter(name = "ids", description = "用户编号列表", example = "[1,2,3]", required = true)
    CommonResult<List<AdminUserRespDTO>> getUserList(@RequestParam("ids") List<Long> ids);

    /**
     * 获取系统当前用户数量（测试聚合调用）
     */
    @GetMapping(PREFIX + "/user/count")
    @Operation(summary = "获取系统用户总数")
    CommonResult<Long> getUserCount();
}