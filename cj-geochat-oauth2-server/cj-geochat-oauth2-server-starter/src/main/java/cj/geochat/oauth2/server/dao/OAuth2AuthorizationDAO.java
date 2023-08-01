package cj.geochat.oauth2.server.dao;

import cj.geochat.ability.oauth.server.OAuth2Authorization;
import cj.geochat.ability.oauth.server.OAuth2TokenType;
import org.springframework.lang.Nullable;

public interface OAuth2AuthorizationDAO {
    void save(OAuth2Authorization authorization);

    void remove(OAuth2Authorization authorization);

    @Nullable
    OAuth2Authorization findById(String id);

    @Nullable
    OAuth2Authorization findByToken(String token, @Nullable OAuth2TokenType tokenType);
}
