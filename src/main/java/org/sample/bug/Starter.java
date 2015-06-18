package org.sample.bug;

import org.apache.ignite.IgniteCache;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Starter {

    public static CacheRemote startLocalCluster(){
        System.setProperty("configuration.path", "classpath:local-application.properties");
        return startImpl();
    }

    public static CacheRemote startRemoteCluster(){
        System.setProperty("configuration.path", "classpath:remote-application.properties");
        return startImpl();
    }

    @NotNull
    private static CacheRemote startImpl() {
        System.setProperty("logback.configurationFile", "debug-logback.xml");
        final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        final IgniteCache myCache = (IgniteCache) context.getBean("myCache");
        return new CacheRemote() {
            public void put(Object key, Object value) {
                myCache.put(key, value);
            }

            public Object get(Object key) {
                return myCache.get(key);
            }
        };
    }
}
