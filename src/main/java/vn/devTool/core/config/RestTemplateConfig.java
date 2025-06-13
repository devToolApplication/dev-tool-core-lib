package vn.devTool.core.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import vn.devTool.core.properties.RestTemplateProperties;

import java.time.Duration;

/**
 * Cấu hình RestTemplate với timeout lấy từ application.yml
 */
@Configuration
@RequiredArgsConstructor
public class RestTemplateConfig {

    private final RestTemplateProperties properties; // Inject properties


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
