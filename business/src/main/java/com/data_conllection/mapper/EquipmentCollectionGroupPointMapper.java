package com.data_conllection.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.data_conllection.domain.po.EquipmentCollectionGroupPointPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EquipmentCollectionGroupPointMapper extends BaseMapper<EquipmentCollectionGroupPointPo> {

    /**
     * 查询所有采集间隔时间
     */
    List<Long> getIntervalTimes();

    /**
     * 插入采集到的值
     */
    void insertCollectedValue(@Param("id") Long id, @Param("collectedValue") String collectedValue);
}
