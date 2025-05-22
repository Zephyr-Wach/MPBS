package com.zephyr.mpbsfiles.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.upload-path}")
    private String systemUploadPath;

    private String mediaPath;

    @PostConstruct
    public void init() {
        // 确保路径末尾有 /
        if (!systemUploadPath.endsWith("/")) {
            systemUploadPath += "/";
        }
        mediaPath = "file:" + systemUploadPath ;
//                + "Media/";
        System.out.println("映射的静态资源路径：" + mediaPath);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations(mediaPath);
    }
}
