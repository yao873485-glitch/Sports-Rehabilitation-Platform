package com.rehabilitation.controller;

import com.rehabilitation.dto.FollowupRecordDTO;
import com.rehabilitation.entity.FollowupRecord;
import com.rehabilitation.service.FollowupRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 随访记录控制器
 */
@RestController
@RequestMapping("/api/followup-record")
public class FollowupRecordController {

    @Autowired
    private FollowupRecordService followupRecordService;

    /**
     * 保存随访记录
     */
    @PostMapping
    public Map<String, Object> saveFollowupRecord(@RequestBody FollowupRecordDTO dto) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = followupRecordService.saveFollowupRecord(dto);
            if (success) {
                result.put("code", 200);
                result.put("message", "随访记录保存成功");
                result.put("success", true);
            } else {
                result.put("code", 500);
                result.put("message", "随访记录保存失败");
                result.put("success", false);
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "保存失败：" + e.getMessage());
            result.put("success", false);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据入组记录ID查询随访记录列表
     */
    @GetMapping("/list/{enrollmentId}")
    public Map<String, Object> getFollowupRecordsByEnrollmentId(@PathVariable Long enrollmentId) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<FollowupRecord> records = followupRecordService.getFollowupRecordsByEnrollmentId(enrollmentId);
            result.put("code", 200);
            result.put("message", "查询成功");
            result.put("success", true);
            result.put("data", records);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "查询失败：" + e.getMessage());
            result.put("success", false);
            e.printStackTrace();
        }
        return result;
    }
}
