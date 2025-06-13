package vn.devTool.core.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.security")
public class SecurityProperties {
    private List<String> publicRoutes;
    private List<String> corsAllowUrl;
    private String tokenHeaderName;
    private Boolean allowCredentials;
    private List<String> allowMethods;

    private OAuth2Properties oauth2;

    @Getter
    @Setter
    public static class OAuth2Properties {
        private ResourceServer resourceServer;

        @Getter
        @Setter
        public static class ResourceServer {
            private Jwt jwt;

            @Getter
            @Setter
            public static class Jwt {
                private String issuerUri;
            }
        }
    }
}
