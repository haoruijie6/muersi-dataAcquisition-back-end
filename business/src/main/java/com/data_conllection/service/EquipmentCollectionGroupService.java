package com.data_conllection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.data_conllection.domain.po.EquipmentCollectionGroupPo;
import com.data_conllection.domain.vo.EquipmentCollectionGroupVo;

import java.util.List;

public interface EquipmentCollectionGroupService extends IService<EquipmentCollectionGroupPo> {

    /**
     * 查询设备关联组树状结果
     */
    List<EquipmentCollectionGroupVo> queryTreeList();

}
