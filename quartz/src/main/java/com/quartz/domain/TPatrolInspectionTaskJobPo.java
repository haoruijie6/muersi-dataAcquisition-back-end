package com.quartz.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

@Data
public class TPatrolInspectionTaskJobPo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 任务编号 */
    private String taskId;

    /** 任务计划id */
    private String planId;

    /** 任务状态 0待开始 1进行中 2已完成 3超时未完成 */
    private String state;

    //是否随机 0可以 1 不可以
    private Integer sort;

    private String wiringDiagramId;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    /** 应该开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shouldStartTime;

}
