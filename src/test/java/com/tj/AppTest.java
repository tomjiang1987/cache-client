package com.tj;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

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
        cacheService.delete(key);
        
//        Collection<String> keys = new ArrayList<String>();
//        keys.add("test");
//        keys.add("no-exist");
//        Map<String,String> rs = cacheService.<String>getAll(keys);
//        
//        for( Map.Entry<String,String> entry : rs.entrySet()){
//        	System.out.println(entry.getKey() + " || " + entry.getValue());
//        }
        
        //System.out.println(cacheService.delete(key));
//        
//        System.out.println("--expire key--");
//        CacheService expireCache = (CacheService)context.getBean("expireCache");
//        System.out.println(expireCache.set(key, 0));
//        System.out.println(expireCache.set(key, 0));
//        System.out.println(expireCache.get(key));
//        System.out.println("sleep 2 seconds...");
//        Thread.sleep(2000);
//        System.out.println(expireCache.get(key));
//        
//        System.out.println("sleep 3 seconds...");
//        Thread.sleep(3000);
//        System.out.println(expireCache.get(key));
//        
//        System.out.println(expireCache.delete(key));
        
        context.close();
	}
}
