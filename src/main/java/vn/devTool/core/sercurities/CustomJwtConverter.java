package vn.devTool.core.sercurities;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import vn.devTool.core.properties.SecurityProperties;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomJwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final SecurityProperties securityProperties;

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        // Kiểm tra client_id
        String clientId = jwt.getClaimAsString("client_id");
        if (!securityProperties.getClients().getAllowed().contains(clientId)) {
            throw new AccessDeniedException("Unauthorized client_id: " + clientId);  // Thay RuntimeException bằng AccessDeniedException
        }

        // Kiểm tra scope
        List<String> requiredScopes = securityProperties.getScopes().getRequired();
        List<String> tokenScopes = jwt.getClaimAsStringList("scope");

        if (tokenScopes == null || !new HashSet<>(tokenScopes).containsAll(requiredScopes)) {
            throw new AccessDeniedException("Token missing required scopes: " + requiredScopes);  // Thay RuntimeException bằng AccessDeniedException
        }

        // Lấy authorities từ scope
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix("SCOPE_");
        authoritiesConverter.setAuthoritiesClaimName("scope");
        Collection<GrantedAuthority> authorities = authoritiesConverter.convert(jwt);

        return new JwtAuthenticationToken(jwt, authorities, jwt.getSubject());
    }
}
