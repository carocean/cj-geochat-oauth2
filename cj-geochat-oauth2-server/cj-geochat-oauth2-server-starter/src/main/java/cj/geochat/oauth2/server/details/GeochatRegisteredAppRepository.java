package cj.geochat.oauth2.server.details;

import cj.geochat.ability.oauth.server.RegisteredApp;
import cj.geochat.ability.oauth.server.repository.RegisteredAppRepository;
import org.springframework.stereotype.Service;

//@Service
public class GeochatRegisteredAppRepository implements RegisteredAppRepository {
    @Override
    public void save(RegisteredApp registeredApp) {

    }

    @Override
    public RegisteredApp findById(String id) {
        return null;
    }

    @Override
    public RegisteredApp findByAppId(String appId) {
        return null;
    }
}
