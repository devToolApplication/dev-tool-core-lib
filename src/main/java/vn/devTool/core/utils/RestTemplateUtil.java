package vn.devTool.core.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Map;

/**
 * Utility class for making HTTP requests using RestTemplate.
 * Supports GET, POST, PUT, DELETE, and custom exchange with full control.
 */
@Component
@Log4j2
@RequiredArgsConstructor
public class RestTemplateUtil {

    private final RestTemplate restTemplate;

    // ========== BASIC METHODS WITH SIMPLE HEADERS ========== //

    public <T> ResponseEntity<T> get(String url, Class<T> responseType, Map<String, String> headers) {
        return exchange(url, HttpMethod.GET, null, responseType, headers);
    }

    public <T, R> ResponseEntity<T> post(String url, R body, Class<T> responseType, Map<String, String> headers) {
        return exchange(url, HttpMethod.POST, body, responseType, headers);
    }

    public <T, R> ResponseEntity<T> put(String url, R body, Class<T> responseType, Map<String, String> headers) {
        return exchange(url, HttpMethod.PUT, body, responseType, headers);
    }

    public <T> ResponseEntity<T> delete(String url, Class<T> responseType, Map<String, String> headers) {
        return exchange(url, HttpMethod.DELETE, null, responseType, headers);
    }

    /**
     * Basic exchange method for convenience (auto-create HttpEntity).
     */
    private <T, R> ResponseEntity<T> exchange(String url, HttpMethod method, R body, Class<T> responseType, Map<String, String> headers) {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            if (headers != null) {
                headers.forEach(httpHeaders::set);
            }

            HttpEntity<R> entity = new HttpEntity<>(body, httpHeaders);
            return restTemplate.exchange(url, method, entity, responseType);
        } catch (HttpStatusCodeException e) {
            log.error("API Error [{}]: {} - {}", e.getStatusCode(), e.getMessage(), e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (Exception e) {
            log.error("Unexpected Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ========== ADVANCED METHOD: FULL CONTROL ========== //

    /**
     * Advanced exchange method with full control using prepared HttpEntity.
     *
     * @param url           Target URL
     * @param method        HTTP Method (GET, POST, PUT, DELETE...)
     * @param requestEntity Pre-constructed HttpEntity with headers and body
     * @param responseType  Expected response class
     * @param <T>           Type of response body
     * @return ResponseEntity<T>
     */
    public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType) {
        try {
            log.debug("Sending request to URL: {}", url);
            log.debug("Method: {}", method);
            log.debug("Headers: {}", requestEntity.getHeaders());
            log.debug("Body: {}", requestEntity.getBody());

            return restTemplate.exchange(url, method, requestEntity, responseType);
        } catch (HttpStatusCodeException e) {
            log.error("API Error [{}]: {} - {}", e.getStatusCode(), e.getMessage(), e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (Exception e) {
            log.error("Unexpected Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ========== OPTIONAL: SHORTCUT FOR POST WITH FULL HttpEntity ========== //

    /**
     * Shortcut for POST method using full HttpEntity.
     */
    public <T> ResponseEntity<T> post(String url, HttpEntity<?> entity, Class<T> responseType) {
        return exchange(url, HttpMethod.POST, entity, responseType);
    }
}
