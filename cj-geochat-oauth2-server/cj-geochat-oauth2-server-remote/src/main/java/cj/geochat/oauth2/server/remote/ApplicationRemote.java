package cj.geochat.oauth2.server.remote;

import cj.geochat.uc.middle.model.Application;
import cj.geochat.uc.middle.web.rest.IApplicationRestfull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(contextId = "applicationRemote", value = "cj-geochat-uc-middle", url = "${app.test.feign.adapter.docker.uc.url:}")
public interface ApplicationRemote extends IApplicationRestfull {
    @RequestMapping(value = "/api/v1/application/getApp", method = RequestMethod.GET)
    @Override
    Application getApp(@RequestParam String appId);
}
