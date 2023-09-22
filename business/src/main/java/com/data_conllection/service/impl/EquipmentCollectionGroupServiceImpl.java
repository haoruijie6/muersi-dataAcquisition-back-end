package com.data_conllection.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.data_conllection.domain.po.EquipmentCollectionGroupPo;
import com.data_conllection.domain.vo.EquipmentCollectionGroupVo;
import com.data_conllection.mapper.EquipmentCollectionGroupMapper;
import com.data_conllection.service.EquipmentCollectionGroupService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentCollectionGroupServiceImpl extends ServiceImpl<EquipmentCollectionGroupMapper, EquipmentCollectionGroupPo> implements EquipmentCollectionGroupService {
    /**
     * 查询设备关联组树状结果
     */
    @Override
    public List<EquipmentCollectionGroupVo> queryTreeList() {
        return baseMapper.queryTreeList();
    }
}
