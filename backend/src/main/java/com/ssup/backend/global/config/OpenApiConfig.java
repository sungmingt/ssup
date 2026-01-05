package com.ssup.backend.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
@Slf4j
public class OpenApiConfig {

    //https://api.ssup.site/swagger-ui/index.html
    private static final String TITLE = "ssup API Docs";
    private static final String VERSION = "V1.0.0";

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .info(new Info()
                        .title(TITLE)
                        .description(readDescription())
                        .summary("this is summary")
                        .version(VERSION)
                );
    }

    private String readDescription() {
        try {
            ClassPathResource resource = new ClassPathResource("api-docs.md");
            return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("### failed to load api-docs.md");
            return "API Docs description 파일을 불러오는 데 실패했습니다.";
        }
    }
}