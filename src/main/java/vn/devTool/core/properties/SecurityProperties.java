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
    private List<String> allowMethods = List.of("GET", "POST", "PUT", "DELETE", "OPTIONS");
    private Boolean allowCredentials = true;

    private Oauth2Properties oauth2;

    private ClientConfig clients;
    private ScopeConfig scopes;

    @Getter
    @Setter
    public static class ClientConfig {
        private List<String> allowed;
    }

    @Getter
    @Setter
    public static class ScopeConfig {
        private List<String> required;
        private List<String> read;
        private List<String> write;
        private List<String> update;
        private List<String> delete;
    }

    @Getter
    @Setter
    public static class Oauth2Properties {
        private ResourceServerProperties resourceServer;

        @Getter
        @Setter
        public static class ResourceServerProperties {
            private JwtProperties jwt;

            @Getter
            @Setter
            public static class JwtProperties {
                private String issuerUri;
            }
        }
    }
}
