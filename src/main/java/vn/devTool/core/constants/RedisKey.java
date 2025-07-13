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

    // ==== AI MODEL ====
    public String aiModelById(String id) {
        return key("ai-model:id:%s", id);
    }

    public String aiModelFindAll() {
        return key("ai-model:all");
    }

    public String aiModelByStorageTypeAndDefault(String modelType, boolean isDefault) {
        return key("ai-model:model-type-default:%s:%s", isDefault, modelType);
    }

    // ==== CHART DATA ====

    /**
     * Key lưu dữ liệu 1 cây nến theo symbol và utcOpenTime
     * Ví dụ: app:chart:BTCUSDT:2024-07-12T15:30:00
     */
    public String chartBySymbolAndUtcTime(String symbol, String utcOpenTime) {
        return key("chart:%s:%s", symbol.toUpperCase(), utcOpenTime);
    }

    /**
     * Key lưu vùng dữ liệu chart theo symbol và khoảng thời gian.
     * Ví dụ: app:chart:BTCUSDT:range:2024-07-10T00:00:00_2024-07-12T00:00:00
     */
    public String chartBySymbolAndRange(String symbol, String startUtc, String endUtc) {
        return key("chart:%s:range:%s_%s", symbol.toUpperCase(), startUtc, endUtc);
    }

    /**
     * Key lưu 1 cây nến cụ thể theo symbol, interval, và thời gian mở nến.
     * Ví dụ: app:chart:BTCUSDT:1m:2024-07-12T15:30:00
     */
    public String chartBySymbolIntervalAndTime(String symbol, String interval, String utcOpenTime) {
        return key("chart:%s:%s:%s", symbol, interval, utcOpenTime);
    }

    public String syncConfigById(String id) {
        return this.key("sync-config:id:%s", id);
    }

    public String syncConfigFindAll() {
        return this.key("sync-config:all");
    }

    public String syncConfigPage(String params) {
        return this.key("sync-config:page:%s", params);
    }

    public String lastSyncedAt(String configId, String intervalName) {
        return String.format("sync-config:lastSyncedAt:%s:%s", configId, intervalName);
    }

    public String configFindAll() {
        return this.key("config:all");
    }

    public String configPage(String params) {
        return this.key("config:page:%s", params);
    }

    public String configById(String id) {
        return this.key("config:id:%s", id);
    }

    public String configByKey(String key) {
        return this.key("config:key:%s", key);
    }
}
