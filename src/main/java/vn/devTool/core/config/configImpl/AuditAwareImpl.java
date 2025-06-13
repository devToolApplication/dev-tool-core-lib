package vn.devTool.core.config.configImpl;

import org.springframework.data.domain.AuditorAware;
import org.slf4j.MDC;
import java.util.Optional;

public class AuditAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        String userId = MDC.get("userId");
        if (userId != null) {
            // ✅ Lấy userId từ request (nếu có)
            return Optional.of(userId);
        } else {
            return Optional.empty();
        }
    }
}
