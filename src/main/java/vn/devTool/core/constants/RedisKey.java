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
    public String uploadStorageByStorageTypeAndDefault(String storageType, boolean isDefault) {
        return key("upload-storage:storage-type-default:%s:%s", isDefault, storageType);
    }
    public String uploadStorageFindAll() {
        return key("upload-storage:all");
    }

    // ==== UPLOAD FILE ====
    public String uploadFileById(String id) {
        return key("upload-file:id:%s", id);
    }

    public String uploadFileFindAll() {
        return key("upload-file:all");
    }
}
