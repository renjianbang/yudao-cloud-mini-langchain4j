package cn.iocoder.yudao.module.travel.framework.security.config;

import cn.iocoder.yudao.framework.security.config.AuthorizeRequestsCustomizer;
import cn.iocoder.yudao.module.travel.enums.ApiConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;

/**
 * Travel 模块的 Security 配置
 * 该类是 Travel 模块的 Spring Security 授权配置扩展：
 *
 *
 * 通过声明名为 travelAuthorizeRequestsCustomizer 的 AuthorizeRequestsCustomizer Bean，向全局安全配置追加本模块的放行规则。
 * 在 customize 方法中允许无需登录访问的端点：Swagger 文档相关路径、Actuator 监控、Druid 监控、以及以 ApiConstants.PREFIX（模块内部 RPC/开放接口前缀）开头的所有请求。
 * 采用模块内独立 @Configuration，便于多模块按需合并安全规则，减少全局配置耦合。
 * 总结：它负责注册并注入一段授权匹配规则，使上述公共/监控/RPC 接口不被权限拦截。
 */
@Configuration(proxyBeanMethods = false, value = "travelSecurityConfiguration")
public class SecurityConfiguration {

    @Bean("travelAuthorizeRequestsCustomizer")
    public AuthorizeRequestsCustomizer authorizeRequestsCustomizer() {
        return new AuthorizeRequestsCustomizer() {

            @Override
            public void customize(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
                // Swagger 接口文档
                registry.requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/webjars/**").permitAll()
                        .requestMatchers("/swagger-ui").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll();
                // Spring Boot Actuator 的安全配置
                registry.requestMatchers("/actuator").permitAll()
                        .requestMatchers("/actuator/**").permitAll();
                // Druid 监控
                registry.requestMatchers("/druid/**").permitAll();
                // RPC 服务的安全配置
                registry.requestMatchers(ApiConstants.PREFIX + "/**").permitAll();
            }

        };
    }

}