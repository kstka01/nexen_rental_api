package com.nexentire.rental.util.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

public class RedisStringUtil implements RedisUtil {

	private ValueOperations<String, String> valueOperations = null;
	
	public RedisStringUtil(RedisTemplate<?, ?> redisTemplate) {
		// TODO Auto-generated constructor stub
		valueOperations = (ValueOperations<String, String>) redisTemplate.opsForValue();
	}
	
	@Override
	public void saveRedisItem(String key, Object value) {
		valueOperations.set(key, value.toString());
	}
	
	@Override
	public <T> T getRedisItem(T key) {
		// TODO Auto-generated method stub
		return (T) valueOperations.get(key);
	}

	@Override
	public void removeRedisItem(String key) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public long getCount(String hkey) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <T> T getRedisItem(T exportKey, int i, int size) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
