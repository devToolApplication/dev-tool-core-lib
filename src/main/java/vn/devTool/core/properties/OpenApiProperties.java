package vn.devTool.core.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "springdoc")
public class OpenApiProperties {
    private String title;
    private String description;
    private String version;
    private Contact contact;
    private License license;

    @Getter
    @Setter
    public static class Contact {
        private String name;
        private String email;
    }

    @Getter
    @Setter
    public static class License {
        private String name;
        private String url;
    }
}
