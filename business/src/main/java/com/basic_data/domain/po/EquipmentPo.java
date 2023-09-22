package com.basic_data.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("t_equipment")
public class EquipmentPo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String code;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date createTime;

    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date updateTime;

    private String updateBy;

}
