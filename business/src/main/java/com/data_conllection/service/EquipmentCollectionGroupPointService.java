package com.data_conllection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.data_conllection.domain.po.EquipmentCollectionGroupPointPo;

import java.util.List;

public interface EquipmentCollectionGroupPointService extends IService<EquipmentCollectionGroupPointPo> {
    /**
     * 查询所有采集间隔时间
     */
    List<Long> getIntervalTimes();

    void insertCollectedValue(Long id, String collectedValue);
}
