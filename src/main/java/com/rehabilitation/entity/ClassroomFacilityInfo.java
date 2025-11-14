package com.rehabilitation.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 教室设施信息表
 * 记录各类运动教室的基本信息、容量、状态及时间戳
 */
@Data
@TableName("classroom_facility_info")
public class ClassroomFacilityInfo {

    /**
     * 主键ID，唯一标识每个教室记录
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 运动方式：该教室对应的运动类型，如游泳、瑜伽、普拉提、功率自行车等
     */
    private String exerciseType;

    /**
     * 教室名称：具体的教室名称，如"阳光瑜伽室"、"动感单车1号厅"
     */
    private String classroomName;

    /**
     * 课程时长：在该教室内进行的标准课程时长，单位为分钟，例如45、60等
     */
    private Integer classDuration;

    /**
     * 预约上限：该教室可预约的最大人数
     */
    private Integer reservationLimit;

    /**
     * 状态：教室当前的运营状态，控制是否可被预约或展示
     */
    private String status;

    /**
     * 上次操作时间：最近一次对该教室进行操作（如编辑、上下架）的时间
     */
    @TableField(value = "last_operation_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime lastOperationTime;

    /**
     * 创建时间：该教室记录在系统中创建的时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;
}