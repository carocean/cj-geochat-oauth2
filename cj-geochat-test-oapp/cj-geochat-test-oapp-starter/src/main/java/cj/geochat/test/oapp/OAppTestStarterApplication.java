package cj.geochat.test.oapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"cj.geochat.test.oapp"})
public class OAppTestStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(OAppTestStarterApplication.class, args);
    }

}
