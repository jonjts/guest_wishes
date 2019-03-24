package com.jonjts.guestwishes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@SpringBootApplication
@EnableJpaAuditing
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }


    @Bean
    public Logger myLogger() {
        return Logger.getGlobal();
    }

}
