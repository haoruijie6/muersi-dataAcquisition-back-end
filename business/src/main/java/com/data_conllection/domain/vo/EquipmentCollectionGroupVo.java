package com.data_conllection.domain.vo;

import com.basic_data.domain.po.EquipmentPo;
import com.data_conllection.domain.po.EquipmentCollectionGroupPo;
import lombok.Data;

import java.util.List;

@Data
public class EquipmentCollectionGroupVo extends EquipmentPo {

    private List<EquipmentCollectionGroupPo> groupPoList;

}
