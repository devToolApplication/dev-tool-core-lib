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

    // Account service
    ACCOUNT_NOT_FOUND("ACCOUNT_01", "Invalid username or password", HttpStatus.BAD_REQUEST),
    KEYCLOAK_SERVER_ERROR("ACCOUNT_02", "Authentication error", HttpStatus.BAD_REQUEST),

    // ==== UPLOAD MCRS ERROR ====
    UPLOAD_STORAGE_NOT_FOUND("UPLOAD_STORAGE_01", "Upload storage not found", HttpStatus.BAD_REQUEST),
    REQUIRED_FIELD_NOT_NULL("UPLOAD_STORAGE_02", "Both storage type and storage id is null", HttpStatus.BAD_REQUEST),
    UPLOAD_FAIL("UPLOAD_STORAGE_03", "Upload file to storage is error", HttpStatus.INTERNAL_SERVER_ERROR),

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
