package vn.devTool.core.config;

import feign.Contract;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import vn.devTool.core.exceptions.FeignClientException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
@Log4j2
public class FeignClientConfig {

}
