package vn.devTool.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import vn.devTool.core.config.configImpl.AuditAwareImpl;

@Configuration
@EnableMongoAuditing(auditorAwareRef = "auditorProvider")
public class MongoConfig {

    @Bean
    public AuditAwareImpl auditorProvider() {
        return new AuditAwareImpl();
    }
}
