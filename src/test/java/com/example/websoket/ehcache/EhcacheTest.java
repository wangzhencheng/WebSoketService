package com.example.websoket.ehcache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import org.junit.Test;

public class EhcacheTest {

    @Test
    public void testEhcache() {
        Configuration configuration = new Configuration();

        CacheConfiguration cacheConfiguration = new CacheConfiguration();
        cacheConfiguration.setName("test");
        cacheConfiguration.setMaxEntriesLocalHeap(3);
        cacheConfiguration.setTimeToLiveSeconds(1);
        cacheConfiguration.setTimeToIdleSeconds(1);

        configuration.addCache(cacheConfiguration);

        CacheManager cacheManager = new CacheManager(configuration);
        Cache cache = cacheManager.getCache("test");

        cache.put(new Element("login1", 1));
        cache.put(new Element("login2", 2));
        cache.put(new Element("login3", 3));
        cache.put(new Element("login4", 4));
        cache.put(new Element("login5", 5));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(cache.isExpired(new Element("login1",1)));

        System.out.println(cache.get("login1"));
        System.out.println(cache.get("login2"));
        System.out.println(cache.get("login3"));
        System.out.println(cache.get("login4"));
        System.out.println(cache.get("login5"));

    }

    public static void main(String[] args) {
        long currTime=System.currentTimeMillis();
        for(int i=0;i<100000;i++){
//            System.out.println("i"+i);
        }
        System.out.println(System.currentTimeMillis()-currTime);
    }

}
