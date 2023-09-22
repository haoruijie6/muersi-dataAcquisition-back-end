package com.data_conllection.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.common.core.controller.BaseController;
import com.common.core.domain.AjaxResult;
import com.common.core.page.TableDataInfo;
import com.common.utils.DateUtils;
import com.common.utils.SecurityUtils;
import com.common.utils.StringUtils;
import com.data_conllection.common.data_collection_thread.DataCollectionThread;
import com.data_conllection.common.data_collection_thread.cache.DataCollectionThreadCache;
import com.data_conllection.common.modbus.tcp.cache.ModbusTcpCollectionPointsCache;
import com.data_conllection.common.modbus.tcp.collection_method.ModbusTcpCollectMethod;
import com.data_conllection.domain.dto.EquipmentCollectionGroupPointsDto;
import com.data_conllection.domain.po.EquipmentCollectionGroupPointPo;
import com.data_conllection.service.EquipmentCollectionGroupPointService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/equipmentCollectionGroupPoint")
public class EquipmentCollectionGroupPointController extends BaseController {

    @Autowired
    private EquipmentCollectionGroupPointService equipmentCollectionGroupPointService;

    @Autowired
    private ModbusTcpCollectMethod modbusTcpCollectMethod;

    /**
     * 分页查询采集点根据id
     */
    @GetMapping("/listPagesByGroupId")
    public TableDataInfo listPagesByGroupId(EquipmentCollectionGroupPointPo equipmentCollectionGroupPointPo) {
        startPage();
        List<EquipmentCollectionGroupPointPo> list = equipmentCollectionGroupPointService.list(
                new LambdaQueryWrapper<EquipmentCollectionGroupPointPo>()
                        .eq(EquipmentCollectionGroupPointPo::getGroupId, equipmentCollectionGroupPointPo.getGroupId())
                        .like(StringUtils.isNotEmpty(equipmentCollectionGroupPointPo.getName()), EquipmentCollectionGroupPointPo::getName, equipmentCollectionGroupPointPo.getName())
        );
        return getDataTable(list);
    }

    /**
     * 查询全部采集点根据id
     */
    @GetMapping("/listByGroupId")
    public AjaxResult listByGroupId(EquipmentCollectionGroupPointPo equipmentCollectionGroupPointPo) {
        List<EquipmentCollectionGroupPointPo> list = equipmentCollectionGroupPointService.list(
                new LambdaQueryWrapper<EquipmentCollectionGroupPointPo>()
                        .eq(EquipmentCollectionGroupPointPo::getGroupId, equipmentCollectionGroupPointPo.getGroupId())
                        .like(StringUtils.isNotEmpty(equipmentCollectionGroupPointPo.getName()), EquipmentCollectionGroupPointPo::getName, equipmentCollectionGroupPointPo.getName())
        );
        return AjaxResult.success(list);
    }

    /**
     * 批量添加采集点
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody EquipmentCollectionGroupPointsDto pointsDtos) {
        pointsDtos.getEquipmentCollectionGroupPointPos().forEach(equipmentCollectionGroupPointPo -> {
            equipmentCollectionGroupPointPo.setCreateBy(String.valueOf(SecurityUtils.getUserId()));
            equipmentCollectionGroupPointPo.setCreateTime(DateUtils.getNowDate());
            equipmentCollectionGroupPointPo.setGroupId(pointsDtos.getGroupId());
            //查询是否有不存在的采集间隔
            int count = equipmentCollectionGroupPointService.count(
                    new LambdaQueryWrapper<EquipmentCollectionGroupPointPo>()
                            .eq(EquipmentCollectionGroupPointPo::getIntervalTime, equipmentCollectionGroupPointPo.getIntervalTime())
            );
            //判断线程缓存中是否有此key
            boolean b = DataCollectionThreadCache.collectionThreadCache.containsKey(equipmentCollectionGroupPointPo.getIntervalTime());
            //不存在则创建线程
            if (count == 0 || !b) {
                //创建线程
                DataCollectionThread dataCollectionThread = new DataCollectionThread(equipmentCollectionGroupPointPo.getIntervalTime());
                //开启线程
                dataCollectionThread.start();
                //放入缓存
                DataCollectionThreadCache.putCollectionThreadCache(equipmentCollectionGroupPointPo.getIntervalTime(), dataCollectionThread);
            }

        });
        return equipmentCollectionGroupPointService.saveBatch(pointsDtos.getEquipmentCollectionGroupPointPos()) ? success() : error();
    }

    /**
     * 修改采集点
     */
    @PutMapping("/put")
    public AjaxResult put(@RequestBody EquipmentCollectionGroupPointPo equipmentCollectionGroupPointPo) {
        EquipmentCollectionGroupPointPo byId = equipmentCollectionGroupPointService.getById(equipmentCollectionGroupPointPo.getId());
        equipmentCollectionGroupPointPo.setUpdateBy(String.valueOf(SecurityUtils.getUserId()));
        equipmentCollectionGroupPointPo.setUpdateTime(DateUtils.getNowDate());
        //查询是否有不存在的采集间隔
        int count = equipmentCollectionGroupPointService.count(
                new LambdaQueryWrapper<EquipmentCollectionGroupPointPo>()
                        .eq(EquipmentCollectionGroupPointPo::getIntervalTime, equipmentCollectionGroupPointPo.getIntervalTime())
        );
        //不存在则创建线程
        if (count == 0) {
            //创建线程
            DataCollectionThread dataCollectionThread = new DataCollectionThread(equipmentCollectionGroupPointPo.getIntervalTime());
            //开启线程
            dataCollectionThread.start();
            //放入缓存
            DataCollectionThreadCache.putCollectionThreadCache(equipmentCollectionGroupPointPo.getIntervalTime(), dataCollectionThread);
        }
        //判断是否修改了采集间隔
        if (!byId.getIntervalTime().equals(equipmentCollectionGroupPointPo.getIntervalTime())){
            int count2 = equipmentCollectionGroupPointService.count(
                    new LambdaQueryWrapper<EquipmentCollectionGroupPointPo>()
                            .eq(EquipmentCollectionGroupPointPo::getIntervalTime, byId.getIntervalTime())
            );
            if (count2 == 1) { //如果只要一个则删除线程
                DataCollectionThreadCache.removeCollectionThreadCache(byId.getIntervalTime());
            }
        }

        //判断是否在采集
        if (byId.getCollectionStatus() == 1) {
            //删除除之前重新放入
            List<EquipmentCollectionGroupPointPo> pointPos = ModbusTcpCollectionPointsCache.getCollectionThreadCache(byId.getIntervalTime());
            for (int i = 0; i < pointPos.size(); i++) {
                if (pointPos.get(i).getId() == equipmentCollectionGroupPointPo.getId()) {
                    pointPos.remove(i);
                }
            }
            //判断是否有相同采集间隔的采集点
            if (ModbusTcpCollectionPointsCache.collectionThreadCache.containsKey(equipmentCollectionGroupPointPo.getIntervalTime())) {
                //将采集点放入采集缓存中
                List<EquipmentCollectionGroupPointPo> pointPos2 = ModbusTcpCollectionPointsCache.collectionThreadCache.get(equipmentCollectionGroupPointPo.getIntervalTime());
                //校验null
                if (pointPos2 != null) {
                    ModbusTcpCollectionPointsCache.collectionThreadCache.get(equipmentCollectionGroupPointPo.getIntervalTime()).add(equipmentCollectionGroupPointPo);
                }else {
                    //存放一组采集点
                    List<EquipmentCollectionGroupPointPo> newPointPos = new ArrayList<>();
                    newPointPos.add(equipmentCollectionGroupPointPo);
                    ModbusTcpCollectionPointsCache.putCollectionThreadCache(equipmentCollectionGroupPointPo.getIntervalTime(), newPointPos);
                }
                System.err.println(
                        pointPos2.toString()
                );
            } else {
                //存放一组采集点
                List<EquipmentCollectionGroupPointPo> newPointPos = new ArrayList<>();
                newPointPos.add(equipmentCollectionGroupPointPo);
                ModbusTcpCollectionPointsCache.putCollectionThreadCache(equipmentCollectionGroupPointPo.getIntervalTime(), newPointPos);
            }

        }
        //设置原因的采集状态
        equipmentCollectionGroupPointPo.setCollectionStatus(byId.getCollectionStatus());
        return equipmentCollectionGroupPointService.updateById(equipmentCollectionGroupPointPo) ? success() : error();
    }

    /**
     * 删除采集点
     */
    @DeleteMapping("/del/{id}")
    public AjaxResult del(@PathVariable Long id) {
        //查询采集点信息
        EquipmentCollectionGroupPointPo equipmentCollectionGroupPointPo = equipmentCollectionGroupPointService.getById(id);
        //查询是否有不存在的采集间隔
        int count = equipmentCollectionGroupPointService.count(
                new LambdaQueryWrapper<EquipmentCollectionGroupPointPo>()
                        .eq(EquipmentCollectionGroupPointPo::getIntervalTime, equipmentCollectionGroupPointPo.getIntervalTime())
        );
        if (count == 1) { //如果只要一个则删除线程
            DataCollectionThreadCache.removeCollectionThreadCache(equipmentCollectionGroupPointPo.getIntervalTime());
        }
        //判断是否在采集
        if (equipmentCollectionGroupPointPo.getCollectionStatus() == 1){
            //删除缓存
            List<EquipmentCollectionGroupPointPo> pointPos = ModbusTcpCollectionPointsCache.getCollectionThreadCache(equipmentCollectionGroupPointPo.getIntervalTime());
            for (int i = 0; i < pointPos.size(); i++) {
                if (pointPos.get(i).getId() == equipmentCollectionGroupPointPo.getId()) {
                    pointPos.remove(i);
                }
            }
        }
        return equipmentCollectionGroupPointService.removeById(id) ? success() : error();
    }

    /**
     * 启动全部采集
     */
    @GetMapping("/startAllCollection/{groupId}")
    public AjaxResult startAllCollection(@PathVariable Long groupId) {
        try {
            List<EquipmentCollectionGroupPointPo> list = equipmentCollectionGroupPointService.list(
                    new LambdaQueryWrapper<EquipmentCollectionGroupPointPo>()
                            .eq(EquipmentCollectionGroupPointPo::getGroupId, groupId)
                            .eq(EquipmentCollectionGroupPointPo::getCollectionStatus, 0)
            );
            list.forEach(l -> {
                //判断是否有相同采集间隔的采集点
                if (ModbusTcpCollectionPointsCache.collectionThreadCache.containsKey(l.getIntervalTime())) {
                    //将采集点放入采集缓存中
                    List<EquipmentCollectionGroupPointPo> pointPos = ModbusTcpCollectionPointsCache.collectionThreadCache.get(l.getIntervalTime());
                    //判断
                    if (ObjectUtils.isNotEmpty(pointPos) && pointPos.size() > 0) {
                        ModbusTcpCollectionPointsCache.collectionThreadCache.get(l.getIntervalTime()).add(l);
                    }else {
                        //存放一组采集点
                        List<EquipmentCollectionGroupPointPo> newPointPos = new ArrayList<>();
                        newPointPos.add(l);
                        ModbusTcpCollectionPointsCache.putCollectionThreadCache(l.getIntervalTime(), newPointPos);
                    }
                } else {
                    //存放一组采集点
                    List<EquipmentCollectionGroupPointPo> newPointPos = new ArrayList<>();
                    newPointPos.add(l);
                    ModbusTcpCollectionPointsCache.putCollectionThreadCache(l.getIntervalTime(), newPointPos);
                }
                l.setCollectionStatus(1);
            });
            //修改采集状态
            equipmentCollectionGroupPointService.updateBatchById(list);
            //立刻执行一次采集
            modbusTcpCollectMethod.startMaster(list);
        } catch (Exception e) {
            throw new RuntimeException("处理采集异常");
        }
        return AjaxResult.success();
    }

    /**
     * 启动单个采集
     */
    @GetMapping("/startCollection")
    public AjaxResult startCollection(EquipmentCollectionGroupPointPo equipmentCollectionGroupPointPo) {
        try {
            //判断是否有相同采集间隔的采集点
            if (ModbusTcpCollectionPointsCache.collectionThreadCache.containsKey(equipmentCollectionGroupPointPo.getIntervalTime())) {
                //将采集点放入采集缓存中
                List<EquipmentCollectionGroupPointPo> pointPos = ModbusTcpCollectionPointsCache.collectionThreadCache.get(equipmentCollectionGroupPointPo.getIntervalTime());
                //判断
                if (ObjectUtils.isNotEmpty(pointPos) && pointPos.size() != 0) {
                    pointPos.add(equipmentCollectionGroupPointPo);
                }else {
                    //存放一组采集点
                    List<EquipmentCollectionGroupPointPo> newPointPos = new ArrayList<>();
                    newPointPos.add(equipmentCollectionGroupPointPo);
                    ModbusTcpCollectionPointsCache.putCollectionThreadCache(equipmentCollectionGroupPointPo.getIntervalTime(), newPointPos);
                }
            } else {
                //存放一组采集点
                List<EquipmentCollectionGroupPointPo> newPointPos = new ArrayList<>();
                newPointPos.add(equipmentCollectionGroupPointPo);
                ModbusTcpCollectionPointsCache.putCollectionThreadCache(equipmentCollectionGroupPointPo.getIntervalTime(), newPointPos);
            }
            //修改采集状态
            equipmentCollectionGroupPointPo.setCollectionStatus(1);
            equipmentCollectionGroupPointService.updateById(equipmentCollectionGroupPointPo);
            //立刻执行一次采集
            List<EquipmentCollectionGroupPointPo> l = new ArrayList<>();
            l.add(equipmentCollectionGroupPointPo);
            modbusTcpCollectMethod.startMaster(l);
        } catch (Exception e) {
            throw new RuntimeException("处理采集异常");
        }
        return AjaxResult.success();
    }

    /**
     * 停止全部采集点
     */
    @GetMapping("/stopAllCollection/{groupId}")
    public AjaxResult stopAllCollection(@PathVariable Long groupId) {
        try {
            List<EquipmentCollectionGroupPointPo> list = equipmentCollectionGroupPointService.list(
                    new LambdaQueryWrapper<EquipmentCollectionGroupPointPo>()
                            .eq(EquipmentCollectionGroupPointPo::getGroupId, groupId)
                            .eq(EquipmentCollectionGroupPointPo::getCollectionStatus, 1)
            );
            list.forEach(l -> {
                //删除此采集点的缓存
                List<EquipmentCollectionGroupPointPo> pointPos = ModbusTcpCollectionPointsCache.getCollectionThreadCache(l.getIntervalTime());
                for (int i = 0; i < pointPos.size(); i++) {
                    if (pointPos.get(i).getId() == l.getId()) {
                        pointPos.remove(i);
                    }
                }
                l.setCollectionStatus(0);
            });
            //修改采集状态
            equipmentCollectionGroupPointService.updateBatchById(list);
        } catch (Exception e) {
            throw new RuntimeException("处理采集异常");
        }
        return AjaxResult.success();
    }

    /**
     * 停止单个采集点
     */
    @GetMapping("/stopCollection")
    public AjaxResult stopAllCollection(EquipmentCollectionGroupPointPo equipmentCollectionGroupPointPo) {
        try {
            //删除此采集点的缓存
            List<EquipmentCollectionGroupPointPo> pointPos = ModbusTcpCollectionPointsCache.getCollectionThreadCache(equipmentCollectionGroupPointPo.getIntervalTime());
            for (int i = 0; i < pointPos.size(); i++) {
                if (pointPos.get(i).getId() == equipmentCollectionGroupPointPo.getId()) {
                    pointPos.remove(i);
                }
            }
            equipmentCollectionGroupPointPo.setCollectionStatus(0);
            //修改采集状态
            equipmentCollectionGroupPointService.updateById(equipmentCollectionGroupPointPo);
        } catch (Exception e) {
            throw new RuntimeException("处理采集异常");
        }
        return AjaxResult.success();
    }
}
