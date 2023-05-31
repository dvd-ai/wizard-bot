package com.learngerman.wizardbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WizardBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(WizardBotApplication.class, args);
    }

}
