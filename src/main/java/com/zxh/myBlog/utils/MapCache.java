package com.zxh.myBlog.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cache function with map
 * @author xzou
 *
 */
public class MapCache {
	/**
	 * default size is 1024;
	 */
	
	private static final int Default_Caches = 1024;
	
	private static final MapCache INS = new MapCache();
	
	public static MapCache single() {
		return INS;
	}
	
	static class CacheObject {
        private String key;
        private Object value;
        private long expired;

        public CacheObject(String key, Object value, long expired) {
            this.key = key;
            this.value = value;
            this.expired = expired;
        }

        public String getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        public long getExpired() {
            return expired;
        }
    }
	
	/*
	 * Container for the cache
	 */
	private Map<String, CacheObject> cachePool;
	
	public MapCache() {
        this(Default_Caches);
    }

    public MapCache(int cacheCount) {
    	 cachePool = new ConcurrentHashMap<String, CacheObject>(cacheCount);
    }
    
    /**
     * Get one cache value
     * @param key
     * @return
     */
    public <T> T get(String key) {
    	CacheObject cacheObject = cachePool.get(key);
    	if(cacheObject!=null) {
    		long cur = System.currentTimeMillis()/1000;
    		if(cacheObject.getExpired()<=0 || cacheObject.getExpired()>cur) {
    			Object result = cacheObject.getValue();
    			return (T) result;
    		}
    		if (cur > cacheObject.getExpired()) {
                cachePool.remove(key);
            }
    	}
    	return null;
    }
    
    public void set(String key, Object value) {
    	this.set(key, value, -1);
    }
    
    public void set(String key, Object value, long expired) {
    	expired = expired>0?System.currentTimeMillis()/1000+expired:expired;
    	
    	if(cachePool.size()>800) {
    		cachePool.clear();
    	}
    	
    	CacheObject cacheobject = new CacheObject(key, value, expired);
    	cachePool.put(key, cacheobject);
    }
}
