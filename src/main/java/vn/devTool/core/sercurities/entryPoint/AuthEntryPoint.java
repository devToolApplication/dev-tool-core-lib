package vn.devTool.core.sercurities.entryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import vn.devTool.core.base.BaseResponse;
import vn.devTool.core.filter.RequestFilter;
import vn.devTool.core.utils.JsonUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request,
                       HttpServletResponse response,
                       AuthenticationException authException)
      throws IOException {

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json");

    BaseResponse<Object> baseResponse = BaseResponse.error(
        RequestFilter.getTraceId(),
        request.getRequestURI(),
        HttpServletResponse.SC_UNAUTHORIZED,
        "Invalid or expired token"
    );

    response.getWriter().write(JsonUtils.toExactJson(baseResponse));
  }
}
