package com.nexentire.rental.util.jandi;

import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class IncommingHookConfig {

	@Value("${jandi.incomming.url}")
	private String sendUrl;
	
	public IncommingHookConfig() {
		// TODO Auto-generated constructor stub
		
	}

	@Bean
	public HttpURLConnection httpUrlConnection() {
		
		URL url = null;
		HttpURLConnection conn = null;
		
		try {
			url = new URL(sendUrl);
			conn = (HttpURLConnection)url.openConnection();
			
		}catch(Exception e) {
			
		}
		
		return conn;
	}
}
