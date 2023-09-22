package com.data_conllection.common.modbus.tcp.collection_method;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.common.utils.DateUtils;
import com.data_conllection.common.modbus.tcp.cache.ModbusTcpCollectionPointsCache;
import com.data_conllection.common.modbus.tcp.cache.ModbusTcpConnectCache;
import com.data_conllection.common.modbus.tcp.service.ModbusTcpService;
import com.data_conllection.domain.po.CollectionLogPo;
import com.data_conllection.domain.po.DataCollectionPo;
import com.data_conllection.domain.po.EquipmentCollectionGroupPo;
import com.data_conllection.domain.po.EquipmentCollectionGroupPointPo;
import com.data_conllection.service.CollectionLogService;
import com.data_conllection.service.DataCollectionService;
import com.data_conllection.service.EquipmentCollectionGroupPointService;
import com.data_conllection.service.EquipmentCollectionGroupService;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * modbus tcp 采集执行方法
 */
@Component("ModbusTcpCollectMethod")
public class ModbusTcpCollectMethod {
    @Autowired
    private EquipmentCollectionGroupPointService equipmentCollectionGroupPointService;

    @Autowired
    private DataCollectionService dataCollectionService;

    @Autowired
    private EquipmentCollectionGroupService equipmentCollectionGroupService;

    @Autowired
    private CollectionLogService collectionLogService;

    @Autowired
    private ModbusTcpService modbusTcpService;

    public void startMaster(List<EquipmentCollectionGroupPointPo> pointPos) {
        //同步 防止其他地方修改采集点
        synchronized (pointPos){
            if (ObjectUtils.isNotEmpty(pointPos) && pointPos.size() > 0) {
                pointPos.forEach(p -> {
                    //获取连接通道
                    ModbusMaster modbusTcpCache = ModbusTcpConnectCache.getModbusTcpCache(p.getGroupId());
                    //执行采集
                    try {
                        //读取数据
                        int[] res = modbusTcpService.read(p,modbusTcpCache);
                        //处理采集到的数据 转换32位或64位数
                        String resultStr = modbusTcpService.resultOfHandling(p, res);
                        //更新采集的数据
                        equipmentCollectionGroupPointService.insertCollectedValue(p.getId(), resultStr);
                        //判断是否持久化采集数据
                        if (p.getDataPersistence() == 1) {
                            DataCollectionPo dataCollectionPo = new DataCollectionPo(p.getId(), p.getName(), p.getCollectedValue(), DateUtils.getNowDate());
                            dataCollectionService.save(dataCollectionPo);
                        }
                        System.err.println(p.getName() + ":" + resultStr);
                    } catch (ModbusProtocolException e) { //modbus协议异常
                        connectionException(p.getGroupId());
                    } catch (ModbusNumberException e) { //采集点采集数据异常
                        //采集异常处理采集处理
                        collectException(p);
                    } catch (ModbusIOException e) { //连接通道异常
                        //连接异常修改数据
                        connectionException(p.getGroupId());
                    }
                });
            }
        }
    }

    /**
     * 处理连接异常业务
     * @param groupId 采集组id
     */
    @Transactional(rollbackFor = Exception.class)
    public void connectionException(Long groupId) {
        //修改状态
        equipmentCollectionGroupService.update(
                new LambdaUpdateWrapper<EquipmentCollectionGroupPo>()
                        .set(EquipmentCollectionGroupPo::getConnectionStatus, 0)
                        .eq(EquipmentCollectionGroupPo::getId, groupId)
        );
        //保存日志
        CollectionLogPo collectionLogPo = new CollectionLogPo(groupId, "采集时连接异常，系统自动修改组连接状态", DateUtils.getNowDate());
        collectionLogService.save(collectionLogPo);
        //将组下的采集点状态都改为未采集
        List<EquipmentCollectionGroupPointPo> poList = equipmentCollectionGroupPointService.list(
                new LambdaQueryWrapper<EquipmentCollectionGroupPointPo>()
                        .eq(EquipmentCollectionGroupPointPo::getGroupId, groupId)
                        .eq(EquipmentCollectionGroupPointPo::getCollectionStatus, 1)
        );
        poList.forEach(c -> {
            c.setCollectionStatus(0);
        });
        equipmentCollectionGroupPointService.updateBatchById(poList);
    }

    /**
     * 采集点采集异常业务处理
     * @param pointPo 采集点对象
     */
    @Transactional(rollbackFor = Exception.class)
    public void collectException(EquipmentCollectionGroupPointPo pointPo){
        //停止采集 删除此采集点的缓存 修改采集点状态
        List<EquipmentCollectionGroupPointPo> pointPos = ModbusTcpCollectionPointsCache.getCollectionThreadCache(pointPo.getIntervalTime());
        for (int i = 0; i < pointPos.size(); i++) {
            if (pointPos.get(i).getId() == pointPo.getId()) {
                pointPos.remove(i);
            }
        }
        pointPo.setCollectionStatus(0);
        equipmentCollectionGroupPointService.updateById(pointPo);
    }
}
