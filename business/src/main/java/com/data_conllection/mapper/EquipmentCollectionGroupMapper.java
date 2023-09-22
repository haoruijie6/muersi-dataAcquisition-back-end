package com.data_conllection.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.data_conllection.domain.po.EquipmentCollectionGroupPo;
import com.data_conllection.domain.vo.EquipmentCollectionGroupVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EquipmentCollectionGroupMapper extends BaseMapper<EquipmentCollectionGroupPo> {

    /**
     * 查询设备关联组树状结果
     */
    List<EquipmentCollectionGroupVo> queryTreeList();

}
