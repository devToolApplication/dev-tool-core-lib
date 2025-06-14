package vn.devTool.core.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

            String clientId = jwt.getClaimAsString("client_id");
            List<String> scopes = jwt.getClaimAsStringList("scope");

            Set<String> requiredScopes = new HashSet<>();
            requiredScopes.addAll(securityProperties.getScopes().getRead());
            requiredScopes.addAll(securityProperties.getScopes().getWrite());
            requiredScopes.addAll(securityProperties.getScopes().getUpdate());
            requiredScopes.addAll(securityProperties.getScopes().getDelete());

            boolean invalidClient = !securityProperties.getClients().getAllowed().contains(clientId);
            boolean invalidScope = scopes == null || Collections.disjoint(scopes, requiredScopes);

            if (invalidClient || invalidScope) {
                BaseResponse<Void> errorResponse = BaseResponse.error(
                    HttpServletResponse.SC_FORBIDDEN,
                    "Forbidden: invalid client or scope"
                );

                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                response.getWriter().write(JsonUtils.toExactJson(errorResponse));
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
