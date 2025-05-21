package com.zephyr.mpbssecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 允许前端请求的地址（开发阶段可用 *，生产环境请改为前端真实域名）
        config.addAllowedOrigin("http://localhost:5173");
//        config.addAllowedOrigin("*");
        config.setAllowCredentials(true);               // 是否允许携带 cookie
        config.addAllowedHeader("*");                    // 允许所有请求头（包括 Authorization）
        config.addAllowedMethod("*");                    // 允许所有请求方法 GET、POST、OPTIONS 等

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
