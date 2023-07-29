package cj.geochat.server.authorization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"cj.geochat.server.authorization"})
@EnableDiscoveryClient
public class CjGeochatServerAuthStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(CjGeochatServerAuthStarterApplication.class, args);
    }

}
