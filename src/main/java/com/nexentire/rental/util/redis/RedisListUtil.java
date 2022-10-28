/**
 * 
 */
package com.nexentire.rental.util.redis;

import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author Nexen
 *
 */
public class RedisListUtil implements RedisUtil {

	private ListOperations<String, Object> listOperations;
	/**
	 * 
	 */
	public RedisListUtil(RedisTemplate<?, ?> redisTemplate) {
		// TODO Auto-generated constructor stub
		listOperations = (ListOperations<String, Object>) redisTemplate.opsForList();
	}

	@Override
	public void saveRedisItem(String key, Object value) {
		// TODO Auto-generated method stub
		listOperations.rightPush(key, value);
	}
	
	@Override
	public <T> T getRedisItem(T key) {
		// TODO Auto-generated method stub
		List members = listOperations.range(key.toString(), 0, listOperations.size((String)key));
		Object[] items = members.toArray();
		return (T) items;
	}
	
	public <T> T getRedisItem(T key, int startIndex, int endIndex) {
		// TODO Auto-generated method stub
		List members = listOperations.range(key.toString(), startIndex, endIndex);
		Object[] items = members.toArray();
		return (T) items;
	}
	
	public long removeRedisItem(String key, int index, Object value) {
		// TODO Auto-generated method stub
		return listOperations.remove(key, index, value);
	}
	
	@Override
	public void removeRedisItem(String key) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public long getCount(String key) {
		Long size = listOperations.size(key);
		return size;
	}
	
	public boolean isContained(String key, Object value) {
		List members = listOperations.range(key.toString(), 0, listOperations.size((String)key));
		return members.contains(value);			
	}

}
