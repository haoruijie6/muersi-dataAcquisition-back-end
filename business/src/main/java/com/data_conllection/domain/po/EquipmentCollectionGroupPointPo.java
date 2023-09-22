package com.data_conllection.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("t_equipment_collection_group_point")
public class EquipmentCollectionGroupPointPo implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    //组id
    private Long groupId;
    //采集点名称
    private String name;
    //从站地址
    private Integer slaveId;
    //寄存器类型 0 read coils 1 input status 2 holding register 3 input register
    private Integer registerType;
    //地址
    private Integer address;
    //采集偏移量
    private Integer offset;
    //是否持久化 0 不持久 1 持久
    private Integer dataPersistence;
    //数据类型
    private Integer dataType;

    //采集间隔 ms
    private Long intervalTime;

    //采集状态 0 未采集 1 正在采集
    private Integer collectionStatus;

    //采集的值
    private String collectedValue;

    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date createTime;

    private String updateBy;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date updateTime;

}
