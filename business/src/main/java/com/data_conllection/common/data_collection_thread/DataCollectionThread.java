package com.data_conllection.common.data_collection_thread;


import com.common.utils.spring.SpringUtils;
import com.data_conllection.common.modbus.tcp.cache.ModbusTcpCollectionPointsCache;
import com.data_conllection.common.modbus.tcp.collection_method.ModbusTcpCollectMethod;
import com.data_conllection.domain.po.EquipmentCollectionGroupPointPo;

import java.util.List;

/**
 * 采集线程
 * @author haoruijie
 */
public class DataCollectionThread extends Thread implements Runnable{

    private Long timeInterval;

    private DataCollectionThread(){}

    private ModbusTcpCollectMethod modbusTcpCollectMethod = SpringUtils.getBean("ModbusTcpCollectMethod");

    /**
     * 初始化线程
     * @param time 采集时间间隔
     */
    public DataCollectionThread(Long time){
        this.timeInterval = time;
    }

    /**
     * 重写线程方法
     */
    @Override
    public void run(){
        try {
            this.startMaster();
        } catch (InterruptedException e) {
            throw new RuntimeException("调用"+this.timeInterval+"ms线程错误!");
        }
    }

    /**
     * 开启采集
     * @throws InterruptedException
     */
    private void startMaster() throws InterruptedException {
        //调用静态线程方法组
        while (true){
            //获取到所有采集对象
            List<EquipmentCollectionGroupPointPo> threadMethodsByTime = ModbusTcpCollectionPointsCache.getCollectionThreadCache(this.timeInterval);
            if(threadMethodsByTime != null && threadMethodsByTime.size() > 0){
                //依次执行采集方法
                modbusTcpCollectMethod.startMaster(threadMethodsByTime);
            }
            //设置这一组采集点的采集间隔
            Thread.sleep(this.timeInterval);
        }
    }
}
