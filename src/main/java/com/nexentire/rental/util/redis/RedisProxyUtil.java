package com.nexentire.rental.util.redis;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

//생성된 클래스를 bean으로 등록
@Component
public class RedisProxyUtil {

	private static RedisTemplate<?, ?> redisTemplate;
	
    public final static String STRING = "STRING";
    public final static String SET = "SET";
    public final static String LIST = "LIST";
    public final static String HASH = "HASH";
    
	public RedisProxyUtil() {
	}
	
	//static 변수는 메모리의 영역이 다르므로 Autowired 어노테이션을 그냥 사용 불가
	//생성자 주입이나 setter주입을 사용해야함
	@Autowired
	public void setRedisTemplate(RedisTemplate<?, ?> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
	//proxy패턴
	public static Object getInstance(String mode) {
		
		switch(mode) {
			case "STRING":
				return new RedisStringUtil(redisTemplate);
			case "SET":
				return new RedisSetUtil(redisTemplate);
			case "LIST":
				return new RedisListUtil(redisTemplate);
			case "HASH":
				return new RedisHashUtil(redisTemplate);
			default:
				return null;
		}
	}
	
	public static long removeRedisItem(Collection key) {
		return redisTemplate.delete(key);
	}
	
}
