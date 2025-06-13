package vn.devTool.core.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RedisKey {

    // Tiền tố cho tất cả khóa Redis (lấy từ ENV hoặc application.yaml)
    @Value("${spring.data.redis.prefix:app}") // Mặc định là "app" nếu không cấu hình
    private String prefix;

    // =================== USER ===================
    public final String USERS = ":users:";
    public final String USER_PAGE = ":users:page:";
    public final String USER_BY_ID = ":user:id:%s";
    // =================== ACCOUNT ===================
    public final String ACCOUNT_BY_ID = ":account:id:%s";
    public final String ACCOUNT_BY_USERNAME = ":account:username:%s";
    // =================== WALLET ===================
    public final String WALLET_BY_USER_ID = ":wallet:user_id:%s";
    // =================== STORE ===================
    public final String STORE_BY_ID = ":store:id:%s";
    public final String STORE_BRANCH_BY_ID = ":store_branch:%s";

    // =================== CATEGORY ===================
    public final String CATEGORY_BY_ID = ":category:id:%s";

    // =================== PRODUCT ===================
    public final String PRODUCT_BY_ID = ":product:id:%s";
    public final String PRODUCT_GROUP_BY_ID = ":product:id:%s";

    // =================== CONFIG ===================
    public final String CONFIG_BY_KEY = ":config:key:%s";

    // =================== COMMON FUNCTIONS ===================

    /**
     * 📝 Tạo key với prefix
     */
    public String format(String pattern, Object... args) {
        return prefix + ":" + String.format(pattern, args);
    }
}
