package com.example.demo;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @version V1.0.0
 * @ClassName: {@link Swagger2}
 * @Description: Swagger2 配置
 * @author: 厦门智强软件科技
 * @date: 2019/6/12 15:20
 * @Copyright:2019 All rights reserved.
 * 注意：本内容仅限于厦门智强软件科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class Swagger2 {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //为当前包路径
                .apis(RequestHandlerSelectors.basePackage("com.example.demo.controller"))
                .paths(PathSelectors.any())
                .build();
    }


    /**
     * 构建 api文档的详细信息函数,注意这里的注解引用的是哪个
     *
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title("Scaffold 开发测试 Swagger2 构建外部接口 API")
                //版本号
                .version("1.0.0")
                .termsOfServiceUrl("http://localhost:8080/")
                .contact(new Contact("兰州", "http://www.yuangfeng.top", "1627518680@qq.com"))
                //描述
                .description("对于外部SDK远程调用的接口封装")
                .build();
    }
}
