package com.rehabilitation.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 设备信息实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("equipment")
public class Equipment {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 序号：显示设备在当前列表中的顺序编号
     */
    @TableField("serial_number")
    private Integer serialNumber;

    /**
     * 区域：显示设备所属的区域或部门
     */
    private String region;

    /**
     * 设备名称：显示设备的具体名称
     */
    private String equipmentName;

    /**
     * 设备类型：显示设备的类型分类
     */
    private String equipmentType;

    /**
     * 设备编号：显示设备的唯一标识编号
     */
    private String equipmentCode;

    /**
     * 状态：显示设备当前的状态（如上架、下架等）
     */
    private String status;

    /**
     * 上次操作时间：显示设备最近一次状态变更的时间
     */
    private LocalDateTime lastOperationTime;

    /**
     * 创建时间：显示设备记录创建的时间
     */
    private LocalDateTime createdTime;
}