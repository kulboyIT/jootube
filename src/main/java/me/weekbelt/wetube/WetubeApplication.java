package me.weekbelt.wetube;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class WetubeApplication {

    public static void main(String[] args) {
        SpringApplication.run(WetubeApplication.class, args);
    }

}
