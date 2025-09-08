package cn.iocoder.yudao.module.travel;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * 应用启动测试
 * 验证Elasticsearch禁用后系统是否能正常启动
 * 
 * @author 芋道源码
 */
@SpringBootTest(classes = TravelServerApplication.class)
@ActiveProfiles("travel") // 使用travel配置文件，其中禁用了Elasticsearch
public class ApplicationStartupTest {

    @Test
    public void contextLoads() {
        // 如果这个测试通过，说明Spring Boot上下文可以正常加载
        // 即使Elasticsearch被禁用，应用仍能正常启动
        System.out.println("应用启动成功！Elasticsearch已被禁用，不再出现分词器配置错误。");
    }
}