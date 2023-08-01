package cj.geochat.oauth2.server.service;

import cj.geochat.ability.oauth.server.OAuth2AuthorizationConsent;
import cj.geochat.ability.oauth.server.service.OAuth2AuthorizationConsentService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
public class RedisOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        String id = getId(authorizationConsent);
        String json = new Gson().toJson(authorizationConsent);
        redisTemplate.opsForValue().set(id, json);
    }

    @Override
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        String id = getId(authorizationConsent);
        redisTemplate.delete(id);
    }

    @Override
    public OAuth2AuthorizationConsent findById(String registeredAppId, String principalName) {
        Assert.hasText(registeredAppId, "registeredClientId cannot be empty");
        Assert.hasText(principalName, "principalName cannot be empty");
        String id = getId(registeredAppId, principalName);
        String json = redisTemplate.opsForValue().get(id);
        OAuth2AuthorizationConsent consent = createConsent(json);
        return consent;
    }

    private OAuth2AuthorizationConsent createConsent(String json)  {
        if (!StringUtils.hasText(json)) {
            return null;
        }
        JsonObject obj = new Gson().fromJson(json, JsonObject.class);
        String registeredAppId = obj.get("registeredAppId").getAsString();
        String principalName = obj.get("principalName").getAsString();
        var builder = OAuth2AuthorizationConsent.withId(registeredAppId, principalName);
        JsonArray authoritiesArr = obj.getAsJsonArray("authorities");
        if (authoritiesArr != null) {
            Set<GrantedAuthority> authorities = new HashSet<>();
            for (JsonElement e : authoritiesArr) {
                authorities.add(new SimpleGrantedAuthority(e.getAsString()));
            }
            builder.authorities(authorities::addAll);
        }
        return builder.build();
    }

    private static String getId(String registeredClientId, String principalName) {
        String id = Objects.hash(registeredClientId, principalName) + "";
        id = String.format("oauth2:consent:%s", id);
        return id;
    }

    private static String getId(OAuth2AuthorizationConsent authorizationConsent) {
        return getId(authorizationConsent.getRegisteredAppId(), authorizationConsent.getPrincipalName());
    }
}
