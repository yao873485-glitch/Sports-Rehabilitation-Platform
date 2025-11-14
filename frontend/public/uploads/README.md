# 文件上传目录说明

## 目录结构

```
uploads/
├── patient-docs/           # 患者文档目录
│   ├── [patient_id]/       # 按患者ID分目录
│   │   ├── reports/        # 报告文件
│   │   ├── images/         # 图片文件
│   │   └── documents/      # 其他文档
│   └── temp/               # 临时文件目录
└── system/                 # 系统文件目录
```

## 文件命名规则

- 报告文件：`[timestamp]_[patient_id]_[type].[ext]`
- 图片文件：`[timestamp]_[patient_id]_[type].[ext]`
- 临时文件：`temp_[timestamp]_[random].[ext]`

## 文件类型限制

- 支持的图片格式：jpg, jpeg, png
- 文件大小限制：500KB
- 文件保存位置：本目录下按患者ID分类保存

## 注意事项

1. 所有上传的文件都会重命名，避免文件名冲突
2. 定期清理temp目录下的临时文件
3. 敏感文档需要加密存储
4. 定期备份重要文档