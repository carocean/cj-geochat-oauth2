package cj.geochat.oauth2.server.service;

import cj.geochat.ability.oauth.server.OAuth2Authorization;
import cj.geochat.ability.oauth.server.OAuth2TokenType;
import cj.geochat.ability.oauth.server.service.OAuth2AuthorizationService;
import cj.geochat.oauth2.server.dao.DefaultAuthorizationDAO;
import cj.geochat.oauth2.server.dao.OAuth2AuthorizationDAO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService, InitializingBean {

    OAuth2AuthorizationDAO completedAuthorizationDAO;
    OAuth2AuthorizationDAO initializedAuthorizationDAO;
    @Autowired
    @Qualifier("bytesRedisTemplate")
    RedisTemplate<String, byte[]> redisTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
        completedAuthorizationDAO = new DefaultAuthorizationDAO("oauth2:authorizations:completed", redisTemplate);
        initializedAuthorizationDAO = new DefaultAuthorizationDAO("oauth2:authorizations:initialized", redisTemplate);
    }

    @Override
    public void save(OAuth2Authorization authorization) {
        if (isComplete(authorization)) {
            completedAuthorizationDAO.save(authorization);
        } else {
            initializedAuthorizationDAO.save(authorization);
        }
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        if (isComplete(authorization)) {
            completedAuthorizationDAO.remove(authorization);
        } else {
            initializedAuthorizationDAO.remove(authorization);
        }
    }

    @Override
    public OAuth2Authorization findById(String id) {
        OAuth2Authorization authorization = completedAuthorizationDAO.findById(id);
        if (authorization != null) {
            return authorization;
        }
        return initializedAuthorizationDAO.findById(id);
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        OAuth2Authorization authorization = completedAuthorizationDAO.findByToken(token, tokenType);
        if (authorization != null) {
            return authorization;
        }
        return initializedAuthorizationDAO.findByToken(token, tokenType);
    }

    private static boolean isComplete(OAuth2Authorization authorization) {
        return authorization.getAccessToken() != null;
    }
}
