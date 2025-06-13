package vn.devTool.core.exceptions;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import vn.devTool.core.base.BaseResponse;
import vn.devTool.core.base.type.BusinessErrorCode;
import vn.devTool.core.filter.RequestFilter;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<BaseResponse<Void>> handleBusinessException(BusinessException ex) {
    String traceId = RequestFilter.getTraceId();
    String path = RequestFilter.getPath();

    log.warn("⚠️ Business Error [{}]: {}", ex.getErrorCode(), ex.getMessage());

    return ResponseEntity.status(ex.getStatus())
        .body(BaseResponse.error(traceId, path, ex.getStatus().value(), ex.getMessage()));
  }

  @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
  public ResponseEntity<BaseResponse<Void>> handleNotFound(Exception e) {
    String traceId = RequestFilter.getTraceId();
    String path = RequestFilter.getPath();

    log.warn("🔍 Not Found: {}", path);
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(BaseResponse.error(traceId, path, 404, "The requested resource was not found"));
  }

  @ExceptionHandler({AccessDeniedException.class})
  public ResponseEntity<BaseResponse<Void>> handleAccessDeniedException(AccessDeniedException ex) {
    String traceId = RequestFilter.getTraceId();
    String path = RequestFilter.getPath();

    log.warn("🚫 Access Denied at {}: {}", path, ex.getMessage());

    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(BaseResponse.error(traceId, path, HttpStatus.FORBIDDEN.value(), "Access Denied"));
  }

  @ExceptionHandler(FeignClientException.class)
  public ResponseEntity<BaseResponse<Void>> handleFeignClientException(FeignClientException e) {
    String traceId = RequestFilter.getTraceId();
    String path = RequestFilter.getPath();

    // 🛑 Log chi tiết lỗi từ Feign Client
    log.error("🚨 FeignClientException: Status [{}], Message: {}, Response Body: {}",
        e.getStatus().value(), e.getMessage(), e.getResponseBody());

    // 📝 Format lại message để trả về client
    String formattedMessage = String.format("Feign Error: %s | Response: %s",
        e.getMessage(), e.getResponseBody());

    return ResponseEntity.status(e.getStatus())
        .body(BaseResponse.error(traceId, path, BusinessErrorCode.INTERNAL_SERVER_ERROR.getStatus().value(), formattedMessage));
  }
  @ExceptionHandler(Exception.class)
  public ResponseEntity<BaseResponse<Void>> handleGeneralError(Exception ex) {
    String traceId = RequestFilter.getTraceId();
    String path = RequestFilter.getPath();

    log.error("❌ Error at {}: {}", path, ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(BaseResponse.error(traceId, path, 500, "Internal Server Error"));
  }

  /**
   * ❌ Xử lý lỗi validation từ @RequestBody (DTO validation)
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<BaseResponse<Void>> handleValidationException(MethodArgumentNotValidException ex) {
    String traceId = RequestFilter.getTraceId();
    String path = RequestFilter.getPath();

    // 🔍 Lấy danh sách lỗi từ validation
    List<String> errors = ex.getBindingResult().getFieldErrors().stream()
        .map(FieldError::getDefaultMessage)
        .collect(Collectors.toList());

    log.warn("⚠️ Validation Error at {}: {}", path, errors);

    return ResponseEntity.badRequest()
        .body(BaseResponse.error(traceId, path, HttpStatus.BAD_REQUEST.value(), String.join(", ", errors)));
  }

  /**
   * ❌ Xử lý lỗi validation từ @RequestParam, @PathVariable (ví dụ: @Min, @Max)
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<BaseResponse<Void>> handleConstraintViolationException(ConstraintViolationException ex) {
    String traceId = RequestFilter.getTraceId();
    String path = RequestFilter.getPath();

    // 🔍 Lấy danh sách lỗi từ validation
    List<String> errors = ex.getConstraintViolations().stream()
        .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
        .collect(Collectors.toList());

    log.warn("⚠️ Constraint Violation at {}: {}", path, errors);

    return ResponseEntity.badRequest()
        .body(BaseResponse.error(traceId, path, HttpStatus.BAD_REQUEST.value(), String.join(", ", errors)));
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<BaseResponse<Void>> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
    String traceId = RequestFilter.getTraceId();
    String path = RequestFilter.getPath();

    String errorMessage = String.format("Missing required parameter: %s", ex.getParameterName());

    log.warn("⚠️ Missing Request Parameter at {}: {}", path, errorMessage);

    return ResponseEntity.badRequest()
        .body(BaseResponse.error(traceId, path, HttpStatus.BAD_REQUEST.value(), errorMessage));
  }

}
