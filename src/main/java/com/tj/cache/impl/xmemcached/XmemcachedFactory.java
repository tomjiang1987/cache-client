package com.tj.cache.impl.xmemcached;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator;
import net.rubyeye.xmemcached.utils.AddrUtil;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import com.tj.cache.CacheService;

public class XmemcachedFactory implements FactoryBean<CacheService>, DisposableBean, InitializingBean {
	private XmemcachedAdapter memcachedAdapter;
	private String servers;
	private String namespace;
	private int expireTime;//in second
	private List<Integer> weights;
	private int connectionPoolSize = MemcachedClient.DEFAULT_CONNECTION_POOL_SIZE;

	Map<InetSocketAddress, InetSocketAddress> serverMap;
	private long opTimeout = MemcachedClient.DEFAULT_OP_TIMEOUT;
	
	public XmemcachedFactory() {}
	
	@Override
	public CacheService getObject() throws Exception {
		int[] weightsArray = this.getWeightsArray(serverMap);
		MemcachedClientBuilder builder = this.newBuilder(serverMap,weightsArray);
		builder.setSessionLocator(new KetamaMemcachedSessionLocator());
		builder.setConnectionPoolSize(connectionPoolSize);
		
		MemcachedClient memcachedClient = builder.build();
		memcachedClient.setOpTimeout(opTimeout);
		memcachedAdapter = new XmemcachedAdapter(memcachedClient);
		memcachedAdapter.setNs(namespace);
		memcachedAdapter.setExpireTime(expireTime);
		return memcachedAdapter;
	}

	@Override
	public Class<CacheService> getObjectType() {
		return CacheService.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void destroy() throws Exception {
		if (memcachedAdapter != null) {
			memcachedAdapter.shutdown();
		}
	}

	private MemcachedClientBuilder newBuilder(Map<InetSocketAddress, InetSocketAddress> serverMap,int[] weightsArray) {
		MemcachedClientBuilder builder;
		if (weightsArray == null) {
			builder = new XMemcachedClientBuilder(serverMap);
		} else {
			builder = new XMemcachedClientBuilder(serverMap, weightsArray);
		}
		return builder;
	}
	
	private int[] getWeightsArray(Map<InetSocketAddress, InetSocketAddress> serverMap) {
		int[] weightsArray = null;
		if (serverMap != null && serverMap.size() > 0 && this.weights != null) {
			if (this.weights.size() < serverMap.size()) {
				throw new IllegalArgumentException(
						"Weight list's size is less than server list's size");
			}
			weightsArray = new int[this.weights.size()];
			for (int i = 0; i < weightsArray.length; i++) {
				weightsArray[i] = this.weights.get(i);
			}
		}
		return weightsArray;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		// validate parameter
		if (this.servers != null && this.servers.length() > 0) {
			serverMap = AddrUtil.getAddressMap(this.servers);
		}else{
			throw new IllegalArgumentException("cache's servers is blank");
		}
		
		if(namespace == null || namespace.length()<=0){
			throw new IllegalArgumentException("cache's namespace is blank");
		}
		
		if (connectionPoolSize <= 0) {
			throw new IllegalArgumentException("poolSize<=0");
		}
		
		if (expireTime < 0) {
			throw new IllegalArgumentException("expireTime < 0");
		}
	}

	public String getServers() {
		return this.servers;
	}

	public void setServers(String servers) {
		this.servers = servers;
	}
	
	public List<Integer> getWeights() {
		return weights;
	}

	public void setWeights(List<Integer> weights) {
		this.weights = weights;
	}

	public int getConnectionPoolSize() {
		return connectionPoolSize;
	}

	public void setConnectionPoolSize(int connectionPoolSize) {
		this.connectionPoolSize = connectionPoolSize;
	}
	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public int getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(int expireTime) {
		this.expireTime = expireTime;
	}

}
