package cj.geochat.oauth2.oapp.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"cj.geochat.oauth2.oapp.test"})
public class OAppTestStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(OAppTestStarterApplication.class, args);
    }

}
