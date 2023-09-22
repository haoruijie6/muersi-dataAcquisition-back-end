package com.data_conllection.common.data_collection_thread.cache;

import org.apache.commons.lang3.ObjectUtils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据采集线程缓存
 */
public class DataCollectionThreadCache {

    /**
     * 每个采集间隔对应一个采集线程
     * key采集时间
     */
    public volatile static ConcurrentHashMap<Long, Thread> collectionThreadCache = new ConcurrentHashMap<>();

    /**
     * 设置线程缓存
     * @param intervalTime 采集间隔
     * @param runnable 线程
     */
    public static void putCollectionThreadCache(Long intervalTime, Thread runnable){
        collectionThreadCache.put(intervalTime, runnable);
    }

    /**
     * 获取线程缓存
     */
    public static Thread getCollectionThreadCache(Long intervalTime){
        return collectionThreadCache.get(intervalTime);
    }

    /**
     * 删除线程缓存
     */
    public static void removeCollectionThreadCache(Long intervalTime){
        //停止线程
        if (collectionThreadCache.containsKey(intervalTime)){
            Thread thread = getCollectionThreadCache(intervalTime);
            if (ObjectUtils.isNotEmpty(thread)){
                thread.stop();
            }
        }
        collectionThreadCache.remove(intervalTime);
    }

}
