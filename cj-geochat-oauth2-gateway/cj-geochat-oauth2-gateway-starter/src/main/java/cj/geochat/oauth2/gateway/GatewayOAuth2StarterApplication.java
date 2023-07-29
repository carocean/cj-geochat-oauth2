package cj.geochat.oauth2.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"cj.geochat.oauth2.gateway"})
public class GatewayOAuth2StarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayOAuth2StarterApplication.class, args);
    }

}
