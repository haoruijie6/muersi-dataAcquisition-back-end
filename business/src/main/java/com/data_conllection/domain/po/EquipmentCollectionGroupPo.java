package com.data_conllection.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("t_equipment_collection_group")
public class EquipmentCollectionGroupPo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long equipmentId;

    private String name;

    private String ip;

    private Integer port;

    //通道连接状态 0未连接 1已连接
    private Integer connectionStatus;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date createTime;

    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date updateTime;

    private String updateBy;
}
