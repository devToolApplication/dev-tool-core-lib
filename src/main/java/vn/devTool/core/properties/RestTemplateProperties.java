package vn.devTool.core.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Cấu hình properties cho RestTemplate lấy từ application.yml
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "rest-template.client.config.default")
public class RestTemplateProperties {
    private int connectTimeout; // ⏳ Timeout kết nối
    private int readTimeout;    // 📖 Timeout đọc dữ liệu
}
