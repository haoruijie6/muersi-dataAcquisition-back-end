package com.data_conllection.common.modbus.tcp.cache;

import com.data_conllection.domain.po.EquipmentCollectionGroupPointPo;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * modbusTcp采集点位缓存
 */
public class ModbusTcpCollectionPointsCache {

    /**
     * 每个采集间隔对应一组采集线程
     * key采集时间
     */
    public volatile static ConcurrentHashMap<Long, List<EquipmentCollectionGroupPointPo>> collectionThreadCache = new ConcurrentHashMap<>();

    /**
     * 设置线程缓存
     * @param intervalTime 采集间隔
     * @param list 采集点位
     */
    public static void putCollectionThreadCache(Long intervalTime, List<EquipmentCollectionGroupPointPo> list){
        collectionThreadCache.put(intervalTime, list);
    }

    /**
     * 获取线程缓存
     */
    public static List<EquipmentCollectionGroupPointPo> getCollectionThreadCache(Long intervalTime){
        return collectionThreadCache.get(intervalTime);
    }

    /**
     * 删除线程缓存
     */
    public static void removeCollectionThreadCache(Long intervalTime){
        collectionThreadCache.remove(intervalTime);
    }

}
