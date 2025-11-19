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





-- auto-generated definition
create table patient_detail
(
    id                             int auto_increment comment '详情记录ID'
        primary key,
    patient_id                     int          not null comment '关联患者表的主键ID',
    nickname                       varchar(50)  null comment '客户昵称',
    id_card_type                   varchar(50)  not null comment '证件类型',
    height_cm                      int          null comment '身高（单位：厘米）',
    ethnicity                      varchar(20)  not null comment '民族',
    occupation                     varchar(50)  not null comment '职业',
    contact_province_city_district varchar(100) null comment '联系地址（省市区）',
    detail_address                 varchar(255) null comment '详细地址（门牌号、小区名称等）',
    remark                         text         null comment '备注说明',
    constraint patient_detail_ibfk_1
        foreign key (patient_id) references patient (id)
            on update cascade on delete cascade
)
    comment '患者详细信息表' collate = utf8mb4_unicode_ci;

create index idx_nickname
    on patient_detail (nickname);

create index idx_occupation
    on patient_detail (occupation);

create index idx_patient_id
    on patient_detail (patient_id);



-- auto-generated definition
create table patient_scheme_info
(
    id          bigint auto_increment comment '主键ID'
        primary key,
    patient_id  int                                not null comment '患者ID',
    scheme_id   varchar(100)                       not null comment '方案编号，唯一标识',
    status      tinyint  default 1                 not null comment '方案状态：1-执行中，2-已结束',
    create_time datetime default CURRENT_TIMESTAMP not null comment '方案创建时间',
    end_time    datetime                           null comment '预计或实际结束时间',
    constraint scheme_id
        unique (scheme_id),
    constraint fk_scheme_info_patient
        foreign key (patient_id) references patient (id)
            on delete cascade
)
    comment '患者方案信息表（精简版）';

create index idx_create_time
    on patient_scheme_info (create_time);

create index idx_patient_id
    on patient_scheme_info (patient_id);

create index idx_scheme_id
    on patient_scheme_info (scheme_id);

create index idx_status
    on patient_scheme_info (status);


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


-- 随访项目配置表（独立表）
CREATE TABLE followup_project_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    project_code VARCHAR(20) NOT NULL UNIQUE COMMENT '项目标号（10位随机字符串）',
    project_name VARCHAR(200) NOT NULL COMMENT '项目名称',
    project_brief TEXT COMMENT '项目简介',
    project_description TEXT COMMENT '项目介绍',
    disease_type VARCHAR(100) NOT NULL COMMENT '项目病种',
    project_type VARCHAR(100) COMMENT '项目类型',
    consultation_times INT DEFAULT 1 COMMENT '患者咨询次数',
    single_duration INT DEFAULT 1 COMMENT '单次时长（小时）',
    bind_plan VARCHAR(500) COMMENT '绑定随访计划',
    operator VARCHAR(100) DEFAULT '系统' COMMENT '操作人',
    is_published TINYINT(1) DEFAULT 0 COMMENT '是否发布：0-否，1-是',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    INDEX idx_project_code (project_code),
    INDEX idx_project_name (project_name),
    INDEX idx_disease_type (disease_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='随访项目配置表 - 存储随访项目的详细配置信息';


-- =====================================================
-- 随访计划相关表
-- =====================================================

-- 随访计划主表
CREATE TABLE followup_plan (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    plan_name VARCHAR(100) NOT NULL COMMENT '计划名称',
    plan_description TEXT NOT NULL COMMENT '计划描述',
    linked_project_id BIGINT NOT NULL COMMENT '关联的随访项目ID',
    linked_project_name VARCHAR(100) COMMENT '关联的随访项目名称',
    available_team VARCHAR(100) NOT NULL COMMENT '可用团队',
    owner_institution VARCHAR(100) NOT NULL COMMENT '所属机构',
    first_followup_baseline VARCHAR(100) NOT NULL COMMENT '首次随访基线',
    version_number VARCHAR(20) DEFAULT 'V1.0' COMMENT '版本号',
    version_remark VARCHAR(500) COMMENT '版本备注',
    status VARCHAR(20) DEFAULT '未发布' COMMENT '状态：未发布、已发布、已停用',
    invitation_qr_code_url VARCHAR(500) COMMENT '随访邀请二维码URL',
    is_draft TINYINT(1) DEFAULT 0 COMMENT '是否草稿：0-否，1-是',
    operator VARCHAR(100) DEFAULT '系统' COMMENT '操作人',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',

    INDEX idx_plan_name (plan_name),
    INDEX idx_status (status),
    INDEX idx_linked_project_id (linked_project_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='随访计划主表';

-- 随访计划任务配置表
CREATE TABLE followup_plan_task (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    plan_id BIGINT NOT NULL COMMENT '关联的随访计划ID',
    task_name VARCHAR(100) NOT NULL COMMENT '任务名称',
    task_order INT DEFAULT 1 COMMENT '任务顺序',
    execute_day INT NOT NULL COMMENT '执行时间（第几天）',
    task_content TEXT COMMENT '任务内容描述',
    is_enabled TINYINT(1) DEFAULT 1 COMMENT '是否启用：0-否，1-是',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    INDEX idx_plan_id (plan_id),
    INDEX idx_execute_day (execute_day),
    FOREIGN KEY (plan_id) REFERENCES followup_plan(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='随访计划任务配置表';

-- 随访计划提醒设置表
CREATE TABLE followup_plan_reminder (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    plan_id BIGINT NOT NULL COMMENT '关联的随访计划ID',
    reminder_type VARCHAR(20) NOT NULL COMMENT '提醒类型：early-提前提醒、overdue-逾期提醒',
    is_enabled TINYINT(1) DEFAULT 0 COMMENT '是否启用：0-否，1-是',
    days INT COMMENT '提前/逾期天数',
    reminder_methods VARCHAR(200) COMMENT '提醒方式（多个用逗号分隔）：短信,APP推送,微信',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    INDEX idx_plan_id (plan_id),
    INDEX idx_reminder_type (reminder_type),
    FOREIGN KEY (plan_id) REFERENCES followup_plan(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='随访计划提醒设置表';



-- ====================================
-- 医教图文内容管理表
-- ====================================

CREATE TABLE education_image_content (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    content_id VARCHAR(50) UNIQUE NOT NULL COMMENT '内容ID（业务主键）',
    content_type VARCHAR(20) DEFAULT '文章' COMMENT '内容类型',
    content_title VARCHAR(100) NOT NULL COMMENT '文章标题',
    author VARCHAR(50) NOT NULL COMMENT '作者',
    author_type VARCHAR(50) DEFAULT '平台运营' COMMENT '作者类型',
    tags VARCHAR(200) COMMENT '文章标签',
    category_section VARCHAR(50) COMMENT '所属板块',
    publish_time DATETIME COMMENT '发布时间',
    support_game_mode TINYINT(1) DEFAULT 1 COMMENT '是否支持游客模式：0-不支持，1-支持',
    is_original TINYINT(1) DEFAULT 1 COMMENT '是否原创：0-否，1-是',
    article_description TEXT COMMENT '文章描述',
    cover_image VARCHAR(500) COMMENT '封面图URL',
    content_source VARCHAR(20) DEFAULT '系统录入' COMMENT '文章来源：系统录入、外链',
    article_content LONGTEXT COMMENT '文章内容（富文本）',
    external_link VARCHAR(500) COMMENT '外链地址',
    module_category VARCHAR(20) DEFAULT '医教' COMMENT '所属模块：医教、患教',
    like_count_base INT DEFAULT 0 COMMENT '点赞基数',
    view_count_base INT DEFAULT 0 COMMENT '阅读基数',
    like_count INT DEFAULT 0 COMMENT '实际点赞量',
    view_count INT DEFAULT 0 COMMENT '实际阅读量',
    content_status VARCHAR(20) DEFAULT '草稿' COMMENT '内容状态：草稿、待审核、已上架、未上架、已下架',
    article_link VARCHAR(500) COMMENT '文章链接',
    operator VARCHAR(100) DEFAULT '系统管理员' COMMENT '操作人',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    INDEX idx_content_id (content_id),
    INDEX idx_content_title (content_title),
    INDEX idx_author (author),
    INDEX idx_category_section (category_section),
    INDEX idx_content_status (content_status),
    INDEX idx_publish_time (publish_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='医教图文内容表';




create table edu_video_asset
(
    id                   bigint auto_increment comment '主键ID，系统内部唯一标识'
        primary key,
    content_type         enum ('视频') default '视频' not null comment '内容类型：视频',
    video_title          varchar(255)                                                         not null comment '视频标题：视频的显示名称，用于列表展示和搜索，如"孕期营养指导"',
    video_id             varchar(50)                                                          not null comment '视频ID：全局唯一的标识符（如系统生成ID或UUID），用于接口调用、数据追踪和外部引用',
    author               varchar(100)                                                         null comment '作者：视频内容的创建者或责任编辑姓名',
    author_type          varchar(50)                                                          null comment '作者类型：如"平台运营"等',
    publish_time         datetime                                                             null comment '发布时间：视频正式对外发布的具体时间；未发布时可为空',
    support_game_mode    tinyint(1) default 1                                                 null comment '是否支持游客模式：1-支持，0-不支持',
    is_original          tinyint(1) default 1                                                 null comment '是否原创：1-是，0-否',
    video_description    text                                                                 null comment '视频描述：对视频内容的简要说明或摘要，可用于预览和SEO，最多300字',
    cover_image          varchar(500)                                                         null comment '封面图：图片URL地址，支持多张图片（逗号分隔），最多3张，单张最大370*225',
    video_source         enum ('本地上传', '在线文件地址') default '本地上传'                  not null comment '视频来源：本地上传或在线文件地址',
    video_url            varchar(500)                                                         null comment '文件地址：视频文件的URL地址',
    module_category      enum ('医教', '患教')                                                null comment '所属模块：内容所属的业务模块分类',
    like_count_base      int default 0                                                        null comment '点赞基数：初始点赞量基数',
    view_count_base      int default 0                                                        null comment '阅读基数：初始阅读量基数',
    like_count           int default 0                                                        not null comment '点赞量：记录用户点赞的总数，反映内容受欢迎程度',
    view_count           int default 0                                                        not null comment '播放量：记录该视频被播放的总次数，用于效果评估（注：原"阅读量"应为播放量）',
    content_status       enum ('草稿', '待审核', '已上架', '未上架', '已下架') default '草稿' not null comment '内容状态：标识视频当前所处的生命周期阶段，用于权限控制与前端展示逻辑',
    operator             varchar(100) default '系统管理员'                                     null comment '操作人',
    created_at           datetime default CURRENT_TIMESTAMP                                   null comment '创建时间',
    updated_at           datetime default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP       null comment '更新时间',
    constraint video_id
        unique (video_id)
)
    comment '宣教视频资源表 - 存储所有健康教育类视频素材的元信息、状态及统计指标' collate = utf8mb4_unicode_ci;


-- ==================== 内容应用配置表 ====================
-- 用于管理图文和视频内容的应用配置信息（应用模块、项目、频道、关联疾病症状商品等）
CREATE TABLE content_application_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    content_id VARCHAR(50) NOT NULL COMMENT '内容ID：关联education_image_content或edu_video_asset的content_id/video_id',
    content_type ENUM('文章', '视频') NOT NULL COMMENT '内容类型：文章或视频',

    -- 应用相关字段
    application_module VARCHAR(100) COMMENT '应用模块：如"健康管理"、"疾病管理"等',
    application_project VARCHAR(100) COMMENT '应用项目：具体的应用项目名称',
    channel VARCHAR(100) COMMENT '所属频道：如"儿童生长发育"、"育儿健康"、"随访"、"健康小知识"等',

    -- 关联关系字段
    related_diseases VARCHAR(500) COMMENT '关联疾病：多个疾病用逗号分隔',
    related_symptoms VARCHAR(500) COMMENT '关联症状：多个症状用逗号分隔',
    related_products VARCHAR(500) COMMENT '关联商品：多个商品用逗号分隔',

    -- 其他配置
    custom_tags VARCHAR(500) COMMENT '自定义标签：多个标签用逗号分隔',
    sort_order INT DEFAULT 0 COMMENT '排序顺序：数字越小越靠前',
    is_pinned TINYINT(1) DEFAULT 0 COMMENT '是否置顶：0-否，1-是',
    remark TEXT COMMENT '备注说明',

    -- 审计字段
    operator VARCHAR(100) DEFAULT '系统管理员' COMMENT '操作人',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    -- 索引
    UNIQUE INDEX uk_content_type_id (content_type, content_id) COMMENT '内容类型和ID的联合唯一索引',
    INDEX idx_application_module (application_module),
    INDEX idx_application_project (application_project),
    INDEX idx_channel (channel),
    INDEX idx_is_pinned (is_pinned),
    INDEX idx_sort_order (sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='内容应用配置表 - 存储内容的应用配置信息';
