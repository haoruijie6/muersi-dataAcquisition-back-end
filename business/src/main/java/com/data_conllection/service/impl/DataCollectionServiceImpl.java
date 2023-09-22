package com.data_conllection.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.data_conllection.domain.po.DataCollectionPo;
import com.data_conllection.mapper.DataCollectionMapper;
import com.data_conllection.service.DataCollectionService;
import org.springframework.stereotype.Service;

@Service
public class DataCollectionServiceImpl extends ServiceImpl<DataCollectionMapper, DataCollectionPo> implements DataCollectionService {


}
