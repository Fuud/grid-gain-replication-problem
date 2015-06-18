package org.sample.bug;

import java.rmi.Remote;

public interface CacheRemote extends Remote {
    void put(Object key, Object value);

    Object get(Object key);
}
