package com.nexentire.rental.util.redis;

public interface RedisUtil {

	public void saveRedisItem(String key, Object value);
	public void removeRedisItem(String key);
	public <T> T getRedisItem(T key);
	public <T> T getRedisItem(T exportKey, int i, int size);
	public long getCount(String hkey);
	
}
