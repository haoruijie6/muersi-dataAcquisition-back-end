package com.basic_data.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.basic_data.domain.po.EquipmentPo;
import com.basic_data.mapper.EquipmentMapper;
import com.basic_data.service.EquipmentService;
import org.springframework.stereotype.Service;

@Service
public class EquipmentServiceImpl extends ServiceImpl<EquipmentMapper, EquipmentPo> implements EquipmentService {
}
