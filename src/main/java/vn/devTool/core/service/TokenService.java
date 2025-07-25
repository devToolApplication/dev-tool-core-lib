package vn.devTool.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import vn.devTool.core.base.pojo.KeycloakToken;
import vn.devTool.core.utils.RestTemplateUtil;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RestTemplateUtil restTemplateUtil;

    @Value("${spring.security.oauth2.resourceserver.jwt.token-uri}")
    private String tokenUrl;

    /**
     * Lấy access_token từ Keycloak qua client credentials flow.
     *
     * @param clientId     clientId từ Keycloak
     * @param clientSecret clientSecret từ Keycloak
     * @return access_token string
     */
    public KeycloakToken getToken(String clientId, String clientSecret) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);

        ResponseEntity<KeycloakToken> response = restTemplateUtil.post(
            tokenUrl,
            request,
            KeycloakToken.class
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to retrieve access token from Keycloak. Status: "
                + response.getStatusCode());
        }
    }

}
