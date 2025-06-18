package vn.devTool.core.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import vn.devTool.core.base.BaseResponse;
import vn.devTool.core.properties.SecurityProperties;
import vn.devTool.core.utils.JsonUtils;

import java.io.IOException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class JwtScopeValidationFilter extends OncePerRequestFilter {

    private final SecurityProperties securityProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            Jwt jwt = jwtAuth.getToken();
            MDC.put(RequestFilter.USER_ID, jwt.getClaim("preferred_username"));
            Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
            if (resourceAccess == null || resourceAccess.isEmpty()) {
                sendForbidden(response, "Forbidden: missing or empty resource_access");
                return;
            }

            // Xác định các role cần thiết dựa theo HTTP method
            Set<String> requiredRoles = switch (request.getMethod()) {
                case "GET" -> new HashSet<>(securityProperties.getScopes().getRead());
                case "POST" -> new HashSet<>(securityProperties.getScopes().getWrite());
                case "PUT" -> new HashSet<>(securityProperties.getScopes().getUpdate());
                case "DELETE" -> new HashSet<>(securityProperties.getScopes().getDelete());
                default -> Collections.emptySet();
            };

            // Nếu không yêu cầu role nào cho method này → cho phép luôn
            if (requiredRoles.isEmpty() ||
                (requiredRoles.size() == 1 && requiredRoles.contains(""))) {
                // Không yêu cầu role => pass luôn
                filterChain.doFilter(request, response);
                return;
            }

            // Duyệt các client roles
            boolean hasValidRole = false;

            for (Object clientObj : resourceAccess.values()) {
                if (!(clientObj instanceof Map<?, ?> clientMap)) continue;

                Object rolesObj = clientMap.get("roles");
                if (!(rolesObj instanceof List<?> roles)) continue;

                for (Object role : roles) {
                    if (role instanceof String roleStr && requiredRoles.contains(roleStr)) {
                        hasValidRole = true;
                        break;
                    }
                }

                if (hasValidRole) break;
            }

            if (!hasValidRole) {
                sendForbidden(response, "Forbidden: missing required role for this method");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void sendForbidden(HttpServletResponse response, String message) throws IOException {
        BaseResponse<Void> errorResponse = BaseResponse.error(
            HttpServletResponse.SC_FORBIDDEN,
            message
        );
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write(JsonUtils.toExactJson(errorResponse));
    }
}
