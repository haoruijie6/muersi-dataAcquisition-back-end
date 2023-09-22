package com.data_conllection;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.common.utils.DateUtils;
import com.data_conllection.common.data_collection_thread.DataCollectionThread;
import com.data_conllection.common.data_collection_thread.cache.DataCollectionThreadCache;
import com.data_conllection.common.modbus.tcp.ModbusTcpBuilder;
import com.data_conllection.common.modbus.tcp.cache.ModbusTcpCollectionPointsCache;
import com.data_conllection.common.modbus.tcp.cache.ModbusTcpConnectCache;
import com.data_conllection.common.modbus.tcp.collection_method.ModbusTcpCollectMethod;
import com.data_conllection.domain.po.CollectionLogPo;
import com.data_conllection.domain.po.EquipmentCollectionGroupPo;
import com.data_conllection.domain.po.EquipmentCollectionGroupPointPo;
import com.data_conllection.service.CollectionLogService;
import com.data_conllection.service.EquipmentCollectionGroupPointService;
import com.data_conllection.service.EquipmentCollectionGroupService;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 初始化采集状态
 */
@Component
@Order(1)
public class InitialCollection implements ApplicationRunner {

    //采集组业务类
    @Autowired
    private EquipmentCollectionGroupService equipmentCollectionGroupService;

    @Autowired
    private EquipmentCollectionGroupPointService equipmentCollectionGroupPointService;

    //采集日志业务类
    @Autowired
    private CollectionLogService collectionLogService;

    @Autowired
    private ModbusTcpCollectMethod modbusTcpCollectMethod;
    /**
     * 初始化采集状态
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(ApplicationArguments args) throws Exception {
        //查询正在连接的采集组
        List<EquipmentCollectionGroupPo> list = equipmentCollectionGroupService.list(
                new LambdaQueryWrapper<EquipmentCollectionGroupPo>()
                        .eq(EquipmentCollectionGroupPo::getConnectionStatus, 1)
        );
        //连接所有设备
        list.forEach(l -> {
            //测试连接是否正常
            ModbusMaster modbusTcp = ModbusTcpBuilder.getModbusTcp(l.getIp(), l.getPort());
            if (modbusTcp.isConnected()) {
                //放入缓存
                ModbusTcpConnectCache.putModbusTcpCache(l, modbusTcp);
            } else {
                //修改状态
                equipmentCollectionGroupService.update(
                        new LambdaUpdateWrapper<EquipmentCollectionGroupPo>()
                                .set(EquipmentCollectionGroupPo::getConnectionStatus, 0)
                                .eq(EquipmentCollectionGroupPo::getId, l.getId())
                );
                //保存日志
                CollectionLogPo collectionLogPo = new CollectionLogPo(l.getId(), "启动项目连接异常，系统自动修改组连接状态", DateUtils.getNowDate());
                collectionLogService.save(collectionLogPo);
                //将组下的采集点状态都改为未采集
                List<EquipmentCollectionGroupPointPo> poList = equipmentCollectionGroupPointService.list(
                        new LambdaQueryWrapper<EquipmentCollectionGroupPointPo>()
                                .eq(EquipmentCollectionGroupPointPo::getGroupId,l.getId())
                                .eq(EquipmentCollectionGroupPointPo::getCollectionStatus, 1)
                );
                poList.forEach(p->{
                    p.setCollectionStatus(0);
                });
                equipmentCollectionGroupPointService.updateBatchById(poList);
            }
        });
        //---------------
        //分组查询所有采集点的时间间隔
        List<Long> pointPos = equipmentCollectionGroupPointService.getIntervalTimes();
        //创建线程
        pointPos.forEach(p -> {
            //创建线程
            DataCollectionThread dataCollectionThread = new DataCollectionThread(p);
            //开启线程
            dataCollectionThread.start();
            //放入缓存
            DataCollectionThreadCache.putCollectionThreadCache(p, dataCollectionThread);
        });
        //---------------
        //将正在采集的点放到缓存中
        List<EquipmentCollectionGroupPointPo> poList = equipmentCollectionGroupPointService.list(
                new LambdaQueryWrapper<EquipmentCollectionGroupPointPo>()
                        .eq(EquipmentCollectionGroupPointPo::getCollectionStatus, 1)
        );
        poList.forEach(p -> {
            //判断是否有相同采集间隔的采集点
            if (ModbusTcpCollectionPointsCache.collectionThreadCache.containsKey(p.getIntervalTime())) {
                //将采集点放入采集缓存中
                List<EquipmentCollectionGroupPointPo> pointPosCache = ModbusTcpCollectionPointsCache.collectionThreadCache.get(p.getIntervalTime());
                //判断
                if (ObjectUtils.isNotEmpty(pointPosCache) && pointPosCache.size() != 0) {
                    pointPosCache.add(p);
                }
            } else {
                //存放一组采集点
                List<EquipmentCollectionGroupPointPo> newPointPos = new ArrayList<>();
                newPointPos.add(p);
                ModbusTcpCollectionPointsCache.putCollectionThreadCache(p.getIntervalTime(), newPointPos);
            }
        });
        //立即执行一次采集
        modbusTcpCollectMethod.startMaster(poList);
    }


}
