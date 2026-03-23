package com.xq.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
 * 解决跨域问题
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    /**
     * 跨域配置
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173", "http://localhost:8080")  // 指定具体的前端地址
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Authorization")  // 暴露必要的响应头
                .maxAge(3600)
                .allowCredentials(true);  // 允许发送 Cookie，必须指定具体的源
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        // MinIO的URL是 http://localhost:9000
        // 静态资源访问路径是 /gymnasium/**
        registry.addResourceHandler("/gymnasium/**")
                .addResourceLocations("http://localhost:9000/gymnasium/");
    }
}
