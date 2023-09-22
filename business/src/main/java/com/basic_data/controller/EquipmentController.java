package com.basic_data.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.basic_data.domain.po.EquipmentPo;
import com.basic_data.service.EquipmentService;
import com.common.core.controller.BaseController;
import com.common.core.domain.AjaxResult;
import com.common.core.page.TableDataInfo;
import com.common.utils.DateUtils;
import com.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController extends BaseController {

    @Autowired
    private EquipmentService equipmentService;

    /**
     * 查询采集设备
     */
    @GetMapping("/list")
    public TableDataInfo list(EquipmentPo equipmentPo){
        startPage();
        List<EquipmentPo> list = equipmentService.list(
                new LambdaQueryWrapper<EquipmentPo>()
                        .eq(StringUtils.isNotEmpty(equipmentPo.getName()), EquipmentPo::getName, equipmentPo.getName())
                        .eq(StringUtils.isNotEmpty(equipmentPo.getCode()), EquipmentPo::getCode, equipmentPo.getCode())
        );
        return getDataTable(list);
    }

    /**
     * 添加采集设备
     */
    @PostMapping("/add")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult add(@RequestBody EquipmentPo equipmentPo){
        equipmentPo.setCreateTime(DateUtils.getNowDate());
        equipmentPo.setCreateBy(String.valueOf(getUserId()));
        return equipmentService.save(equipmentPo) ? success() : error();
    }

    /**
     * 修改采集设备
     */
    @PutMapping("/put")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult put(@RequestBody EquipmentPo equipmentPo){
        equipmentPo.setUpdateTime(DateUtils.getNowDate());
        equipmentPo.setUpdateBy(String.valueOf(getUserId()));
        return equipmentService.updateById(equipmentPo) ? success() : error();
    }

    /**
     * 删除采集设备
     */
    @DeleteMapping("/del/{id}")
    public AjaxResult del(@PathVariable Long id){
        return equipmentService.removeById(id) ? success() : error();
    }
}
