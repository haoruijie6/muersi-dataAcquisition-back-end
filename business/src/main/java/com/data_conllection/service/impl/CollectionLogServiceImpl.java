package com.data_conllection.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.data_conllection.domain.po.CollectionLogPo;
import com.data_conllection.mapper.CollectionLogMapper;
import com.data_conllection.service.CollectionLogService;
import org.springframework.stereotype.Service;

@Service
public class CollectionLogServiceImpl extends ServiceImpl<CollectionLogMapper, CollectionLogPo> implements CollectionLogService {
}
