package cn.iocoder.yudao.module.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * test 模块的启动类
 *
 * @author iocoder.cn
 */
@SpringBootApplication
@EnableFeignClients(basePackages = "cn.iocoder.yudao.module.system.api")
public class TestServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestServerApplication.class, args);
    }

}