package cj.geochat.oauth2.server.remote;

import cj.geochat.uc.middle.LoginAccountCategory;
import cj.geochat.uc.middle.UserDetails;
import cj.geochat.uc.middle.web.rest.IUserDetailsRestfull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(contextId = "userDetailsRemote", value = "cj-geochat-uc-middle", url = "${app.test.feign.adapter.docker.uc.url:}")
public interface UserDetailsRemote extends IUserDetailsRestfull {
    @RequestMapping(value = "/api/v1/user/details/loadUserDetailsByAccount", method = RequestMethod.GET)
    @Override
    UserDetails loadUserDetailsByAccount(@RequestParam LoginAccountCategory category, @RequestParam String openCode);
}
