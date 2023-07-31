package cj.geochat.oauth2.server.config;

import cj.geochat.ability.oauth.server.AuthorizationGrantType;
import cj.geochat.ability.oauth.server.RegisteredApp;
import cj.geochat.ability.oauth.server.annotation.EnableCjOAuth2Server;
import cj.geochat.ability.oauth.server.repository.InMemoryRegisteredAppRepository;
import cj.geochat.ability.oauth.server.repository.RegisteredAppRepository;
import cj.geochat.ability.oauth.server.settings.AuthorizationServerSettings;
import cj.geochat.ability.oauth.server.user.details.GeochatUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.UUID;
import java.util.regex.Pattern;

//@EnableCjRedis
@EnableCjOAuth2Server
@Configuration
public class OpenOAuth2ServerConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public RegisteredAppRepository registeredAppRepository(PasswordEncoder passwordEncoder) {
//        RegisteredApp registeredApp = RegisteredApp.withId(UUID.randomUUID().toString())
//                .appId("demo-client-id")
//                .appSecret(passwordEncoder.encode("demo-client-secret"))
////                .appSecret("{noop}demo-client-secret")
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//                .redirectUri("http://127.0.0.1:8081/login/oauth2/code/client-id-1")
//                .redirectUri("http://127.0.0.1:8081/login/oauth2/code/client-id-2")
//                .scope("message.read")
//                .scope("message.write")
//                .scope("user_info")
//                .scope("pull_requests")
//                .requireAuthorizationConsent(true)
//                .build();
//
//        return new InMemoryRegisteredAppRepository(registeredApp);
//    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .authorizationEndpoint("/oauth2/v1/authorize")
                .tokenEndpoint("/oauth2/v1/token")
                .checkTokenEndpoint("/oauth2/v1/check_token")
                .tokenRevocationEndpoint("/oauth2/v1/revoke")
                .build();
    }
}
