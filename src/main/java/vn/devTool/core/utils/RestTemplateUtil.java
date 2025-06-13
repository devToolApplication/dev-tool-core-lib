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
 * Provides common methods for GET, POST, PUT, and DELETE requests.
 */
@Component
@Log4j2
@RequiredArgsConstructor
public class RestTemplateUtil {

    private final RestTemplate restTemplate;

    /**
     * Sends a GET request to the specified URL and returns the response.
     *
     * @param url          The target URL.
     * @param responseType The expected response type.
     * @param headers      Optional headers to include in the request.
     * @param <T>          The type of the response body.
     * @return ResponseEntity containing the response data.
     */
    public <T> ResponseEntity<T> get(String url, Class<T> responseType, Map<String, String> headers) {
        return exchange(url, HttpMethod.GET, null, responseType, headers);
    }

    /**
     * Sends a POST request to the specified URL with a request body.
     *
     * @param url          The target URL.
     * @param body         The request body.
     * @param responseType The expected response type.
     * @param headers      Optional headers to include in the request.
     * @param <T>          The type of the response body.
     * @param <R>          The type of the request body.
     * @return ResponseEntity containing the response data.
     */
    public <T, R> ResponseEntity<T> post(String url, R body, Class<T> responseType, Map<String, String> headers) {
        return exchange(url, HttpMethod.POST, body, responseType, headers);
    }

    /**
     * Sends a PUT request to the specified URL with a request body.
     *
     * @param url          The target URL.
     * @param body         The request body.
     * @param responseType The expected response type.
     * @param headers      Optional headers to include in the request.
     * @param <T>          The type of the response body.
     * @param <R>          The type of the request body.
     * @return ResponseEntity containing the response data.
     */
    public <T, R> ResponseEntity<T> put(String url, R body, Class<T> responseType, Map<String, String> headers) {
        return exchange(url, HttpMethod.PUT, body, responseType, headers);
    }

    /**
     * Sends a DELETE request to the specified URL.
     *
     * @param url          The target URL.
     * @param responseType The expected response type.
     * @param headers      Optional headers to include in the request.
     * @param <T>          The type of the response body.
     * @return ResponseEntity containing the response data.
     */
    public <T> ResponseEntity<T> delete(String url, Class<T> responseType, Map<String, String> headers) {
        return exchange(url, HttpMethod.DELETE, null, responseType, headers);
    }

    /**
     * Handles the HTTP request execution for all HTTP methods.
     * Logs errors if the request fails.
     *
     * @param url          The target URL.
     * @param method       The HTTP method to use (GET, POST, PUT, DELETE).
     * @param body         The request body (if applicable).
     * @param responseType The expected response type.
     * @param headers      Headers to include in the request.
     * @param <T>          The type of the response body.
     * @param <R>          The type of the request body.
     * @return ResponseEntity containing the response data.
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
            log.error("❌ API Error [{}]: {} - {}", e.getStatusCode(), e.getMessage(), e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (Exception e) {
            log.error("❌ Unexpected Error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
