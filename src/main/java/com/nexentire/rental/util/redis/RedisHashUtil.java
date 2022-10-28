package com.nexentire.rental.util.redis;

import java.util.Map;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisHashUtil implements RedisUtil {

	private HashOperations<String, Object, Object> hashOperations;
	
	public RedisHashUtil(RedisTemplate<?, ?> redisTemplate) {
		// TODO Auto-generated constructor stub
		hashOperations = (HashOperations<String, Object, Object>) redisTemplate.opsForHash();
	}
	
	public void saveRedisItem(String hkey, String key, String value) {
		hashOperations.put(hkey, key, value);
	}
	
	public Object getRedisItem(String hkey, String key) {
		Object value = hashOperations.get(hkey, key);
		return value;
	}
	
	public void removeRedisItem(String hKey, String key) {
		hashOperations.delete(key, hKey);
	}
	
	public long getCount(String hkey) {
		Long size = hashOperations.size(hkey);
		return size;
	}
	
	public boolean isKeyContained(String hkey, String key) {
		Map<Object, Object> entries = hashOperations.entries(hkey);
		return entries.keySet().contains(key);			
	}
	
	public boolean isValueContained(String hkey, String value) {
		Map<Object, Object> entries = hashOperations.entries(hkey);
		return entries.values().contains(hkey);			
	}

	@Override
	public void saveRedisItem(String key, Object value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> T getRedisItem(T key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeRedisItem(String key) {
		// TODO Auto-generated method stub
	}

	@Override
	public <T> T getRedisItem(T exportKey, int i, int size) {
		// TODO Auto-generated method stub
		return null;
	}
}
