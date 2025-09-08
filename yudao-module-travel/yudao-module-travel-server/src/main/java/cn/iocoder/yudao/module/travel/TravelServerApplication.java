package cn.iocoder.yudao.module.travel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 差旅系统服务启动类
 * 
 * 项目的启动的入口类
 *
 * @author 芋道源码
 */
@SpringBootApplication(scanBasePackages = {"cn.iocoder.yudao.framework", "cn.iocoder.yudao.module.travel"})
public class TravelServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelServerApplication.class, args);
    }

}