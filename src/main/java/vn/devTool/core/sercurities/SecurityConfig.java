package vn.devTool.core.sercurities;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import vn.devTool.core.filter.JwtScopeValidationFilter;
import vn.devTool.core.properties.SecurityProperties;
import vn.devTool.core.sercurities.entryPoint.AuthEntryPoint;

import java.util.List;

@EnableConfigurationProperties
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final SecurityProperties props;
  private final AuthEntryPoint authEntryPoint;
  private final JwtScopeValidationFilter jwtScopeValidationFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.cors(cors -> cors.configurationSource(request -> {
          CorsConfiguration config = new CorsConfiguration();
          config.setAllowedOrigins(props.getCorsAllowUrl());
          config.setAllowedMethods(props.getAllowMethods());
          config.setAllowedHeaders(List.of("*"));
          config.setAllowCredentials(props.getAllowCredentials());
          return config;
        }))
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(props.getPublicRoutes().toArray(new String[0])).permitAll()
            .requestMatchers(HttpMethod.GET, "/**")
            .hasAnyAuthority(props.getScopes().getRead().stream()
                .map(s -> "SCOPE_" + s).toArray(String[]::new))

            .requestMatchers(HttpMethod.POST, "/**")
            .hasAnyAuthority(props.getScopes().getWrite().stream()
                .map(s -> "SCOPE_" + s).toArray(String[]::new))

            .requestMatchers(HttpMethod.PUT, "/**")
            .hasAnyAuthority(props.getScopes().getUpdate().stream()
                .map(s -> "SCOPE_" + s).toArray(String[]::new))

            .requestMatchers(HttpMethod.DELETE, "/**")
            .hasAnyAuthority(props.getScopes().getDelete().stream()
                .map(s -> "SCOPE_" + s).toArray(String[]::new))

            .anyRequest().authenticated()
        )
        .oauth2ResourceServer(oauth2 -> oauth2
            .jwt(Customizer.withDefaults())
            .authenticationEntryPoint(authEntryPoint)
        )
        .addFilterAfter(jwtScopeValidationFilter, BearerTokenAuthenticationFilter.class)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();
  }


  @Bean
  public JwtDecoder jwtDecoder() {
    return JwtDecoders.fromIssuerLocation(
        props.getOauth2().getResourceServer().getJwt().getIssuerUri()
    );
  }
}
