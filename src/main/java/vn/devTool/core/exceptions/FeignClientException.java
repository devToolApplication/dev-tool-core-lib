package vn.devTool.core.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FeignClientException extends RuntimeException {

    private final HttpStatus status;
    private final String responseBody;

    public FeignClientException(HttpStatus status, String message, String responseBody) {
        super(message);
        this.status = status;
        this.responseBody = responseBody;
    }
}
