package com.tj.cache.impl.xmemcached;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import com.tj.cache.CacheException;
import com.tj.cache.CacheService;

public class XmemcachedAdapter implements CacheService {
	private int expireTime = 0;
	private String ns = "";
	private MemcachedClient memcachedClient;

	public XmemcachedAdapter(MemcachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
	}

	@Override
	public <T> T get(final String key) throws InterruptedException, TimeoutException,CacheException {
		try{
			return memcachedClient.get(this.addNs(key));
		}catch(MemcachedException memcachedEx){
			throw new CacheException(memcachedEx);
		}
	}
	
	@Override
	public <T> Map<String, T> getAll(final Collection<String> keyCollections)
			throws InterruptedException, TimeoutException, CacheException {
		try{
			return memcachedClient.get(this.addNs(keyCollections));
		}catch(MemcachedException memcachedEx){
			throw new CacheException(memcachedEx);
		}
	}

	@Override
	public <T> Boolean set(final String key, final T value) throws InterruptedException, TimeoutException,CacheException{
		try{
			return memcachedClient.set(this.addNs(key), expireTime, value);
		}catch(MemcachedException memcachedEx){
			throw new CacheException(memcachedEx);
		}
	}

	@Override
	public Boolean delete(final String key) throws InterruptedException, TimeoutException,CacheException{
		try{
			return memcachedClient.delete(this.addNs(key));
		}catch(MemcachedException memcachedEx){
			throw new CacheException(memcachedEx);
		}
	}

	

	private String addNs(String key){
		if(key == null){
			throw new IllegalArgumentException("key is null");
		}
		
		return "/" + ns + ":" + key;
	}
	
	private Collection<String> addNs(final Collection<String> keyCollections){
		Collection<String> rs = new ArrayList<String>();
		for(String key: keyCollections){
			rs.add(this.addNs(key));
		}
		return rs;
	}
	
	public void shutdown() throws IOException {
		if (this.memcachedClient != null) {
			this.memcachedClient.shutdown();
		}
	}
	
	//getters and setters
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
