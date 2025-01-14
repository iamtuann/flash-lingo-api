package dev.iamtuann.flashlingo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FlashLingoBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlashLingoBeApplication.class, args);
    }

}
