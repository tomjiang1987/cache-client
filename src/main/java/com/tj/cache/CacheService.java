package com.tj.cache;

import java.util.concurrent.TimeoutException;

public interface CacheService {

	public <T> T get(String key) throws InterruptedException, TimeoutException,CacheException;
	
	public <T> Boolean set(String key,T value) throws InterruptedException, TimeoutException,CacheException;
	
	public Boolean delete(String key) throws InterruptedException, TimeoutException,CacheException;
	
}
