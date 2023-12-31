package cj.geochat.test.iapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"cj.geochat.test.iapp"})
public class IAppTestStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(IAppTestStarterApplication.class, args);
    }

}
