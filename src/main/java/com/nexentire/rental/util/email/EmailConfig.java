package com.nexentire.rental.util.email;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfig {

	public EmailConfig() {
		// TODO Auto-generated constructor stub
	}

	@Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("172.17.0.100");
        javaMailSender.setPort(25);
        javaMailSender.setUsername("qms");
        javaMailSender.setPassword("qms");
        
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        javaMailSender.setJavaMailProperties(properties);
        
        return javaMailSender;
    }

}
