package cj.geochat.oauth2.server.details;

import cj.geochat.ability.oauth.server.AuthorizationGrantType;
import cj.geochat.ability.oauth.server.NoSuchAppException;
import cj.geochat.ability.oauth.server.RegisteredApp;
import cj.geochat.ability.oauth.server.repository.RegisteredAppRepository;
import cj.geochat.ability.util.GeochatException;
import cj.geochat.ability.util.GeochatRuntimeException;
import cj.geochat.oauth2.server.remote.AppDetailsRemote;
import cj.geochat.uc.middle.AppDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GeochatRegisteredAppRepository implements RegisteredAppRepository {
    @Autowired
    AppDetailsRemote appDetailsRemote;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public RegisteredApp findByAppId(String appId) {
        AppDetails details = null;
        try {
            details = appDetailsRemote.loadAppByAppKey(appId);
        } catch (GeochatException e) {
            log.error(String.format("%s %s", e.getCode(), e.getMessage()));
            throw new GeochatRuntimeException(e.getCode(), e.getMessage());
        }
        if (details == null) {
            throw new NoSuchAppException(appId);
        }
        try {
            return createAppDetails(details);
        } catch (JsonProcessingException | ParseException e) {
            throw new GeochatRuntimeException("5000", e.getMessage());
        }
    }

    private RegisteredApp createAppDetails(AppDetails details) throws JsonProcessingException, ParseException {
        return RegisteredApp.withId(details.getId())
                .appId(details.getAppKey())
                .appIdIssuedAt(new SimpleDateFormat("yyyyMMddHHmmss").parse(details.getAppKeyIssuedAt()).toInstant())
                .appSecret(passwordEncoder.encode(details.getAppSecret()))
                .appSecretExpiresAt(new SimpleDateFormat("yyyyMMddHHmmss").parse(details.getAppSecretIssuedAt()).toInstant())
                .appName(details.getAppName())
                .requireAuthorizationConsent(details.isAutoapprove())
                .reuseRefreshTokens(details.isReuseRefreshTokens())
                .authorizationCodeTimeToLive(Duration.ofSeconds(details.getAuthCodeValidity()>0?details.getAuthCodeValidity():2592000L))
                .authorizationAccessTokenTimeToLive(Duration.ofSeconds(details.getAccessTokenValidity()>0?details.getAccessTokenValidity():600L))
                .authorizationRefreshTokenTimeToLive(Duration.ofSeconds(details.getRefreshTokenValidity()>0?details.getRefreshTokenValidity():2592000L))
                .authorizationGrantTypes(item -> details.getGrantTypes().stream().map(gt -> new AuthorizationGrantType(gt)))
                .redirectUris(e -> details.getRedirectUris())
                .scopes(e -> details.getScopes())
                .build();
    }
}
