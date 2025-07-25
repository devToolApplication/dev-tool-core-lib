package vn.devTool.core.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.function.Supplier;

import org.springframework.data.domain.Page;

@Component
public class CacheUtil {
    private static final long CACHE_DEFAULT = 3600;
    private static final long CACHE_6H = 3600 * 6;
    private static final long CACHE_12H = 3600 * 12;
    private static final long CACHE_24H = 3600 * 24;
    @Autowired private RedisTemplate<String, Object> redisTemplate;

    /**
     * 📝 Lưu giá trị vào Redis với thời gian hết hạn (Object thông thường)
     */
    public void set(String key, Object value, long time) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
    }

    /**
     * 📝 Lưu giá trị vào Redis không có thời gian hết hạn (Object thông thường)
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 🔍 Lấy giá trị từ Redis (Object thông thường)
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * ❌ Xóa một khóa trong Redis
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * ❌ Xóa một khóa trong Redis
     */
    public void delete(List<String> keys) {
        redisTemplate.delete(keys);
    }

    // ========================== HỖ TRỢ JSON ==========================

    /**
     * 📝 Lưu Object vào Redis dưới dạng JSON
     */
    public void setJson(String key, Object value, long time) {
        String json = JsonUtils.toJson(value);
        if (json != null) {
            redisTemplate.opsForValue().set(key, json, time, TimeUnit.SECONDS);
        }
    }

    /**
     * 📝 Lưu Object vào Redis dưới dạng JSON (không có thời gian hết hạn)
     */
    public void setJson(String key, Object value) {
        String json = JsonUtils.toJson(value);
        if (json != null) {
            redisTemplate.opsForValue().set(key, json, CACHE_DEFAULT, TimeUnit.SECONDS);
        }
    }

    /**
     * 🔍 Lấy Object từ Redis dưới dạng JSON
     */
    public <T> T getJson(String key, Class<T> clazz) {
        String json = (String) redisTemplate.opsForValue().get(key);
        if(json == null){
            return null;
        }
        return JsonUtils.fromJson(json, clazz);
    }

    public <T> T getJson(String key, Class<T> clazz, Supplier<T> fallback) {
        String json = (String) this.redisTemplate.opsForValue().get(key);
        if (json != null) {
            return JsonUtils.fromJson(json, clazz);
        }

        // Nếu cache không có, gọi fallback, lưu vào Redis nếu kết quả khác null
        T result = fallback.get();
        if (result != null) {
            this.setJson(key, result);
        }
        return result;
    }


    // ========================== HỖ TRỢ LIST<T> ==========================

    /**
     * 📝 Lưu danh sách vào Redis dưới dạng JSON
     */
    public <T> void setList(String key, List<T> list, long time) {
        String json = JsonUtils.toJson(list);
        if (json != null) {
            redisTemplate.opsForValue().set(key, json, time, TimeUnit.SECONDS);
        }
    }

    /**
     * 📝 Lưu danh sách vào Redis dưới dạng JSON (không có thời gian hết hạn)
     */
    public <T> void setList(String key, List<T> list) {
        String json = JsonUtils.toJson(list);
        if (json != null) {
            redisTemplate.opsForValue().set(key, json);
        }
    }

    /**
     * 🔍 Lấy danh sách từ Redis dưới dạng JSON, fallback nếu không có
     */
    public <T> List<T> getList(String key, Class<T> clazz, Supplier<List<T>> fallback) {
        String json = (String) redisTemplate.opsForValue().get(key);
        if (json != null) {
            return JsonUtils.fromJsonToList(json, clazz);
        }

        List<T> result = fallback.get();
        if (result != null && !result.isEmpty()) {
            this.setJson(key, result);
        }
        return result;
    }


    /**
     * 🔍 Lấy danh sách từ Redis dưới dạng JSON
     */
    public <T> List<T> getList(String key, Class<T> clazz) {
        String json = (String) redisTemplate.opsForValue().get(key);
        return JsonUtils.fromJsonToList(json, clazz);
    }

    // ========================== HỖ TRỢ PAGE<T> ==========================

    /**
     * 📝 Lưu Page vào Redis dưới dạng JSON
     */
    public <T> void setPage(String key, Page<T> page, long time) {
        String json = JsonUtils.toJson(page);
        if (json != null) {
            redisTemplate.opsForValue().set(key, json, time, TimeUnit.SECONDS);
        }
    }

    /**
     * 📝 Lưu Page vào Redis dưới dạng JSON (không có thời gian hết hạn)
     */
    public <T> void setPage(String key, Page<T> page) {
        String json = JsonUtils.toJson(page);
        if (json != null) {
            redisTemplate.opsForValue().set(key, json);
        }
    }

    /**
     * 🔍 Lấy Page từ Redis dưới dạng JSON
     */
    public <T> Page<T> getPage(String key, Class<T> clazz) {
        String json = (String) redisTemplate.opsForValue().get(key);
        return JsonUtils.fromJsonToPage(json, clazz);
    }
}
