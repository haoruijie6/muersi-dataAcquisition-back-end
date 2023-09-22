package com.data_conllection.common.modbus.tcp.cache;

import com.data_conllection.domain.po.EquipmentCollectionGroupPo;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;

import java.util.concurrent.ConcurrentHashMap;

public class ModbusTcpConnectCache {

    /**
     * 每台设备下每组对应的一个通道
     * key组id
     */
    public volatile static ConcurrentHashMap<Long, ModbusMaster> modbusTcpCache = new ConcurrentHashMap<>();

    /**
     * 设置ModbusMaster缓存
     */
    public static void putModbusTcpCache(EquipmentCollectionGroupPo groupPo, ModbusMaster modbusMaster){
        putModbusTcpCache(groupPo.getId(), modbusMaster);
    }

    /**
     * 设置缓存
     * @param id 采集组id
     * @param modbusMaster 连接
     */
    public static void putModbusTcpCache(Long id, ModbusMaster modbusMaster){
        modbusTcpCache.put(id, modbusMaster);
    }

    /**
     * 获取连接缓存
     */
    public static ModbusMaster getModbusTcpCache(EquipmentCollectionGroupPo groupPo){
        return getModbusTcpCache(groupPo.getId());
    }

    /**
     * 获取连接缓存
     */
    public static ModbusMaster getModbusTcpCache(Long id){
        return modbusTcpCache.get(id);
    }

    /**
     * 删除连接缓存
     */
    public static void removeModbusTcpCache(EquipmentCollectionGroupPo groupPo){
        removeModbusTcpCache(groupPo.getId());
    }

    /**
     * 删除连接缓存
     */
    public static void removeModbusTcpCache(Long id){
         modbusTcpCache.remove(id);
    }

}
