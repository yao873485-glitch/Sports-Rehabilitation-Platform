package com.rehabilitation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rehabilitation.dto.EquipmentBatchOperationDTO;
import com.rehabilitation.dto.EquipmentQueryDTO;
import com.rehabilitation.dto.EquipmentVO;
import com.rehabilitation.entity.Equipment;

import java.util.List;

/**
 * 设备信息服务接口
 */
public interface EquipmentService extends IService<Equipment> {

    /**
     * 分页查询设备列表
     */
    IPage<EquipmentVO> getEquipmentPage(EquipmentQueryDTO query);

    /**
     * 获取设备详情
     */
    EquipmentVO getEquipmentById(Long id);

    /**
     * 新增设备
     */
    void addEquipment(Equipment equipment);

    /**
     * 更新设备
     */
    void updateEquipment(Equipment equipment);

    /**
     * 删除设备
     */
    void deleteEquipment(Long id);

    /**
     * 获取所有设备类型
     */
    List<String> getAllEquipmentTypes();

    /**
     * 批量操作设备
     */
    void batchOperation(EquipmentBatchOperationDTO batchOperation);

    /**
     * 导入设备数据
     */
    void importEquipment(List<Equipment> equipmentList);
}