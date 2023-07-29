package cj.geochat.oauth2.oapp.test.config;

import cj.geochat.ability.feign.annotation.EnableCjFeign;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableCjFeign
@EnableFeignClients(basePackages = "cj.geochat.oauth2.oapp.test.remote")
@Configuration
public class OpenFeignConfig {
}
