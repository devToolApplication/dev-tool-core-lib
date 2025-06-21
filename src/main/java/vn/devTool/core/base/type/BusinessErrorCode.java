package vn.devTool.core.base.type;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Danh sách các mã lỗi chung dùng cho toàn hệ thống.
 * 0-50 cho validate - check tồn tại
 * 51-100 cho lỗi phát sinh trong hệ thống
 * 100-1xx cho lỗi phát sinh ngoài hệ thống
 */
@Getter
public enum BusinessErrorCode {

    // Common error code
    DATA_NOT_FOUND("COMMON_001", "Data not found", HttpStatus.BAD_REQUEST),
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

    KEYCLOAK_SERVER_ERROR("ACCOUNT_100", "Authentication error", HttpStatus.BAD_REQUEST),

    // ==== UPLOAD MCRS ERROR ====
    UPLOAD_STORAGE_NOT_FOUND("UPLOAD_STORAGE_01", "Upload storage not found", HttpStatus.BAD_REQUEST),
    REQUIRED_FIELD_NOT_NULL("UPLOAD_STORAGE_02", "Both storage type and storage id is null", HttpStatus.BAD_REQUEST),
    UPLOAD_FAIL("UPLOAD_STORAGE_03", "Upload file to storage is error", HttpStatus.INTERNAL_SERVER_ERROR),
    UPLOAD_FILE_NOT_FOUND("UPLOAD_STORAGE_4", "Upload file not found", HttpStatus.BAD_REQUEST),

    DOWNLOAD_FILE_ERROR("UPLOAD_STORAGE_100", "Download file error", HttpStatus.INTERNAL_SERVER_ERROR),

    // ==== AI GENERATE MCRS ERROR ====
    AI_MODEL_NOT_FOUND("AI_MODEL_01", "Ai model not found", HttpStatus.BAD_REQUEST),
    AI_MODEL_INVALID_URL("AI_MODEL_02", "Ai model not found url", HttpStatus.BAD_REQUEST),
    AI_MODEL_MISSING_API_KEY("AI_MODEL_03", "Ai model missing api key", HttpStatus.BAD_REQUEST),


    AI_MODEL_REQUEST_FAILED("AI_MODEL_100", "Ai model request fail", HttpStatus.BAD_REQUEST),

    PROMPT_SETTING_NOT_FOUND("PROMPT_SETTING_01", "Prompt setting not found", HttpStatus.BAD_REQUEST),

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
