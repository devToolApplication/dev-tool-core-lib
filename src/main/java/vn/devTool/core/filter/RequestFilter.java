package vn.devTool.core.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
@Log4j2
public class RequestFilter implements Filter {

    public static final String TRACE_ID = "traceId";
    public static final String USER_ID = "userId";
    public static final String PATH = "path";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // Lấy traceId từ header hoặc tạo mới nếu không có
        String traceId = httpRequest.getHeader(TRACE_ID);
        if (traceId == null || traceId.isBlank()) {
            traceId = UUID.randomUUID().toString();
        }

        // Lấy path từ request
        String path = httpRequest.getRequestURI();

        // Gán traceId và path vào MDC (ThreadLocal storage)
        MDC.put(TRACE_ID, traceId);
        MDC.put(PATH, path);

        log.debug("🔗 Trace ID: {} | Path: {}", traceId, path);

        try {
            chain.doFilter(request, response);
        } finally {
            MDC.clear(); // Cleanup để tránh memory leak
        }
    }

    public static String getTraceId() {
        return MDC.get(TRACE_ID);
    }

    public static String getPath() {
        return MDC.get(PATH);
    }

    public static String getUserId() {
        return MDC.get(USER_ID);
    }
}
