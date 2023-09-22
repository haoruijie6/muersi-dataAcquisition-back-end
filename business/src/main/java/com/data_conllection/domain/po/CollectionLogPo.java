package com.data_conllection.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 采集日志实体
 */
@Data
@TableName("t_collection_log")
public class CollectionLogPo implements Serializable {

    private Long id;

    private Long relevanceId;

    private String info;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date createTime;
    public CollectionLogPo(){}

    public CollectionLogPo(Long relevanceId, String info, Date createTime) {
        this.relevanceId = relevanceId;
        this.info = info;
        this.createTime = createTime;
    }
}
