package vn.devTool.core.base.type;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Danh sách các mã lỗi chung dùng cho toàn hệ thống.
 */
@Getter
public enum BusinessErrorCode {

    // Common error code
    BAD_REQUEST("COMMON_400", "Bad request", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("COMMON_401", "Unauthorized", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("COMMON_403", "Forbidden", HttpStatus.FORBIDDEN),
    NOT_FOUND("COMMON_404", "Resource not found", HttpStatus.NOT_FOUND),
    METHOD_NOT_ALLOWED("COMMON_405", "Method not allowed", HttpStatus.METHOD_NOT_ALLOWED),
    CONFLICT("COMMON_409", "Conflict", HttpStatus.CONFLICT),
    UNSUPPORTED_MEDIA_TYPE("COMMON_415", "Unsupported media type", HttpStatus.UNSUPPORTED_MEDIA_TYPE),
    INTERNAL_SERVER_ERROR("COMMON_500", "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    SERVICE_UNAVAILABLE("COMMON_503", "Service unavailable", HttpStatus.SERVICE_UNAVAILABLE),
    JSON_PARSE_ERROR("COMMON_1000", "Json parse error", HttpStatus.SERVICE_UNAVAILABLE),

    // Account error code
    ACCOUNT_NOT_FOUND("ACCOUNT_01", "Account not found", HttpStatus.BAD_REQUEST),

    // User error code
    USER_NOT_FOUND("USER_01", "User not found", HttpStatus.BAD_REQUEST),
    USER_EXISTS("USER_02", "User already exists", HttpStatus.BAD_REQUEST),

    // Wallet error code
    WALLET_NOT_ENOUGH_BALANCE("WALLET_01", "Wallet not enough balance", HttpStatus.BAD_REQUEST),

    // Store error code
    STORE_NOT_FOUND("STORE_01", "Store not found", HttpStatus.BAD_REQUEST),

    // Store branch error code
    STORE_BRANCH_NOT_FOUND("STORE_BRANCH_01", "Store branch not found", HttpStatus.BAD_REQUEST),

    // Category error code
    CATEGORY_NOT_FOUND("CATEGORY_01", "Category not found", HttpStatus.BAD_REQUEST),

    // Product error code
    PRODUCT_NOT_FOUND("PRODUCT_01", "Product not found", HttpStatus.BAD_REQUEST),

    ;

    private final String code;
    private final String message;
    private final HttpStatus status;

    BusinessErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
