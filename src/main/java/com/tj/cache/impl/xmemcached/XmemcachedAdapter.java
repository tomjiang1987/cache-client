package com.tj.cache.impl.xmemcached;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientCallable;
import net.rubyeye.xmemcached.exception.MemcachedException;

import com.tj.cache.CacheException;
import com.tj.cache.CacheService;

public class XmemcachedAdapter implements CacheService {
	private int expireTime = 0;
	private String ns = "default";
	private MemcachedClient memcachedClient;

	public XmemcachedAdapter(MemcachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
	}

	@Override
	public <T> T get(final String key) throws InterruptedException, TimeoutException,CacheException {
		try{
			return memcachedClient.withNamespace(ns, new MemcachedClientCallable<T>(){
				public T call(MemcachedClient client) throws MemcachedException,InterruptedException, TimeoutException{
				      	return client.get(key);
				}
			});
		}catch(MemcachedException memcachedEx){
			throw new CacheException(memcachedEx);
		}
		
	}

	@Override
	public <T> Boolean set(final String key, final T value) throws InterruptedException, TimeoutException,CacheException{
		try{
			return memcachedClient.withNamespace(ns, new MemcachedClientCallable<Boolean>(){
				public Boolean call(MemcachedClient client) throws MemcachedException,InterruptedException, TimeoutException{
				      	return client.set(key, expireTime, value);
				}
			});
		}catch(MemcachedException memcachedEx){
			throw new CacheException(memcachedEx);
		}
	}

	@Override
	public Boolean delete(final String key) throws InterruptedException, TimeoutException,CacheException{
		try{
			return memcachedClient.withNamespace(ns, new MemcachedClientCallable<Boolean>(){
				public Boolean call(MemcachedClient client) throws MemcachedException,InterruptedException, TimeoutException{
				      	return client.delete(key);
				}
			});
		}catch(MemcachedException memcachedEx){
			throw new CacheException(memcachedEx);
		}
	}

	public void shutdown() throws IOException {
		if (this.memcachedClient != null) {
			this.memcachedClient.shutdown();
		}
	}

	public int getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(int expireTime) {
		this.expireTime = expireTime;
	}

	public String getNs() {
		return ns;
	}

	public void setNs(String ns) {
		this.ns = ns;
	}

	public MemcachedClient getMemcachedClient() {
		return memcachedClient;
	}

	public void setMemcachedClient(MemcachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
	}

}
