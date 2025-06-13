package vn.devTool.core.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import vn.devTool.core.base.type.BusinessErrorCode;

@Getter
public class BusinessException extends RuntimeException {
    private final String errorCode;
    private final HttpStatus status;

    public BusinessException(String errorCode, String message, HttpStatus status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }

    public BusinessException(BusinessErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode.getCode();
        this.status = errorCode.getStatus();
    }

    public BusinessException(BusinessErrorCode errorCode, Exception exception) {
        super(exception);
        this.errorCode = errorCode.getCode();
        this.status = errorCode.getStatus();
    }

    public BusinessException(BusinessErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.getCode();
        this.status = errorCode.getStatus();
    }
}
