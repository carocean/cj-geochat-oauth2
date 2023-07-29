package cj.geochat.oauth2.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"cj.geochat.oauth2.server"})
public class ServerOAuth2StarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerOAuth2StarterApplication.class, args);
    }

}
