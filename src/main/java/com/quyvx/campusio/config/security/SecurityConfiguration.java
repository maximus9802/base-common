/*
 * SecurityConfiguration.java
 */
package com.quyvx.campusio.config.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.StringUtils;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@PropertySource("classpath:api-public-path.properties")
public class SecurityConfiguration {

  @Value("${path.public.get-method:}")
  String getMethodPublicPaths;
  @Value("${path.public.post-method:}")
  String postMethodPublicPaths;
  @Value("${path.public.put-method:}")
  String putMethodPublicPaths;
  @Value("${path.public.delete-method:}")
  String deleteMethodPublicPaths;

  private static final String COMMA = ",";

  @Bean
  public SecurityFilterChain api(HttpSecurity http, JwtAuthenticationConverter jwtAuthConverter)
      throws Exception {
    http
        .cors(Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
        .authorizeHttpRequests(auth -> {
          if (StringUtils.hasText(getMethodPublicPaths)) {
            auth.requestMatchers(HttpMethod.GET,
                    StringUtils.trimArrayElements(getMethodPublicPaths.split(COMMA)))
                .permitAll();
          }
          if (StringUtils.hasText(postMethodPublicPaths)) {
            auth.requestMatchers(HttpMethod.POST,
                    StringUtils.trimArrayElements(postMethodPublicPaths.split(COMMA)))
                .permitAll();
          }
          if (StringUtils.hasText(putMethodPublicPaths)) {
            auth.requestMatchers(HttpMethod.PUT,
                    StringUtils.trimArrayElements(putMethodPublicPaths.split(COMMA)))
                .permitAll();
          }
          if (StringUtils.hasText(deleteMethodPublicPaths)) {
            auth.requestMatchers(HttpMethod.DELETE,
                    StringUtils.trimArrayElements(deleteMethodPublicPaths.split(COMMA)))
                .permitAll();
          }

          auth.anyRequest().authenticated();
        })
        .oauth2ResourceServer(
            oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)));
    return http.build();
  }

  @Bean
  public JwtAuthenticationConverter jwtAuthenticationConverter() {
    var converter = new JwtAuthenticationConverter();

    converter.setJwtGrantedAuthoritiesConverter(jwt -> {
      Collection<GrantedAuthority> authorities = new ArrayList<>();

      // Get all realm role
      Map<String, Object> realmAccess = jwt.getClaim("realm_access");
      if (realmAccess != null && realmAccess.get("roles") instanceof Collection<?> roles) {
        roles.forEach(r -> authorities.add(new SimpleGrantedAuthority("ROLE_" + r)));
      }

      // Get all client role
      Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
      if (resourceAccess != null) {
        resourceAccess.values().forEach(val -> {
          if (val instanceof Map<?, ?> m && m.get("roles") instanceof Collection<?> clientRoles) {
            clientRoles.forEach(r -> authorities.add(new SimpleGrantedAuthority("ROLE_" + r)));
          }
        });
      }

      return authorities;
    });
    return converter;
  }

}
