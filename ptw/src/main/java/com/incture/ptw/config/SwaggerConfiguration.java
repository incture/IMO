package com.incture.ptw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import static springfox.documentation.builders.PathSelectors.regex;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableTransactionManagement
public class SwaggerConfiguration extends WebMvcConfigurationSupport {
	@Bean
	public Docket version1() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.incture.MasterBUPA.rest")).paths(regex(".*" + "" + ".*"))
				.build().enable(true).groupName("MASTER-BUPA_Version-1.0.0.0")
				.apiInfo(new ApiInfoBuilder().description("MASTER-BUPA_Version-1.0.0.0 APIs")
						.title("MASTER-BUPA_Version-1.0.0.0").version("1.0.0.0").build());
	}

	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

}
