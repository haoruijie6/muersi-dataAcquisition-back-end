package com.data_conllection.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 采集持久化数据实体
 */
@Data
@NoArgsConstructor
@TableName("t_data_collection")
public class DataCollectionPo {

    private Long id;

    private Long pointId;

    private String pointName;

    private String pointValue;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date createTime;

    public DataCollectionPo(Long pointId, String pointName, String pointValue, Date createTime) {
        this.pointId = pointId;
        this.pointName = pointName;
        this.pointValue = pointValue;
        this.createTime = createTime;
    }
}
