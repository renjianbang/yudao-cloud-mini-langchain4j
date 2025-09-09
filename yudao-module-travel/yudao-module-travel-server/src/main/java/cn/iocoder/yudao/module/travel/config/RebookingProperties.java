package cn.iocoder.yudao.module.travel.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 改签配置属性
 *
 * @author 芋道源码
 */
@Data
@Component
@ConfigurationProperties(prefix = "rebooking")
public class RebookingProperties {

    /**
     * 改签手续费配置
     */
    private ChangeFee changeFee = new ChangeFee();

    /**
     * 服务费配置
     */
    private BigDecimal serviceFee = new BigDecimal("50.00");

    @Data
    public static class ChangeFee {
        /**
         * 国内航班改签费
         */
        private BigDecimal domestic = new BigDecimal("200.00");

        /**
         * 国际航班改签费
         */
        private BigDecimal international = new BigDecimal("500.00");
    }

}