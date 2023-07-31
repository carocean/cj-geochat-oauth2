package cj.geochat.oauth2.server.remote;

import cj.geochat.ability.util.GeochatException;
import cj.geochat.uc.middle.AppDetails;
import cj.geochat.uc.middle.web.rest.IAppDetailsRestfull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(contextId = "appDetailsRemote", value = "cj-geochat-uc-middle", url = "${app.test.feign.adapter.docker.uc.url:}")
public interface AppDetailsRemote extends IAppDetailsRestfull {
    @RequestMapping(value = "/api/v1/app/details/loadAppByAppKey", method = RequestMethod.GET)
    @Override
    AppDetails loadAppByAppKey(@RequestParam String appKey) throws GeochatException;
}
