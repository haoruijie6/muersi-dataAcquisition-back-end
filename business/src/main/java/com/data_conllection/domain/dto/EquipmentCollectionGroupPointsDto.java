package com.data_conllection.domain.dto;

import com.data_conllection.domain.po.EquipmentCollectionGroupPointPo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class EquipmentCollectionGroupPointsDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long groupId;

    private List<EquipmentCollectionGroupPointPo> equipmentCollectionGroupPointPos;
}
