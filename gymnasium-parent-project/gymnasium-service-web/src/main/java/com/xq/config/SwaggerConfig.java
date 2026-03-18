package com.xq.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig{
    /**
     * 创建Docket类型的对象,并使用Spring容器管理
     * Docket是Swagger中的全局配置对象
     * @return
     */
    @Bean
    public Docket getDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(swaggerDemoApiInfo())
                .select()
                .build();
    }
    private ApiInfo swaggerDemoApiInfo(){
        return new ApiInfoBuilder()
                .contact(new Contact("酷我运动健身房",
                        "http://localhost:8080/",
                        "2865165078@qq.com")) //配置文档主体内容
                    //文档标题
                .title("酷我运动健身房")
                    //文档描述
                .description("这里是健身房的描述")
                    //文档版本
                .version("1.0.0")
                .build();
    }
}
