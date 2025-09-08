package cn.iocoder.yudao.module.test.service;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import cn.iocoder.yudao.module.test.api.dto.TestUserInfoRespDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 测试模块 - 调用System模块的服务
 *
 * @author iocoder.cn
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TestSystemService {

    private final AdminUserApi adminUserApi;

    /**
     * 获取用户信息（测试调用system模块）
     */
    public TestUserInfoRespDTO getUserInfo(Long userId) {
        log.info("[getUserInfo] 开始调用system模块获取用户信息，userId: {}", userId);
        
        CommonResult<AdminUserRespDTO> result = adminUserApi.getUser(userId);
        if (!result.isSuccess()) {
            log.error("[getUserInfo] 调用system模块失败，错误码: {}, 错误信息: {}", 
                     result.getCode(), result.getMsg());
            throw new RuntimeException("调用system模块失败: " + result.getMsg());
        }

        AdminUserRespDTO userInfo = result.getData();
        log.info("[getUserInfo] 成功获取用户信息: {}", userInfo);

        TestUserInfoRespDTO response = new TestUserInfoRespDTO();
        response.setUserInfo(userInfo);
        response.setSource("test-server");
        response.setTimestamp(System.currentTimeMillis());
        response.setDescription("通过test-server调用system-server获取的用户信息");

        return response;
    }

    /**
     * 获取多个用户信息（测试批量调用）
     */
    public List<AdminUserRespDTO> getUserList(List<Long> userIds) {
        log.info("[getUserList] 开始批量调用system模块获取用户信息，userIds: {}", userIds);
        
        CommonResult<List<AdminUserRespDTO>> result = adminUserApi.getUserList(userIds);
        if (!result.isSuccess()) {
            log.error("[getUserList] 批量调用system模块失败，错误码: {}, 错误信息: {}", 
                     result.getCode(), result.getMsg());
            throw new RuntimeException("批量调用system模块失败: " + result.getMsg());
        }

        List<AdminUserRespDTO> userList = result.getData();
        log.info("[getUserList] 成功获取 {} 个用户信息", userList.size());

        return userList;
    }

    /**
     * 获取系统用户总数（测试聚合调用）
     * 注意：AdminUserApi中没有直接获取用户总数的方法，这里通过获取所有用户然后计算数量的方式模拟
     */
    public Long getUserCount() {
        log.info("[getUserCount] 开始调用system模块获取用户总数");
        // 由于AdminUserApi中没有提供直接获取用户总数的方法，这里只是示例
        // 实际项目中应该在AdminUserApi中添加对应的方法
        throw new UnsupportedOperationException("AdminUserApi中未提供获取用户总数的方法");
    }
}