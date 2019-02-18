package com.app;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@SpringBootApplication
public class RestApiServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestApiServerApplication.class, args);
    }

    @Bean
    @Qualifier("javaMailSender")
    public JavaMailSender javaMailSender() {

        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setProtocol("smtp");
        sender.setHost("smtp.gmail.com");
        sender.setPort(587);
        sender.setUsername("igor.opona.87@gmail.com");
        sender.setPassword("springsecurity123");

        Properties properties = new Properties();
        properties.put("mail.smtps.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        sender.setJavaMailProperties(properties);

        return sender;
    }
}
