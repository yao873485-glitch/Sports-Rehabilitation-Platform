package com.rehabilitation.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rehabilitation.dto.EquipmentBatchOperationDTO;
import com.rehabilitation.dto.EquipmentQueryDTO;
import com.rehabilitation.dto.EquipmentVO;
import com.rehabilitation.entity.Equipment;
import com.rehabilitation.exception.BusinessException;
import com.rehabilitation.mapper.EquipmentMapper;
import com.rehabilitation.service.EquipmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 设备信息服务实现类
 */
@Service
public class EquipmentServiceImpl extends ServiceImpl<EquipmentMapper, Equipment> implements EquipmentService {

    private static final Logger log = LoggerFactory.getLogger(EquipmentServiceImpl.class);

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Override
    public IPage<EquipmentVO> getEquipmentPage(EquipmentQueryDTO query) {
        Page<Equipment> page = new Page<>(query.getPageNum(), query.getPageSize());
        IPage<Equipment> equipmentPage = equipmentMapper.selectEquipmentPage(page, query);

        // 转换为VO
        IPage<EquipmentVO> voPage = new Page<>(equipmentPage.getCurrent(), equipmentPage.getSize(), equipmentPage.getTotal());
        List<EquipmentVO> voList = equipmentPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }

    @Override
    public EquipmentVO getEquipmentById(Long id) {
        Equipment equipment = equipmentMapper.selectById(id);
        return convertToVO(equipment);
    }

    @Override
    public void addEquipment(Equipment equipment) {
        validateEquipmentCodeUnique(equipment);
        equipment.setCreatedTime(LocalDateTime.now());
        // 自动生成序号：基于当前设备数量+1
        try {
            Integer maxSerial = equipmentMapper.selectMaxSerialNumber();
            equipment.setSerialNumber(maxSerial != null ? maxSerial + 1 : 1);
        } catch (Exception e) {
            log.warn("获取最大序号失败，使用默认序号: " + e.getMessage());
            equipment.setSerialNumber(1);
        }

        // 保存到数据库
        boolean saved = this.save(equipment);
        if (!saved) {
            throw new RuntimeException("设备保存失败");
        }
    }

    @Override
    public void updateEquipment(Equipment equipment) {
        validateEquipmentCodeUnique(equipment);
        equipment.setLastOperationTime(LocalDateTime.now());
        equipmentMapper.updateById(equipment);
    }

    @Override
    public void deleteEquipment(Long id) {
        equipmentMapper.deleteById(id);
    }

    @Override
    public List<String> getAllEquipmentTypes() {
        return equipmentMapper.selectAllEquipmentTypes();
    }

    @Override
    @Transactional
    public void batchOperation(EquipmentBatchOperationDTO batchOperation) {
        List<Long> ids = batchOperation.getIds();
        String operationType = batchOperation.getOperationType();

        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("设备ID列表不能为空");
        }

        if ("上架".equals(operationType) || "下架".equals(operationType)) {
            equipmentMapper.batchUpdateStatus(ids, operationType);
        } else {
            throw new IllegalArgumentException("不支持的批量操作类型: " + operationType);
        }
    }

    @Override
    @Transactional
    public void importEquipment(List<Equipment> equipmentList) {
        if (equipmentList != null && !equipmentList.isEmpty()) {
            saveBatch(equipmentList);
        }
    }

    /**
     * 实体转VO
     */
    private EquipmentVO convertToVO(Equipment equipment) {
        if (equipment == null) {
            return null;
        }
        EquipmentVO vo = new EquipmentVO();
        BeanUtils.copyProperties(equipment, vo);
        return vo;
    }

    private void validateEquipmentCodeUnique(Equipment equipment) {
        String equipmentCode = equipment.getEquipmentCode();
        if (equipmentCode == null || equipmentCode.trim().isEmpty()) {
            return;
        }
        boolean exists = this.lambdaQuery()
                .eq(Equipment::getEquipmentCode, equipmentCode)
                .ne(equipment.getId() != null, Equipment::getId, equipment.getId())
                .count() > 0;
        if (exists) {
            throw new BusinessException(400, "设备编号已存在，请保持唯一");
        }
    }
}
