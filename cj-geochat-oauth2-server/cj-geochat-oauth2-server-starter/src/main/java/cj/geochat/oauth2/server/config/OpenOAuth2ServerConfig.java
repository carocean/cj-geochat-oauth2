package cj.geochat.oauth2.server.config;

import cj.geochat.ability.oauth.server.annotation.EnableCjOAuth2Server;
import cj.geochat.ability.oauth.server.redis.annotation.EnableCjOAuth2ServerForRedis;
import cj.geochat.ability.oauth.server.settings.AuthorizationServerSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableCjOAuth2Server
@EnableCjOAuth2ServerForRedis
@Configuration
public class OpenOAuth2ServerConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .authorizationEndpoint("/oauth2/v1/authorize")
                .tokenEndpoint("/oauth2/v1/token")
                .checkTokenEndpoint("/oauth2/v1/check_token")
                .tokenRevocationEndpoint("/oauth2/v1/revoke")
                .logoutEndpoint("/oauth2/v1/logout")
                .build();
    }
}
