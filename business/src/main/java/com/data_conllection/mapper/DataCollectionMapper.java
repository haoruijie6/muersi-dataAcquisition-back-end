package com.data_conllection.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.data_conllection.domain.po.DataCollectionPo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DataCollectionMapper extends BaseMapper<DataCollectionPo> {
}
