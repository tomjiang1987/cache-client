package com.tj;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tj.cache.CacheService;


public class AppTest {
	public static void main(String[] args)throws Exception {
		String config = "app-test.xml";
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(config);
        context.start();
        System.out.println("spring inited");
        
        CacheService cacheService = (CacheService)context.getBean("cacheService");
        
        
        String key = "test";
        cacheService.set(key, 0);
        System.out.println(cacheService.get(key));
        
        System.out.println(cacheService.delete(key));
        
        System.out.println("--expire key--");
        CacheService expireCache = (CacheService)context.getBean("expireCache");
        System.out.println(expireCache.set(key, 0));
        System.out.println(expireCache.set(key, 0));
        System.out.println(expireCache.get(key));
        System.out.println("sleep 2 seconds...");
        Thread.sleep(2000);
        System.out.println(expireCache.get(key));
        
        System.out.println("sleep 3 seconds...");
        Thread.sleep(3000);
        System.out.println(expireCache.get(key));
        
        System.out.println(expireCache.delete(key));
        
        context.close();
	}
}
