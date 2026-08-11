package com.rehabilitation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rehabilitation.dto.EquipmentQueryDTO;
import com.rehabilitation.entity.Equipment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备信息Mapper接口
 */
public interface EquipmentMapper extends BaseMapper<Equipment> {

    /**
     * 分页查询设备列表
     */
    IPage<Equipment> selectEquipmentPage(Page<Equipment> page, @Param("query") EquipmentQueryDTO query);

    /**
     * 查询所有设备类型
     */
    List<String> selectAllEquipmentTypes();

    /**
     * 批量更新设备状态
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") String status);

    /**
     * 查询最大序号
     */
    Integer selectMaxSerialNumber();
}