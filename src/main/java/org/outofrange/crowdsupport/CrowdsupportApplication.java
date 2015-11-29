package org.outofrange.crowdsupport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class CrowdsupportApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrowdsupportApplication.class, args);
    }
}
