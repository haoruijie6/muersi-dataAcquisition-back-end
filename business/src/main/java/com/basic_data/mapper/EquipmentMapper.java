package com.basic_data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.basic_data.domain.po.EquipmentPo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EquipmentMapper extends BaseMapper<EquipmentPo> {
}
