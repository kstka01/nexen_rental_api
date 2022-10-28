package com.nexentire.rental.util.redis;

import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;

public class RedisSetUtil implements RedisUtil {

	private SetOperations<String, Object> setOperations;
	
	public RedisSetUtil(RedisTemplate<?, ?> redisTemplate) {
		// TODO Auto-generated constructor stub
		setOperations = (SetOperations<String, Object>) redisTemplate.opsForSet();
	}
	
	@Override
	public void saveRedisItem(String key, Object value) {
		setOperations.add(key, value);
	}
	
	@Override
	public <T> T getRedisItem(T key) {
		// TODO Auto-generated method stub
		Set<Object> members = setOperations.members(key.toString());
		Object[] items = members.toArray();
		return (T) items;
	}
	
	@Override
	public void removeRedisItem(String key) {
		// TODO Auto-generated method stub
		setOperations.remove(key, null);
	}
	
	@Override
	public long getCount(String key) {
		Long size = setOperations.size(key);
		return size;
	}
	
	public boolean isContained(String key, Object value) {
		Set<Object> members = setOperations.members(key);
		return members.contains(value);			
	}

	@Override
	public <T> T getRedisItem(T exportKey, int i, int size) {
		// TODO Auto-generated method stub
		return null;
	}

}
