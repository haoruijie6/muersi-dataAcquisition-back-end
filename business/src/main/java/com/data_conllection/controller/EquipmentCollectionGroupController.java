package com.data_conllection.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.common.core.controller.BaseController;
import com.common.core.domain.AjaxResult;
import com.common.utils.DateUtils;
import com.common.utils.SecurityUtils;
import com.data_conllection.common.modbus.tcp.ModbusTcpBuilder;
import com.data_conllection.common.modbus.tcp.cache.ModbusTcpConnectCache;
import com.data_conllection.domain.po.CollectionLogPo;
import com.data_conllection.domain.po.EquipmentCollectionGroupPo;
import com.data_conllection.domain.po.EquipmentCollectionGroupPointPo;
import com.data_conllection.service.CollectionLogService;
import com.data_conllection.service.EquipmentCollectionGroupPointService;
import com.data_conllection.service.EquipmentCollectionGroupService;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


/**
 * 设备采集组管理
 */
@RestController
@RequestMapping("/api/equipmentCollectionGroup")
public class EquipmentCollectionGroupController extends BaseController {

    @Autowired
    private EquipmentCollectionGroupService equipmentCollectionGroupService;

    @Autowired
    private EquipmentCollectionGroupPointService equipmentCollectionGroupPointService;

    @Autowired
    private CollectionLogService collectionLogService;


    /**
     * 查询设备关联采集组树
     */
    @GetMapping("/queryTreeList")
    public AjaxResult queryTreeList() {
        return AjaxResult.success(equipmentCollectionGroupService.queryTreeList());
    }

    /**
     * 获取组信息
     */
    @GetMapping("/getInfoById/{id}")
    public AjaxResult getInfoById(@PathVariable Long id){
        return AjaxResult.success(equipmentCollectionGroupService.getById(id));
    }
    /**
     * 添加采集组
     */
    @PostMapping("/addGroup")
    public AjaxResult addGroup(@RequestBody EquipmentCollectionGroupPo equipmentCollectionGroupPo){
        equipmentCollectionGroupPo.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
        equipmentCollectionGroupPo.setCreateTime(DateUtils.getNowDate());
        return equipmentCollectionGroupService.save(equipmentCollectionGroupPo) ? success() : error();
    }

    /**
     * 修改采集组
     */
    @PutMapping("/putGroup")
    public AjaxResult putGroup(@RequestBody EquipmentCollectionGroupPo equipmentCollectionGroupPo){
        EquipmentCollectionGroupPo byId = equipmentCollectionGroupService.getById(equipmentCollectionGroupPo.getId());
        //判断是否正在连接
        if (byId.getConnectionStatus() == 1){
            throw new RuntimeException("检测到通讯正在连接,请断开后再操作!");
        }
        equipmentCollectionGroupPo.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
        equipmentCollectionGroupPo.setCreateTime(DateUtils.getNowDate());
        return equipmentCollectionGroupService.updateById(equipmentCollectionGroupPo) ? success() : error();
    }

    /**
     * 删除采集组
     */
    @DeleteMapping("/del/{id}")
    public AjaxResult delGroup(@PathVariable Long id){
        EquipmentCollectionGroupPo byId = equipmentCollectionGroupService.getById(id);
        //判断采集组下是否存在采集点
        if (byId.getConnectionStatus() == 1){
            throw new RuntimeException("检测到通讯正在连接,请断开后再操作!");
        }
        return equipmentCollectionGroupService.removeById(id) ? success() : error();
    }

    /**
     * 测试采集组通讯
     */
    @GetMapping("/testConnection")
    public AjaxResult testConnection(EquipmentCollectionGroupPo equipmentCollectionGroupPo){
        //测试连接是否正常
        ModbusMaster modbusTcp = ModbusTcpBuilder.getModbusTcp(equipmentCollectionGroupPo.getIp(), equipmentCollectionGroupPo.getPort());
        return modbusTcp.isConnected() ? success() : new AjaxResult(201,"连接异常请查看配置");
    }

    /**
     * 连接/断开采集组
     */
    @GetMapping("/connection")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult connection(EquipmentCollectionGroupPo equipmentCollectionGroupPo) {
        //连接
        if (equipmentCollectionGroupPo.getConnectionStatus() == 1){
            //测试连接是否正常
            ModbusMaster modbusTcp = ModbusTcpBuilder.getModbusTcp(equipmentCollectionGroupPo.getIp(), equipmentCollectionGroupPo.getPort());
            if (modbusTcp.isConnected()){
                ModbusTcpConnectCache.putModbusTcpCache(equipmentCollectionGroupPo,modbusTcp);
            }else {
                //放入缓存
                throw new RuntimeException("连接异常请检查配置是否正确！");
            }
        }else { //断开
            //检查组下是否存在采集的点位
            int count = equipmentCollectionGroupPointService.count(
                    new LambdaQueryWrapper<EquipmentCollectionGroupPointPo>()
                            .eq(EquipmentCollectionGroupPointPo::getGroupId, equipmentCollectionGroupPo.getId())
                            .eq(EquipmentCollectionGroupPointPo::getCollectionStatus, 1)
            );
            if (count > 0){
                throw new RuntimeException("检测到组下存在正在采集的点位，请停止采集后再断开连接！");
            }
            ModbusMaster modbusTcpCache = ModbusTcpConnectCache.getModbusTcpCache(equipmentCollectionGroupPo);
            try {
                modbusTcpCache.disconnect();
            } catch (ModbusIOException e) {
                //保存日志
                CollectionLogPo collectionLogPo = new CollectionLogPo(equipmentCollectionGroupPo.getId(),"端口连接通道失败，未知原因待排查！",DateUtils.getNowDate());
                collectionLogService.save(collectionLogPo);
            }
        }
        //修改状态
        return equipmentCollectionGroupService.update(
                new LambdaUpdateWrapper<EquipmentCollectionGroupPo>()
                        .set(EquipmentCollectionGroupPo::getConnectionStatus,equipmentCollectionGroupPo.getConnectionStatus())
                        .eq(EquipmentCollectionGroupPo::getId,equipmentCollectionGroupPo.getId())
        ) ? success() : error();
    }

}
