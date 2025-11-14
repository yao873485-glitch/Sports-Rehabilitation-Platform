CREATE TABLE patient (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '患者系统ID',
    name VARCHAR(50) NOT NULL COMMENT '患者姓名',
    gender ENUM('男', '女') NOT NULL COMMENT '性别',
    birth_date DATE NOT NULL COMMENT '出生日期',
    id_card VARCHAR(100) COMMENT '证件号码',
    phone VARCHAR(20) UNIQUE COMMENT '联系电话',
    medical_record_no VARCHAR(50) NOT NULL UNIQUE COMMENT '档案号（业务主键）',
    disease_type VARCHAR(100) NOT NULL COMMENT '主要病种',
    enrollment_institution VARCHAR(100) NOT NULL COMMENT '入组机构',
    address TEXT COMMENT '居住地址',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',

    INDEX idx_medical_record_no (medical_record_no),
    INDEX idx_phone (phone),
    INDEX idx_name (name),
    INDEX idx_enrollment_institution (enrollment_institution)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='患者基本信息表';





CREATE TABLE patient_detail (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '详情记录ID',
    patient_id INT NOT NULL COMMENT '关联患者表的主键ID',
    nickname VARCHAR(50) DEFAULT NULL COMMENT '客户昵称',
    id_card_type VARCHAR(50) NOT NULL COMMENT '证件类型',
    height_cm INT DEFAULT NULL COMMENT '身高（单位：厘米）',
    ethnicity VARCHAR(20) NOT NULL COMMENT '民族',
    occupation VARCHAR(50) NOT NULL COMMENT '职业',
    contact_province_city_district VARCHAR(100) DEFAULT NULL COMMENT '联系地址（省市区）',
    remark TEXT COMMENT '备注说明',

    -- 外键约束
    FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE ON UPDATE CASCADE,

    -- 索引优化
    INDEX idx_patient_id (patient_id),
    INDEX idx_nickname (nickname),
    INDEX idx_occupation (occupation)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='患者详细信息表';


-- ==================== 随访管理相关表 ====================

-- 随访项目表（主数据）
CREATE TABLE followup_program_master (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    project_code VARCHAR(50) NOT NULL UNIQUE COMMENT '项目编号：唯一标识项目的编码',
    project_name VARCHAR(200) NOT NULL COMMENT '项目名称',
    project_description TEXT COMMENT '项目介绍',
    linked_followup_plan VARCHAR(200) COMMENT '绑定随访计划：关联的随访计划名称',
    is_published TINYINT(1) DEFAULT 0 COMMENT '是否发布：0-未发布，1-已发布',
    operator VARCHAR(100) COMMENT '操作人',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    last_modified_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',

    INDEX idx_project_code (project_code),
    INDEX idx_project_name (project_name),
    INDEX idx_is_published (is_published)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='随访项目主数据表 - 存储随访项目的基本信息和配置';


-- 随访计划仓库表
CREATE TABLE followup_plan_repository (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    plan_name VARCHAR(200) NOT NULL COMMENT '计划名称',
    linked_project_id BIGINT COMMENT '关联项目ID：外键关联followup_program_master表',
    linked_project VARCHAR(200) COMMENT '随访项目名称：冗余字段，便于展示',
    version_number VARCHAR(50) COMMENT '当前版本号',
    status VARCHAR(50) COMMENT '状态：草稿/已发布/已归档等',
    version_remark TEXT COMMENT '版本备注',
    invitation_qr_code_url VARCHAR(500) COMMENT '随访邀请二维码URL',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    last_modified_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近一次修改时间',

    INDEX idx_linked_project_id (linked_project_id),
    INDEX idx_plan_name (plan_name),
    INDEX idx_status (status),
    FOREIGN KEY (linked_project_id) REFERENCES followup_program_master(id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='随访计划仓库表 - 存储随访计划的元数据和版本信息';


-- 随访患者入组记录表（随访列表）
CREATE TABLE followup_patient_enrollment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID，系统内部唯一标识',
    serial_number VARCHAR(50) COMMENT '序号：用于展示和排序的外显编号',

    -- 外键关联
    patient_id INT NOT NULL COMMENT '患者ID：外键关联patient表',
    followup_plan_id BIGINT COMMENT '随访计划ID：外键关联followup_plan_repository表',
    followup_project_id BIGINT COMMENT '随访项目ID：外键关联followup_program_master表',

    -- 随访团队信息
    followup_team VARCHAR(200) COMMENT '随访团队：负责该患者的医疗团队名称',
    primary_doctor VARCHAR(100) COMMENT '主负责医生：直接管理该患者的主要医生姓名',
    health_manager VARCHAR(100) COMMENT '健康管理师：协助管理的健康管理师姓名',

    -- 随访流程时间信息
    application_time DATETIME COMMENT '申请时间：患者提交随访申请的时间',
    enrollment_time DATETIME COMMENT '加入项目时间：患者正式被纳入随访项目的时间',

    -- 随访状态
    followup_status ENUM('待入组','随访中','已暂停','已完成','已退出') DEFAULT '待入组' COMMENT '随访状态：当前患者在随访流程中的状态',

    -- 备注信息
    enrollment_remark TEXT COMMENT '入组备注：记录入组时的特殊说明',

    -- 审计字段
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_by VARCHAR(100) COMMENT '创建人',
    updated_by VARCHAR(100) COMMENT '更新人',

    -- 索引
    INDEX idx_patient_id (patient_id),
    INDEX idx_followup_plan_id (followup_plan_id),
    INDEX idx_followup_project_id (followup_project_id),
    INDEX idx_followup_status (followup_status),
    INDEX idx_application_time (application_time),
    INDEX idx_primary_doctor (primary_doctor),

    -- 外键约束
    FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (followup_plan_id) REFERENCES followup_plan_repository(id) ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (followup_project_id) REFERENCES followup_program_master(id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='随访患者入组记录表 - 记录患者加入随访项目的信息，通过外键关联患者、计划、项目表';


-- 随访记录表（开始随访后的详细记录）
CREATE TABLE followup_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',

    -- 外键关联
    enrollment_id BIGINT NOT NULL COMMENT '入组记录ID：外键关联followup_patient_enrollment表',
    patient_id INT NOT NULL COMMENT '患者ID：外键关联patient表',

    -- 随访基本信息
    followup_date DATETIME NOT NULL COMMENT '随访日期：本次随访的时间',
    followup_type VARCHAR(50) COMMENT '随访类型：电话随访/门诊随访/家庭随访/线上随访',
    followup_doctor VARCHAR(100) COMMENT '随访医生：执行本次随访的医生',

    -- 随访详细内容
    followup_content TEXT COMMENT '随访内容：本次随访的详细记录',
    followup_result VARCHAR(200) COMMENT '随访结果：良好/一般/需要干预/异常',

    -- 健康指标（可选）
    blood_pressure VARCHAR(50) COMMENT '血压值：如120/80',
    blood_sugar DECIMAL(5,2) COMMENT '血糖值：单位mmol/L',
    heart_rate INT COMMENT '心率：单位次/分',
    body_temperature DECIMAL(4,2) COMMENT '体温：单位℃',
    weight DECIMAL(5,2) COMMENT '体重：单位kg',

    -- 随访计划
    next_followup_date DATETIME COMMENT '下次随访日期',

    -- 备注与附件
    remarks TEXT COMMENT '备注',
    attachments VARCHAR(1000) COMMENT '附件：多个附件URL用逗号分隔',

    -- 审计字段
    created_by VARCHAR(100) COMMENT '创建人',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    updated_by VARCHAR(100) COMMENT '更新人',

    -- 索引
    INDEX idx_enrollment_id (enrollment_id),
    INDEX idx_patient_id (patient_id),
    INDEX idx_followup_date (followup_date),
    INDEX idx_followup_doctor (followup_doctor),
    INDEX idx_followup_type (followup_type),

    -- 外键约束
    FOREIGN KEY (enrollment_id) REFERENCES followup_patient_enrollment(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (patient_id) REFERENCES patient(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='随访记录表 - 存储每次随访的详细记录，通过外键关联患者和入组记录表获取患者信息';


