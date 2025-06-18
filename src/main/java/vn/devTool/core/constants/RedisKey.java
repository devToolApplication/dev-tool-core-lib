package vn.devTool.core.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RedisKey {

    @Value("${spring.data.redis.prefix:app}")
    private String prefix;

    private String key(String suffix) {
        return prefix + ":" + suffix;
    }

    private String key(String pattern, Object... args) {
        return prefix + ":" + String.format(pattern, args);
    }

    // ==== UPLOAD STORAGE ====
    public String uploadStorageById(String id) {
        return key("upload-storage:id:%s", id);
    }
    public String uploadStorageFindAll() {
        return key("upload-storage:all");
    }

}
