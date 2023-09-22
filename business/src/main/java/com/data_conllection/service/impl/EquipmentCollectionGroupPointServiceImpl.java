package com.data_conllection.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.data_conllection.domain.po.EquipmentCollectionGroupPointPo;
import com.data_conllection.mapper.EquipmentCollectionGroupPointMapper;
import com.data_conllection.service.EquipmentCollectionGroupPointService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentCollectionGroupPointServiceImpl extends ServiceImpl<EquipmentCollectionGroupPointMapper,EquipmentCollectionGroupPointPo> implements EquipmentCollectionGroupPointService {

    /**
     * 查询所有采集间隔时间
     */
    @Override
    public List<Long> getIntervalTimes() {
        return baseMapper.getIntervalTimes();
    }

    @Override
    public void insertCollectedValue(Long id, String collectedValue) {
        baseMapper.insertCollectedValue(id,collectedValue);
    }
}
