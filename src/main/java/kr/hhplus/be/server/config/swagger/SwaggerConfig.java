package kr.hhplus.be.server.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import jdk.javadoc.doclet.Doclet;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@OpenAPIDefinition(
    info = @Info(
        title = "e-커머스 상품 주문 서비스 API 명세서",
        description = "사용자 포인트, 상품, 쿠폰, 주문 기능을 제공하는 e-커머스 주문 API 명세서",
        version = "v1"
    ))
@Configuration
@RequiredArgsConstructor

public class SwaggerConfig {
    @Bean
    public GroupedOpenApi chatOpenApi() {


        return GroupedOpenApi.builder()
            .group("e커머스 API v1")
            .pathsToMatch("/**")
            .build();
    }
}
