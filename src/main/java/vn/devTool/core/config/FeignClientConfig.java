//package vn.devTool.core.config;
//
//import feign.RequestInterceptor;
//import feign.Response;
//import feign.codec.ErrorDecoder;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpStatus;
//import vn.devTool.core.exceptions.FeignClientException;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//
//@Configuration
//@ConfigurationProperties(prefix = "feign.client.config.default")
//@Log4j2
//public class FeignClientConfig {
//
//    @Bean
//    public RequestInterceptor requestInterceptor() {
//        return template -> template.header("Content-Type", "application/json");
//    }
//
//    @Bean
//    public ErrorDecoder feignErrorDecoder() {
//        return (methodKey, response) -> {
//            String responseBody = getResponseBody(response);
//            HttpStatus status = HttpStatus.valueOf(response.status());
//
//            log.error("❌ Feign Client Error: [{}] {} - Response: {}", response.status(), response.reason(), responseBody);
//
//            return new FeignClientException(status, "Feign Client Error: " + response.reason(), responseBody);
//        };
//    }
//
//    private String getResponseBody(Response response) {
//        if (response.body() == null) {
//            return "No response body";
//        }
//        try {
//            return new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);
//        } catch (IOException e) {
//            log.error("❌ Error reading Feign response body", e);
//            return "Error reading response body";
//        }
//    }
//}
