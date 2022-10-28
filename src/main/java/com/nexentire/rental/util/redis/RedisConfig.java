package com.nexentire.rental.util.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

//클래스태 하나이상의 Bean어노테이션을 사용하기 위해서는 configuration어노테이션 선언 필요
//해당 클래스가 설정파일 또는 자바bean을 만드는 클래스임을 알림
@Configuration
@EnableRedisRepositories
public class RedisConfig {

	@Value("${spring.redis.host}")
	private String host;
	@Value("${spring.redis.port}")
	private int port;
	
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory(host, port);
	}
	
	@Bean
	public RedisTemplate<?, ?> redisTemplate(){
		RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
		return redisTemplate;
	}
}
