package com.net.sparrow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * @author sparrow
 * @date 2025/1/8 下午4:45
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.OAS_30)
				.apiInfo(apiInfo())
				.groupName("SwaggerGroup")
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.net.sparrow.controller"))
				.paths(PathSelectors.any())
				.build();
	}

	/**
	 * 配置基本信息
	 *
	 * @return
	 */
	@Bean
	public ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				//设置文档标题(API名称)
				.title("商城系统")
				//文档描述
				.description("API-接口说明")
				//版本号
				.version("1.0.0")
				.build();
	}


}
