package cj.geochat.oauth2.server.validity;

import cj.geochat.ability.oauth.server.redis.TokenValidity;
import cj.geochat.oauth2.server.remote.ApplicationRemote;
import cj.geochat.uc.middle.model.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultTokenValidity implements TokenValidity {
    @Autowired
    ApplicationRemote applicationRemote;

    @Override
    public long getAccessTokenValidity(String registeredAppId) {
        Application application = applicationRemote.getApp(registeredAppId);
        return application == null ? 30 * 24 * 60 * 60 : application.getAccessTokenValidity();
    }

    @Override
    public long getRefreshTokenValidity(String registeredAppId) {
        Application application = applicationRemote.getApp(registeredAppId);
        return application == null ? 30 * 24 * 60 * 60 : application.getRefreshTokenValidity();
    }

    @Override
    public long getAuthCodeTokenValidity(String registeredAppId) {
        Application application = applicationRemote.getApp(registeredAppId);
        return application == null ? 600 : application.getAuthCodeValidity();
    }
}
