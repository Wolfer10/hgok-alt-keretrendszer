package com.hgok.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;


@SpringBootApplication
@EnableAsync
public class DemoApplication {


    public static void main(String[] args) throws IOException {
        SpringApplication.run(DemoApplication.class, args);

    }

}
