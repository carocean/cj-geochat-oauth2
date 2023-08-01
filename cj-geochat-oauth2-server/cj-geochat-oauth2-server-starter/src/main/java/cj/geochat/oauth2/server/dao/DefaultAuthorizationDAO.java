package cj.geochat.oauth2.server.dao;

import cj.geochat.ability.oauth.server.OAuth2Authorization;
import cj.geochat.ability.oauth.server.OAuth2AuthorizationCode;
import cj.geochat.ability.oauth.server.OAuth2ParameterNames;
import cj.geochat.ability.oauth.server.OAuth2TokenType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.SerializationUtils;
import org.springframework.util.StringUtils;

public class DefaultAuthorizationDAO implements OAuth2AuthorizationDAO {
    private String keyPrefix = "oauth2:authorizations:completed";
    RedisTemplate<String, byte[]> redisTemplate;

    public DefaultAuthorizationDAO(String keyPrefix, RedisTemplate<String, byte[]> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.keyPrefix = keyPrefix;
    }

    @Override
    public void save(OAuth2Authorization authorization) {
        byte[] data = SerializationUtils.serialize(authorization);
        String idKey = getIdKey(authorization);
        redisTemplate.opsForValue().set(idKey, data);
        indexAuthorization(authorization.getId().getBytes(), authorization);
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        String idKey = getIdKey(authorization);
        redisTemplate.delete(idKey);
        indexDelete(authorization);
    }

    @Override
    public OAuth2Authorization findById(String id) {
        OAuth2Authorization authorization = null;
        String idKey = getIdKey(id);
        if (StringUtils.hasText(idKey)) {
            byte[] data = redisTemplate.opsForValue().get(idKey);
            if (data != null) {
                authorization = (OAuth2Authorization) SerializationUtils.deserialize(data);
            }
        }
        return authorization;
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        if (tokenType == null) {
            OAuth2Authorization authorization = null;
            authorization = findByState(token);
            if (authorization != null) {
                return authorization;
            }
            authorization = findByCode(token);
            if (authorization != null) {
                return authorization;
            }
            authorization = findByAccessToken(token);
            if (authorization != null) {
                return authorization;
            }
            authorization = findByRefreshToken(token);
            if (authorization != null) {
                return authorization;
            }
        } else if (OAuth2ParameterNames.STATE.equals(tokenType.getValue())) {
            return findByState(token);
        } else if (OAuth2ParameterNames.CODE.equals(tokenType.getValue())) {
            return findByCode(token);
        } else if (OAuth2TokenType.ACCESS_TOKEN.equals(tokenType)) {
            return findByAccessToken(token);
        } else if (OAuth2TokenType.REFRESH_TOKEN.equals(tokenType)) {
            return findByRefreshToken(token);
        }
        return null;
    }

    private OAuth2Authorization findByRefreshToken(String token) {
        String key = String.format("%s:refresh_token:%s", keyPrefix, token);
        byte[] idData = redisTemplate.opsForValue().get(key);
        if (idData == null) {
            return null;
        }
        String id = new String(idData);
        if (!StringUtils.hasText(id)) {
            return null;
        }
        return findById(id);
    }

    private OAuth2Authorization findByAccessToken(String token) {
        String key = String.format("%s:access_token:%s", keyPrefix, token);
        byte[] idData = redisTemplate.opsForValue().get(key);
        if (idData == null) {
            return null;
        }
        String id = new String(idData);
        if (!StringUtils.hasText(id)) {
            return null;
        }
        return findById(id);
    }

    private OAuth2Authorization findByCode(String token) {
        String key = String.format("%s:code:%s", keyPrefix, token);
        byte[] idData = redisTemplate.opsForValue().get(key);
        if (idData == null) {
            return null;
        }
        String id = new String(idData);
        if (!StringUtils.hasText(id)) {
            return null;
        }
        return findById(id);
    }

    private OAuth2Authorization findByState(String token) {
        String key = String.format("%s:state:%s", keyPrefix, token);
        byte[] idData = redisTemplate.opsForValue().get(key);
        if (idData == null) {
            return null;
        }
        String idValue = new String(idData);
        if (!StringUtils.hasText(idValue)) {
            return null;
        }
        return findById(idValue);
    }

    private String getIdKey(OAuth2Authorization authorization) {
        return getIdKey(authorization.getId());
    }

    private String getIdKey(String id) {
        id = String.format("%s:id:%s", keyPrefix, id);
        return id;
    }


    //state,code,access_token,refresh_token,四个索引来索引参数id
    private void indexAuthorization(byte[] idValue, OAuth2Authorization authorization) {
        String access_token = authorization.getAccessToken() == null ? null : authorization.getAccessToken().getToken().getTokenValue();
        String refresh_token = authorization.getRefreshToken() == null ? null : authorization.getRefreshToken().getToken().getTokenValue();
        String state = authorization.getAttribute(OAuth2ParameterNames.STATE);
        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode =
                authorization.getToken(OAuth2AuthorizationCode.class);
        String code = "";
        if (authorizationCode != null) {
            code = authorizationCode.getToken().getTokenValue();
        }
        if (StringUtils.hasText(access_token)) {
            String indexKey = String.format("%s:access_token:%s", keyPrefix, access_token);
            redisTemplate.opsForValue().set(indexKey, idValue);
        }
        if (StringUtils.hasText(refresh_token)) {
            String indexKey = String.format("%s:refresh_token:%s", keyPrefix, refresh_token);
            redisTemplate.opsForValue().set(indexKey, idValue);
        }
        if (StringUtils.hasText(state)) {
            String indexKey = String.format("%s:state:%s", keyPrefix, state);
            redisTemplate.opsForValue().set(indexKey, idValue);
        }
        if (StringUtils.hasText(code)) {
            String indexKey = String.format("%s:code:%s", keyPrefix, code);
            redisTemplate.opsForValue().set(indexKey, idValue);
        }
    }

    private void indexDelete(OAuth2Authorization authorization) {
        String access_token = authorization.getAccessToken().getToken().getTokenValue();
        String refresh_token = authorization.getRefreshToken().getToken().getTokenValue();
        String state = authorization.getAttribute(OAuth2ParameterNames.STATE);
        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode =
                authorization.getToken(OAuth2AuthorizationCode.class);
        String code = "";
        if (authorizationCode != null) {
            code = authorizationCode.getToken().getTokenValue();
        }
        if (StringUtils.hasText(access_token)) {
            String indexKey = String.format("%s:access_token:%s", keyPrefix, access_token);
            redisTemplate.delete(indexKey);
        }
        if (StringUtils.hasText(refresh_token)) {
            String indexKey = String.format("%s:refresh_token:%s", keyPrefix, refresh_token);
            redisTemplate.delete(indexKey);
        }
        if (StringUtils.hasText(state)) {
            String indexKey = String.format("%s:state:%s", keyPrefix, state);
            redisTemplate.delete(indexKey);
        }
        if (StringUtils.hasText(code)) {
            String indexKey = String.format("%s:code:%s", keyPrefix, code);
            redisTemplate.delete(indexKey);
        }
    }

}
