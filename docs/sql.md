create table chat_message
(
    id              bigint auto_increment comment '主键ID'
        primary key,
    conversation_id varchar(100)                          not null comment '会话ID',
    sender_type     varchar(20)                           not null comment '发送人类型',
    sender_id       bigint                                not null comment '发送人ID',
    sender_name     varchar(50)                           not null comment '发送人姓名',
    receiver_id     bigint                                not null comment '接收人ID',
    content         text                                  not null comment '消息内容',
    message_type    varchar(20) default 'text'            not null comment '消息类型',
    is_read         tinyint(1)  default 0                 not null comment '是否已读',
    send_time       datetime    default CURRENT_TIMESTAMP not null comment '发送时间'
)
    collate = utf8mb4_unicode_ci;

create index idx_conversation_id
    on chat_message (conversation_id);

create index idx_is_read
    on chat_message (is_read);

create index idx_receiver_id
    on chat_message (receiver_id);

create index idx_send_time
    on chat_message (send_time);

create index idx_sender_id
    on chat_message (sender_id);

create table class_reservation_record
(
    id                    int auto_increment comment '主键ID，唯一标识每条预约记录'
        primary key,
    reservation_date      datetime                                                                          not null comment '预约日期：客户预约的具体上课时间，如2025-08-21 13:00',
    reserved_by           varchar(100)                                                                      not null comment '预约人员：发起并创建该预约的工作人员或系统用户姓名',
    classroom_name        varchar(255)                                                                      not null comment '教室名称：被预约的教室名称，如游泳教室01、普拉提教室01等',
    exercise_type         varchar(100)                                                                      not null comment '运动方式：该课程对应的运动类型，如游泳、瑜伽、普拉提等',
    patient_name          varchar(100)                                                                      not null comment '患者姓名：实际参与课程的客户姓名',
    gender                enum ('男', '女')                                                                 not null comment '性别：患者性别',
    age                   tinyint unsigned                                                                  null comment '年龄：患者当前年龄（0-255）',
    medical_record_number varchar(50)                                                                       not null comment '档案号：患者的唯一档案编号，用于身份识别和数据关联',
    reservation_status    enum ('待核销', '已核销', '已逾期', '已撤销', '已完成') default '待核销'          not null comment '预约状态：当前预约的执行状态，用于流程控制与界面展示',
    created_time          datetime                                                default CURRENT_TIMESTAMP null comment '创建时间：预约记录在系统中生成的时间'
)
    comment '课程预约记录表 - 记录每位客户对特定课程/教室的预约详情及状态流转' collate = utf8mb4_unicode_ci;

create table classroom_facility_info
(
    id                    int auto_increment comment '主键ID，唯一标识每个教室记录'
        primary key,
    exercise_type         varchar(100)                                                  not null comment '运动方式：该教室对应的运动类型，如游泳、瑜伽、普拉提、功率自行车等',
    classroom_name        varchar(255)                                                  not null comment '教室名称：具体的教室名称，如"阳光瑜伽室"、"动感单车1号厅"',
    class_duration        int unsigned                                                  null comment '单次运动时间：在该教室内进行的标准课程时长，单位为分钟，例如45、60等',
    class_schedule_config varchar(100)                                                  null comment '课时配置：课程的时间安排配置，如"上午班"、"下午班"、"全天班"等',
    reservation_limit     int unsigned                        default '20'              not null comment '预约人数上限：该教室可预约的最大人数',
    classroom_code        varchar(100)                                                  null comment '教室编码：系统自动生成的唯一教室编码，首次保存成功后自动创建',
    status                enum ('已上架', '已下架', '维护中') default '已下架'          not null comment '状态：教室当前的运营状态，控制是否可被预约或展示',
    last_operation_time   datetime                            default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '上次操作时间：最近一次对该教室进行操作（如编辑、上下架）的时间',
    created_time          datetime                            default CURRENT_TIMESTAMP null comment '创建时间：该教室记录在系统中创建的时间',
    constraint classroom_code
        unique (classroom_code)
)
    comment '教室设施信息表 - 记录各类运动教室的基本信息、容量、状态及时间戳' collate = utf8mb4_unicode_ci;

create table content_application_config
(
    id                  bigint auto_increment comment '主键ID'
        primary key,
    content_id          varchar(50)                            not null comment '内容ID：关联education_image_content或edu_video_asset的content_id/video_id',
    content_type        enum ('文章', '视频')                  not null comment '内容类型：文章或视频',
    application_module  varchar(100)                           not null comment '应用模块：如"医教"、"患教"、"健康管理"等',
    application_project varchar(100)                           not null comment '应用项目：具体的应用项目名称',
    channel             varchar(100)                           not null comment '所属频道：如"儿童生长发育"、"育儿健康"、"随访"、"健康小知识"等',
    related_diseases    varchar(500)                           null comment '关联疾病：多个疾病用逗号分隔',
    related_symptoms    varchar(500)                           null comment '关联症状：多个症状用逗号分隔',
    related_products    varchar(500)                           null comment '关联商品：多个商品用逗号分隔',
    related_departments varchar(500)                           null comment '关联科室：多个科室用逗号分隔',
    related_doctors     varchar(500)                           null comment '关联医生：多个医生用逗号分隔',
    custom_tags         varchar(500)                           null comment '自定义标签：多个标签用逗号分隔',
    sort_order          int          default 0                 null comment '排序顺序：数字越小越靠前',
    is_pinned           tinyint(1)   default 0                 null comment '是否置顶：0-否，1-是',
    operator            varchar(100) default '系统管理员'      null comment '操作人',
    created_time        datetime     default CURRENT_TIMESTAMP null comment '创建时间',
    updated_time        datetime     default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint uk_content_type_id
        unique (content_type, content_id) comment '内容类型和ID的联合唯一索引'
)
    comment '内容应用配置表 - 存储内容的应用配置信息' collate = utf8mb4_unicode_ci;

create index idx_application_module
    on content_application_config (application_module);

create index idx_application_project
    on content_application_config (application_project);

create index idx_channel
    on content_application_config (channel);

create index idx_is_pinned
    on content_application_config (is_pinned);

create index idx_sort_order
    on content_application_config (sort_order);

create table content_config_operation_log
(
    id                bigint auto_increment comment '主键ID'
        primary key,
    content_id        varchar(50)                            not null comment '内容ID',
    content_type      enum ('文章', '视频')                  not null comment '内容类型',
    operation_type    varchar(50)                            not null comment '操作类型：新增配置、修改配置、删除配置',
    operation_content text                                   null comment '操作内容：记录具体的操作详情',
    operator          varchar(100) default '系统管理员'      null comment '操作人',
    operation_time    datetime     default CURRENT_TIMESTAMP null comment '操作时间'
)
    comment '内容配置操作日志表' collate = utf8mb4_unicode_ci;

create index idx_content
    on content_config_operation_log (content_type, content_id)
    comment '内容索引';

create index idx_operation_time
    on content_config_operation_log (operation_time)
    comment '操作时间索引';

create table edu_content_repository
(
    id                  bigint auto_increment comment '主键ID，系统内部唯一标识'
        primary key,
    content_title       varchar(255)                                                                    not null comment '内容标题：图文素材的显示标题，用于列表展示和搜索',
    content_id          varchar(50)                                                                     not null comment '内容ID：全局唯一的内容编号（如系统生成ID），用于接口调用、数据追踪和外部引用',
    content_type        enum ('文章', '视频')                                 default '文章'            not null comment '内容类型：文章或视频',
    category_section    varchar(100)                                                                    not null comment '所属板块：内容所属的分类板块，如"医教"、"科普"、"康复指导"等，用于组织与筛选',
    author              varchar(100)                                                                    not null comment '作者：内容的创建者或责任编辑姓名',
    author_type         varchar(50)                                                                     null comment '作者类型：如"平台运营"等',
    tags                varchar(500)                                                                    null comment '标签：多个标签用逗号分隔，最多50个字',
    content_status      enum ('草稿', '待审核', '已上架', '未上架', '已下架') default '草稿'            not null comment '内容状态：标识当前内容所处的生命周期阶段，用于权限控制与前端展示逻辑',
    publish_time        datetime                                                                        null comment '发布时间：内容正式对外发布的具体时间，未发布时可为空',
    support_game_mode   tinyint(1)                                            default 1                 null comment '是否支持游戏模式：1-支持，0-不支持',
    is_original         tinyint(1)                                            default 1                 null comment '是否原创：1-是，0-否',
    article_description text                                                                            null comment '文章描述：对图文内容的简要说明或摘要，可用于预览展示，最多300字',
    cover_image         varchar(500)                                                                    null comment '封面图：图片URL地址，支持多张图片（逗号分隔），最多3张，单张最大370*225',
    content_source      enum ('系统录入', '外链')                             default '系统录入'        not null comment '文章来源：系统录入或外链',
    article_content     longtext                                                                        null comment '文章内容：富文本编辑器内容，仅当content_source为"系统录入"时使用',
    external_link       varchar(500)                                                                    null comment '外链地址：当content_source为"外链"时的URL地址',
    module_category     enum ('医教', '直数')                                                           null comment '所属模块：内容所属的业务模块分类',
    view_count          int                                                   default 0                 not null comment '阅读量：记录该图文被查看的总次数，用于效果评估',
    like_count          int                                                   default 0                 not null comment '点赞量：记录该图文获得的点赞总数，反映用户喜爱程度',
    view_count_base     int                                                   default 0                 null comment '阅读基数：初始阅读量基数',
    like_count_base     int                                                   default 0                 null comment '点赞基数：初始点赞量基数',
    created_at          datetime                                              default CURRENT_TIMESTAMP null comment '创建时间',
    updated_at          datetime                                              default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    created_by          varchar(100)                                                                    null comment '创建人',
    updated_by          varchar(100)                                                                    null comment '更新人',
    constraint content_id
        unique (content_id)
)
    comment '宣教图文内容资源库 - 存储所有健康教育类图文素材的元数据及状态信息' collate = utf8mb4_unicode_ci;

create table edu_video_asset
(
    id                bigint auto_increment comment '主键ID，系统内部唯一标识'
        primary key,
    content_type      enum ('视频')                                         default '视频'            not null comment '内容类型：视频',
    video_title       varchar(255)                                                                    not null comment '视频标题：视频的显示名称，用于列表展示和搜索，如"孕期营养指导"',
    video_id          varchar(50)                                                                     not null comment '视频ID：全局唯一的标识符（如系统生成ID或UUID），用于接口调用、数据追踪和外部引用',
    author            varchar(100)                                                                    null comment '作者：视频内容的创建者或责任编辑姓名',
    author_type       varchar(50)                                                                     null comment '作者类型：如"平台运营"等',
    publish_time      datetime                                                                        null comment '发布时间：视频正式对外发布的具体时间；未发布时可为空',
    support_game_mode tinyint(1)                                            default 1                 null comment '是否支持游客模式：1-支持，0-不支持',
    is_original       tinyint(1)                                            default 1                 null comment '是否原创：1-是，0-否',
    video_description text                                                                            null comment '视频描述：对视频内容的简要说明或摘要，可用于预览和SEO，最多300字',
    cover_image       varchar(500)                                                                    null comment '封面图：图片URL地址，支持多张图片（逗号分隔），最多3张，单张最大370*225',
    video_source      enum ('本地上传', '在线文件地址')                     default '本地上传'        not null comment '视频来源：本地上传或在线文件地址',
    video_url         varchar(500)                                                                    null comment '文件地址：视频文件的URL地址',
    module_category   enum ('医教', '患教')                                                           null comment '所属模块：内容所属的业务模块分类',
    like_count_base   int                                                   default 0                 null comment '点赞基数：初始点赞量基数',
    view_count_base   int                                                   default 0                 null comment '阅读基数：初始阅读量基数',
    like_count        int                                                   default 0                 not null comment '点赞量：记录用户点赞的总数，反映内容受欢迎程度',
    view_count        int                                                   default 0                 not null comment '播放量：记录该视频被播放的总次数，用于效果评估（注：原"阅读量"应为播放量）',
    content_status    enum ('草稿', '待审核', '已上架', '未上架', '已下架') default '草稿'            not null comment '内容状态：标识视频当前所处的生命周期阶段，用于权限控制与前端展示逻辑',
    operator          varchar(100)                                          default '系统管理员'      null comment '操作人',
    created_at        datetime                                              default CURRENT_TIMESTAMP null comment '创建时间',
    updated_at        datetime                                              default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint video_id
        unique (video_id)
)
    comment '宣教视频资源表 - 存储所有健康教育类视频素材的元信息、状态及统计指标' collate = utf8mb4_unicode_ci;

create table education_image_content
(
    id                  bigint auto_increment comment '主键ID'
        primary key,
    content_id          varchar(50)                            not null comment '内容ID（业务主键）',
    content_type        varchar(20)  default '文章'            null comment '内容类型',
    content_title       varchar(100)                           not null comment '文章标题',
    author              varchar(50)                            not null comment '作者',
    author_type         varchar(50)  default '平台运营'        null comment '作者类型',
    tags                varchar(200)                           null comment '文章标签',
    category_section    varchar(50)                            null comment '所属板块',
    publish_time        datetime                               null comment '发布时间',
    support_game_mode   tinyint(1)   default 1                 null comment '是否支持游客模式：0-不支持，1-支持',
    is_original         tinyint(1)   default 1                 null comment '是否原创：0-否，1-是',
    article_description text                                   null comment '文章描述',
    cover_image         varchar(500)                           null comment '封面图URL',
    content_source      varchar(20)  default '系统录入'        null comment '文章来源：系统录入、外链',
    article_content     longtext                               null comment '文章内容（富文本）',
    external_link       varchar(500)                           null comment '外链地址',
    module_category     varchar(20)  default '医教'            null comment '所属模块：医教、患教',
    like_count_base     int          default 0                 null comment '点赞基数',
    view_count_base     int          default 0                 null comment '阅读基数',
    like_count          int          default 0                 null comment '实际点赞量',
    view_count          int          default 0                 null comment '实际阅读量',
    content_status      varchar(20)  default '草稿'            null comment '内容状态：草稿、待审核、已上架、未上架、已下架',
    article_link        varchar(500)                           null comment '文章链接',
    operator            varchar(100) default '系统管理员'      null comment '操作人',
    created_time        datetime     default CURRENT_TIMESTAMP null comment '创建时间',
    updated_time        datetime     default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint content_id
        unique (content_id)
)
    comment '医教图文内容表' collate = utf8mb4_unicode_ci;

create index idx_author
    on education_image_content (author);

create index idx_category_section
    on education_image_content (category_section);

create index idx_content_id
    on education_image_content (content_id);

create index idx_content_status
    on education_image_content (content_status);

create index idx_content_title
    on education_image_content (content_title);

create index idx_publish_time
    on education_image_content (publish_time);

create table equipment
(
    id                  int auto_increment comment '主键ID'
        primary key,
    serial_number       int                                             not null comment '序号：显示设备在当前列表中的顺序编号',
    region              varchar(255)                                    not null comment '区域：显示设备所属的区域或部门',
    equipment_name      varchar(255)                                    not null comment '设备名称：显示设备的具体名称',
    equipment_type      varchar(255)                                    not null comment '设备类型：显示设备的类型分类',
    equipment_code      varchar(255)                                    not null comment '设备编号：显示设备的唯一标识编号',
    status              enum ('上架', '下架') default '上架'            not null comment '状态：显示设备当前的状态（如上架、下架）',
    last_operation_time datetime                                        null comment '上次操作时间：显示设备最近一次状态变更的时间',
    created_time        datetime              default CURRENT_TIMESTAMP null comment '创建时间：显示设备记录创建的时间',
    constraint equipment_code
        unique (equipment_code)
)
    comment '设备信息表';

create table exercise_prescription
(
    id                  int auto_increment comment '主键ID'
        primary key,
    disease_type        varchar(255)                                                    not null comment '病种类型：显示该运动处方适用的病种类型',
    exercise_method     varchar(255)                                                    not null comment '运动方式：显示具体的运动方式，如普拉提、瑜伽、慢跑等',
    exercise_type       enum ('有氧运动', '抗阻训练', '柔韧性训练', '平衡训练', '其他') not null comment '运动类型：显示运动的类型分类，如有氧运动、抗阻训练等',
    action_diagram      text                                                            null comment '动作图式：显示与该运动处方相关的动作图示或视频链接（可存储图片URL或视频URL）',
    precautions         text                                                            null comment '注意事项：显示执行该运动处方时需要注意的事项',
    progression_level   varchar(100)                                                    null comment '运动进阶：显示运动的进阶情况或难度级别，例如初级、中级、高级',
    exercise_scene      varchar(255)                                                    null comment '运动场景：显示适合执行该运动处方的场景或环境，如室内、户外、健身房等',
    status              enum ('上架', '下架') default '下架'                            not null comment '状态：显示运动处方当前的状态（如上架、下架）',
    last_operation_time datetime              default CURRENT_TIMESTAMP                 null on update CURRENT_TIMESTAMP comment '上次操作时间：显示运动处方最近一次状态变更的时间',
    created_time        datetime              default CURRENT_TIMESTAMP                 null comment '创建时间：显示运动处方记录创建的时间'
)
    comment '运动处方信息表';

create table followup_plan
(
    id                      bigint auto_increment comment '主键ID'
        primary key,
    plan_name               varchar(100)                           not null comment '计划名称',
    plan_description        text                                   not null comment '计划描述',
    linked_project_id       bigint                                 not null comment '关联的随访项目ID',
    linked_project_name     varchar(100)                           null comment '关联的随访项目名称',
    available_team          varchar(100)                           not null comment '可用团队',
    owner_institution       varchar(100)                           not null comment '所属机构',
    first_followup_baseline varchar(100)                           not null comment '首次随访基线',
    version_number          varchar(20)  default 'V1.0'            null comment '版本号',
    version_remark          varchar(500)                           null comment '版本备注',
    status                  varchar(20)  default '未发布'          null comment '状态：未发布、已发布、已停用',
    invitation_qr_code_url  varchar(500)                           null comment '随访邀请二维码URL',
    is_draft                tinyint(1)   default 0                 null comment '是否草稿：0-否，1-是',
    operator                varchar(100) default '系统'            null comment '操作人',
    created_time            datetime     default CURRENT_TIMESTAMP null comment '创建时间',
    updated_time            datetime     default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '最后修改时间'
)
    comment '随访计划主表' collate = utf8mb4_unicode_ci;

create index idx_linked_project_id
    on followup_plan (linked_project_id);

create index idx_plan_name
    on followup_plan (plan_name);

create index idx_status
    on followup_plan (status);

create table followup_plan_reminder
(
    id               bigint auto_increment comment '主键ID'
        primary key,
    plan_id          bigint                               not null comment '关联的随访计划ID',
    reminder_type    varchar(20)                          not null comment '提醒类型：early-提前提醒、overdue-逾期提醒',
    is_enabled       tinyint(1) default 0                 null comment '是否启用：0-否，1-是',
    days             int                                  null comment '提前/逾期天数',
    reminder_methods varchar(200)                         null comment '提醒方式（多个用逗号分隔）：短信,APP推送,微信',
    created_time     datetime   default CURRENT_TIMESTAMP null comment '创建时间',
    updated_time     datetime   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint followup_plan_reminder_ibfk_1
        foreign key (plan_id) references followup_plan (id)
            on delete cascade
)
    comment '随访计划提醒设置表' collate = utf8mb4_unicode_ci;

create index idx_plan_id
    on followup_plan_reminder (plan_id);

create index idx_reminder_type
    on followup_plan_reminder (reminder_type);

create table followup_plan_task
(
    id           bigint auto_increment comment '主键ID'
        primary key,
    plan_id      bigint                               not null comment '关联的随访计划ID',
    task_name    varchar(100)                         not null comment '任务名称',
    task_order   int        default 1                 null comment '任务顺序',
    execute_day  int                                  not null comment '执行时间（第几天）',
    task_content text                                 null comment '任务内容描述',
    is_enabled   tinyint(1) default 1                 null comment '是否启用：0-否，1-是',
    created_time datetime   default CURRENT_TIMESTAMP null comment '创建时间',
    updated_time datetime   default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint followup_plan_task_ibfk_1
        foreign key (plan_id) references followup_plan (id)
            on delete cascade
)
    comment '随访计划任务配置表' collate = utf8mb4_unicode_ci;

create index idx_execute_day
    on followup_plan_task (execute_day);

create index idx_plan_id
    on followup_plan_task (plan_id);

create table followup_project_config
(
    id                  bigint auto_increment comment '主键ID'
        primary key,
    project_code        varchar(20)                            not null comment '项目标号（10位随机字符串）',
    project_name        varchar(200)                           not null comment '项目名称',
    project_brief       text                                   null comment '项目简介',
    project_description text                                   null comment '项目介绍',
    disease_type        varchar(100)                           not null comment '项目病种',
    project_type        varchar(100)                           null comment '项目类型',
    consultation_times  int          default 1                 null comment '患者咨询次数',
    single_duration     int          default 1                 null comment '单次时长（小时）',
    bind_plan           varchar(500)                           null comment '绑定随访计划',
    operator            varchar(100) default '系统'            null comment '操作人',
    is_published        tinyint(1)   default 0                 null comment '是否发布：0-否，1-是',
    created_time        datetime     default CURRENT_TIMESTAMP null comment '创建时间',
    updated_time        datetime     default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint project_code
        unique (project_code)
)
    comment '随访项目配置表 - 存储随访项目的详细配置信息' collate = utf8mb4_unicode_ci;

create index idx_disease_type
    on followup_project_config (disease_type);

create index idx_project_code
    on followup_project_config (project_code);

create index idx_project_name
    on followup_project_config (project_name);

create table patient
(
    id                     int auto_increment comment '患者系统ID'
        primary key,
    name                   varchar(50)                        not null comment '患者姓名',
    gender                 enum ('男', '女')                  not null comment '性别',
    birth_date             date                               not null comment '出生日期',
    id_card                varchar(100)                       null comment '证件号码',
    phone                  varchar(20)                        null comment '联系电话',
    medical_record_no      varchar(50)                        not null comment '档案号（业务主键）',
    disease_type           varchar(100)                       not null comment '主要病种',
    enrollment_institution varchar(100)                       not null comment '入组机构',
    address                text                               null comment '居住地址',
    created_time           datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updated_time           datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '最后修改时间',
    constraint medical_record_no
        unique (medical_record_no),
    constraint phone
        unique (phone)
)
    comment '患者基本信息表' collate = utf8mb4_unicode_ci;

create table disease_record
(
    id                         bigint auto_increment comment '专病档案ID'
        primary key,
    patient_id                 int                                not null comment '关联患者ID',
    lpa_min                    decimal(10, 2)                     null comment 'LPA（min）',
    mpa_min                    decimal(10, 2)                     null comment 'MPA（min）',
    vpa_min                    decimal(10, 2)                     null comment 'VPA（min）',
    height_cm                  decimal(10, 2)                     null comment '身高（cm）',
    weight_kg                  decimal(10, 2)                     null comment '体重（kg）',
    bmi                        decimal(10, 2)                     null comment 'BMI（kg/m2）',
    waist_cm                   decimal(10, 2)                     null comment '腰围（cm）',
    hip_cm                     decimal(10, 2)                     null comment '臀围（cm）',
    waist_hip_ratio            decimal(10, 2)                     null comment '腰臀比',
    disease_history_list       json                               null comment '疾病史列表（JSON数组）',
    disease_history_detail     text                               null comment '疾病史详情说明',
    family_history_list        json                               null comment '家族史列表（JSON数组）',
    family_history_detail      text                               null comment '家族史详情说明',
    fasting_glucose            decimal(10, 2)                     null comment '空腹血糖（mmol/L）',
    total_cholesterol          decimal(10, 2)                     null comment '总胆固醇（mmol/L）',
    hdl_cholesterol            decimal(10, 2)                     null comment '高密度脂蛋白（mmol/L）',
    ldl_cholesterol            decimal(10, 2)                     null comment '低密度脂蛋白（mmol/L）',
    blood_pressure             varchar(50)                        null comment '血压（mmHg）',
    heart_rate                 int                                null comment '心率（次/分）',
    sedentary                  varchar(10)                        null comment '静坐少动：无/有',
    smoking                    varchar(10)                        null comment '吸烟：无/有',
    drinking                   varchar(10)                        null comment '饮酒：无/有',
    insufficient_sunlight      varchar(10)                        null comment '阳光照射不足：无/有',
    excessive_caffeine         varchar(10)                        null comment '过量含咖啡因的饮料：无/有',
    protein_intake             varchar(20)                        null comment '蛋白质摄入量：过多/正常/不足',
    high_calcium_diet          varchar(10)                        null comment '高钙饮食：无/有',
    calcium_vd_deficiency      varchar(10)                        null comment '钙和/或维生素D缺乏：无/有',
    medication_list            json                               null comment '药物使用列表（JSON数组）',
    exercise_risk_level        varchar(20)                        null comment '运动风险分级：低/中/高',
    fracture_history           varchar(10)                        null comment '骨折：无/有',
    fracture_detail            text                               null comment '骨折详情说明',
    blood_routine_status       varchar(20)                        null comment '血常规：正常/异常',
    blood_routine_detail       text                               null comment '血常规异常详情',
    urine_routine_status       varchar(20)                        null comment '尿常规：正常/异常',
    urine_routine_detail       text                               null comment '尿常规异常详情',
    liver_function_status      varchar(20)                        null comment '肝功能：正常/异常',
    liver_function_detail      text                               null comment '肝功能异常详情',
    kidney_function_status     varchar(20)                        null comment '肾功能：正常/异常',
    kidney_function_detail     text                               null comment '肾功能异常详情',
    blood_lipid_status         varchar(20)                        null comment '血脂：正常/异常',
    blood_lipid_detail         text                               null comment '血脂异常详情',
    hba1c_status               varchar(20)                        null comment '糖化血红蛋白：正常/异常',
    hba1c_detail               text                               null comment '糖化血红蛋白异常详情',
    fasting_glucose_status     varchar(20)                        null comment '空腹血糖：正常/异常',
    fasting_glucose_detail     text                               null comment '空腹血糖异常详情',
    pancreatic_function_status varchar(20)                        null comment '胰岛功能：正常/异常',
    pancreatic_function_detail text                               null comment '胰岛功能异常详情',
    c_peptide_status           varchar(20)                        null comment '胰岛功能/C肽：正常/异常',
    c_peptide_detail           text                               null comment 'C肽异常详情',
    vitamin_d_status           varchar(20)                        null comment '维生素D：正常/异常',
    vitamin_d_detail           text                               null comment '维生素D异常详情',
    d_dimer_status             varchar(20)                        null comment 'D二聚体：正常/异常',
    d_dimer_detail             text                               null comment 'D二聚体异常详情',
    ultrasound_uterus_status   varchar(20)                        null comment '彩超-子宫附件：正常/异常',
    ultrasound_uterus_detail   text                               null comment '子宫附件异常详情',
    ultrasound_breast_status   varchar(20)                        null comment '彩超-乳腺：正常/异常',
    ultrasound_breast_detail   text                               null comment '乳腺异常详情',
    ultrasound_liver_status    varchar(20)                        null comment '彩超-肝胆胰脾：正常/异常',
    ultrasound_liver_detail    text                               null comment '肝胆胰脾异常详情',
    bone_density_status        varchar(20)                        null comment '骨密度：正常/异常',
    bone_density_detail        text                               null comment '骨密度异常详情',
    ct_abdominal_fat_status    varchar(20)                        null comment 'CT腹部脂肪容积：正常/异常',
    ct_abdominal_fat_detail    text                               null comment 'CT腹部脂肪容积异常详情',
    max_heart_rate             int                                null comment '最大心率',
    six_mwt                    decimal(10, 2)                     null comment '6MWT',
    vo2_max                    decimal(10, 2)                     null comment 'VO2max',
    upper_limb_1rm             decimal(10, 2)                     null comment '上肢1-RM',
    lower_limb_1rm             decimal(10, 2)                     null comment '下肢1-RM',
    upper_limb_endurance       int                                null comment '上肢（次）',
    lower_limb_endurance       int                                null comment '下肢（次）',
    sit_reach_cm               decimal(10, 2)                     null comment '坐位体前屈（cm）',
    balance_left_eye_closed    decimal(10, 2)                     null comment '单推闭眼站立(左)(s)',
    balance_right_eye_closed   decimal(10, 2)                     null comment '单推闭眼站立(右)(s)',
    created_at                 datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_at                 datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint uk_patient_id
        unique (patient_id),
    constraint fk_disease_record_patient
        foreign key (patient_id) references patient (id)
            on update cascade on delete cascade
)
    comment '专病档案表（运动康复）' collate = utf8mb4_unicode_ci;

create table enrollment_assessment
(
    id              bigint auto_increment comment '评估ID'
        primary key,
    patient_id      int                                not null comment '关联患者ID',
    general_q1      varchar(10)                        null comment '一般健康问题1：您是否被医生诊断患有心脏疾病？',
    general_q2      varchar(10)                        null comment '一般健康问题2：您在休息或日常活动中是否感到胸部疼痛？',
    general_q3      varchar(10)                        null comment '一般健康问题3：在过去的一个月中，您是否在没有进行体力活动时感到胸部疼痛？',
    general_q4      varchar(10)                        null comment '一般健康问题4：您是否因为头晕而失去平衡，或者曾经失去知觉？',
    general_q5      varchar(10)                        null comment '一般健康问题5：您是否有骨骼或关节问题，可能会因为体力活动的改变而恶化？',
    general_q6      varchar(10)                        null comment '一般健康问题6：您目前是否正在服用治疗血压或心脏疾病的药物？',
    general_q7      varchar(10)                        null comment '一般健康问题7：您是否知道有任何其他原因不应进行体力活动？',
    medical_q1      varchar(10)                        null comment '医疗问题1：您现在是否怀孕或在过去3个月内分娩？',
    medical_q1a     varchar(10)                        null comment '医疗问题1a：您现在是否处于产后6周以内？',
    medical_q1b     varchar(10)                        null comment '医疗问题1b：您是否有妊娠并发症？',
    medical_q1c     text                               null comment '医疗问题1c：如果是，请具体说明',
    medical_q2      varchar(10)                        null comment '医疗问题2：您是否患有心脏疾病、心血管疾病或脑血管疾病？',
    medical_q2a     varchar(10)                        null comment '医疗问题2a：您是否已完成心脏康复计划？',
    medical_q2b     varchar(10)                        null comment '医疗问题2b：您是否已获得运动许可？',
    medical_q3      varchar(10)                        null comment '医疗问题3：您是否患有高血压？',
    medical_q3a     varchar(10)                        null comment '医疗问题3a：您目前是否正在服用治疗高血压的药物？',
    medical_q3b     varchar(100)                       null comment '医疗问题3b：您的静息血压是多少？',
    medical_q3c     varchar(10)                        null comment '医疗问题3c：您的血压是否超过 160/90 mmHg？',
    medical_q3d     varchar(10)                        null comment '医疗问题3d：您是否已获得运动许可？',
    medical_q4      varchar(10)                        null comment '医疗问题4：您是否有任何代谢疾病？',
    medical_q4a     text                               null comment '医疗问题4a：请列出您的代谢疾病',
    medical_q4b     varchar(10)                        null comment '医疗问题4b：您是否已获得运动许可？',
    medical_q5      varchar(10)                        null comment '医疗问题5：您是否有任何精神健康问题或学习困难？',
    medical_q5a     text                               null comment '医疗问题5a：您是否正在接受治疗或正在服用药物？',
    medical_q5b     text                               null comment '医疗问题5b：您的疾病或用药是否影响您的运动能力？',
    medical_q5c     text                               null comment '医疗问题5c：您是否已获得运动许可？',
    medical_q5d     text                               null comment '医疗问题5d：请具体说明',
    medical_q5e     text                               null comment '医疗问题5e：其他相关信息',
    medical_q6      varchar(10)                        null comment '医疗问题6：您是否患有任何呼吸系统疾病？',
    medical_q6a     text                               null comment '医疗问题6a：请具体说明您的呼吸系统疾病',
    medical_q6b     varchar(10)                        null comment '医疗问题6b：您是否已获得运动许可？',
    medical_q7      varchar(10)                        null comment '医疗问题7：您是否有任何肌肉、骨骼或关节问题？',
    medical_q7a     text                               null comment '医疗问题7a：请具体说明您的问题',
    medical_q7b     varchar(10)                        null comment '医疗问题7b：您的问题是否影响您进行体力活动？',
    medical_q7c     varchar(10)                        null comment '医疗问题7c：您是否已完成康复治疗？',
    medical_q7d     varchar(10)                        null comment '医疗问题7d：您是否已获得运动许可？',
    medical_q8      varchar(10)                        null comment '医疗问题8：您是否有任何其他慢性疾病？',
    medical_q8a     text                               null comment '医疗问题8a：请列出您的慢性疾病',
    medical_q8b     varchar(10)                        null comment '医疗问题8b：您的疾病是否得到控制？',
    medical_q8c     varchar(10)                        null comment '医疗问题8c：您是否已获得运动许可？',
    medical_q9      varchar(10)                        null comment '医疗问题9：您目前是否正在服用任何药物？',
    medical_q9a     text                               null comment '医疗问题9a：请列出所有药物',
    medical_q9b     varchar(10)                        null comment '医疗问题9b：这些药物是否影响您的运动能力？',
    medical_q9c     text                               null comment '医疗问题9c：如果是，请具体说明',
    medical_q10     varchar(10)                        null comment '医疗问题10：您是否有任何其他健康问题？',
    medical_q10a    text                               null comment '医疗问题10a：请具体说明',
    medical_q10b    varchar(10)                        null comment '医疗问题10b：这些问题是否影响您的运动能力？',
    medical_q10c    varchar(10)                        null comment '医疗问题10c：您是否已获得运动许可？',
    recommendation1 text                               null comment '运动建议1：推荐的运动类型和强度',
    recommendation2 text                               null comment '运动建议2：注意事项和限制',
    recommendation3 text                               null comment '运动建议3：后续随访计划',
    assessor_name   varchar(50)                        null comment '评估人员姓名',
    assessment_date datetime                           null comment '评估日期',
    created_at      datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updated_at      datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint uk_patient_id
        unique (patient_id),
    constraint fk_enrollment_assessment_patient
        foreign key (patient_id) references patient (id)
            on update cascade on delete cascade
)
    comment '入组评估表（运动前健康评估问卷）' collate = utf8mb4_unicode_ci;

create table followup_patient_list
(
    id                 bigint auto_increment comment '主键ID'
        primary key,
    patient_id         int                                                                               not null comment '患者ID：外键关联patient表',
    source             varchar(50)                                             default '医生推荐'        null comment '来源：医生推荐、患者自主申请、系统导入等',
    application_time   datetime                                                                          null comment '申请时间：患者提交随访申请的时间',
    enrollment_time    datetime                                                                          null comment '加入项目时间：患者正式被纳入随访项目的时间',
    primary_doctor     varchar(100)                                                                      null comment '主负责医生：直接管理该患者的主要医生姓名',
    health_manager     varchar(100)                                                                      null comment '健管师：协助管理的健康管理师姓名',
    followup_team      varchar(200)                                                                      null comment '随访团队：负责该患者的医疗团队名称',
    followup_plan_name varchar(200)                                                                      null comment '随访计划名称：关联的随访计划名称，未开始随访时为空',
    followup_plan_id   bigint                                                                            null comment '随访计划ID：外键关联followup_plan表，未开始随访时为空',
    followup_project   varchar(200)                                                                      null comment '随访项目：随访项目名称，未开始随访时为空',
    followup_status    enum ('待入组', '随访中', '已暂停', '已完成', '已退出') default '待入组'          null comment '随访状态：当前患者在随访流程中的状态',
    remark             text                                                                              null comment '备注：记录相关说明',
    created_time       datetime                                                default CURRENT_TIMESTAMP null comment '创建时间',
    updated_time       datetime                                                default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    created_by         varchar(100)                                                                      null comment '创建人',
    updated_by         varchar(100)                                                                      null comment '更新人',
    constraint followup_patient_list_ibfk_1
        foreign key (patient_id) references patient (id)
            on update cascade on delete cascade,
    constraint followup_patient_list_ibfk_2
        foreign key (followup_plan_id) references followup_plan (id)
            on update cascade on delete set null
)
    comment '随访患者列表表 - 记录患者的随访信息，开始随访后关联随访计划' collate = utf8mb4_unicode_ci;

create index idx_application_time
    on followup_patient_list (application_time);

create index idx_followup_plan_id
    on followup_patient_list (followup_plan_id);

create index idx_followup_status
    on followup_patient_list (followup_status);

create index idx_patient_id
    on followup_patient_list (patient_id);

create index idx_primary_doctor
    on followup_patient_list (primary_doctor);

create table health_record
(
    id                         bigint auto_increment comment '健康档案ID'
        primary key,
    patient_id                 int                                   not null comment '关联患者ID',
    work_unit                  varchar(200)                          null comment '工作单位/学校',
    native_place               varchar(100)                          null comment '籍贯',
    birth_place                varchar(100)                          null comment '出生地',
    emergency_contact_name     varchar(50)                           null comment '紧急联系人姓名',
    emergency_contact_relation varchar(50)                           null comment '与本人关系',
    emergency_contact_phone    varchar(20)                           null comment '紧急联系人电话',
    residence_type             varchar(20)                           null comment '常驻类型：户籍/非户籍',
    household_address          text                                  null comment '户籍地址',
    residence_address          text                                  null comment '常驻地址',
    education_level            varchar(50)                           null comment '文化程度',
    marital_status             varchar(20)                           null comment '婚姻状况：未婚/已婚/丧偶',
    medical_payment_method     varchar(100)                          null comment '医疗费用支付方式',
    allergy_list               json                                  null comment '过敏史列表（JSON数组）',
    allergy_detail             text                                  null comment '过敏详情说明',
    exposure_list              json                                  null comment '暴露史列表（JSON数组）',
    disease_history            varchar(10) default '无'              null comment '既往病史：无/有',
    disease_detail             text                                  null comment '既往病史详情',
    surgery_history            varchar(10) default '无'              null comment '手术史：无/有',
    surgery_detail             text                                  null comment '手术史详情',
    trauma_history             varchar(10) default '无'              null comment '外伤史：无/有',
    trauma_detail              text                                  null comment '外伤史详情',
    transfusion_history        varchar(10) default '无'              null comment '输血史：无/有',
    transfusion_detail         text                                  null comment '输血史详情',
    vaccination_history        varchar(10) default '无'              null comment '预防接种史：无/有',
    vaccination_detail         text                                  null comment '预防接种详情',
    father_disease_list        json                                  null comment '父亲疾病列表（JSON数组）',
    father_detail              text                                  null comment '父亲疾病详情',
    mother_disease_list        json                                  null comment '母亲疾病列表（JSON数组）',
    mother_detail              text                                  null comment '母亲疾病详情',
    siblings_disease_list      json                                  null comment '兄弟姐妹疾病列表（JSON数组）',
    siblings_detail            text                                  null comment '兄弟姐妹疾病详情',
    children_disease_list      json                                  null comment '子女疾病列表（JSON数组）',
    children_detail            text                                  null comment '子女疾病详情',
    genetic_disease_history    varchar(10) default '无'              null comment '遗传病史：无/有',
    genetic_disease_detail     text                                  null comment '遗传病详情',
    disability_list            json                                  null comment '残疾情况列表（JSON数组）',
    disability_detail          text                                  null comment '残疾详情说明',
    health_identity            varchar(50)                           null comment '个人健康标识：0-6岁儿童/65岁及以上老年人/孕产妇',
    pregnancy_risk_level       varchar(20)                           null comment '孕产妇风险等级：低风险/一般风险/较高风险/高风险',
    chronic_disease_list       json                                  null comment '慢性/重点疾病列表（JSON数组）',
    infectious_disease_list    json                                  null comment '法定传染病列表（JSON数组）',
    infectious_disease_detail  text                                  null comment '法定传染病详情',
    weight_status              varchar(20)                           null comment '体重情况：低/正常/超重/肥胖',
    blood_type_abo             varchar(10)                           null comment 'ABO血型：A/B/O/AB',
    blood_type_rh              varchar(20)                           null comment 'Rh血型：Rh阴性/Rh阳性/不详',
    created_at                 datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_at                 datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint uk_patient_id
        unique (patient_id),
    constraint fk_health_record_patient
        foreign key (patient_id) references patient (id)
            on update cascade on delete cascade
)
    comment '健康档案表（优化版：移除与patient/patient_detail重复字段）' collate = utf8mb4_unicode_ci;

create table medical_scheme
(
    id          bigint auto_increment comment '方案系统ID'
        primary key,
    patient_id  int                                not null comment '患者ID',
    scheme_id   varchar(100)                       not null comment '方案编号（业务唯一）',
    doctor_name varchar(50)                        not null comment '开方医生',
    status      tinyint  default 1                 not null comment '状态：1-执行中，2-已结束',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    end_time    datetime                           null comment '结束时间',
    create_date date                               not null comment '开方日期（用于查询）',
    constraint scheme_id
        unique (scheme_id),
    constraint fk_medical_scheme_patient
        foreign key (patient_id) references patient (id)
            on delete cascade
)
    comment '医疗方案主表';

create index idx_create_date
    on medical_scheme (create_date);

create index idx_doctor_name
    on medical_scheme (doctor_name);

create index idx_patient_id
    on medical_scheme (patient_id);

create index idx_scheme_id
    on medical_scheme (scheme_id);

create index idx_status
    on medical_scheme (status);

create index idx_enrollment_institution
    on patient (enrollment_institution);

create index idx_medical_record_no
    on patient (medical_record_no);

create index idx_name
    on patient (name);

create index idx_phone
    on patient (phone);

create table patient_assessment_plan
(
    id                    int auto_increment comment '主键ID'
        primary key,
    patient_id            int                                   not null comment '患者ID',
    medical_scheme_id     bigint                                null comment '所属医疗方案ID（可选）',
    assessment_name       varchar(255)                          not null comment '评定方案名称',
    execution_institution varchar(255)                          null comment '执行机构',
    status                tinyint     default 1                 not null comment '状态：1-已创建, 2-执行中, 3-已完成, 4-已结束',
    prescribing_doctor    varchar(100)                          not null comment '开方医生',
    created_time          datetime    default CURRENT_TIMESTAMP null,
    updated_time          datetime    default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    execution_type        varchar(20) default '机构'            not null comment '执行场景：机构、居家',
    scale_name            varchar(255)                          not null comment '量表名称',
    execution_frequency   varchar(50)                           not null comment '执行频率，如：3次/周',
    cycle_duration        int         default 0                 not null comment '执行周期长度（周），0表示无周期',
    total_times           int         default 1                 not null comment '总次数（次），整数',
    remarks               text                                  null comment '备注，选填',
    constraint fk_assessment_plan_patient
        foreign key (patient_id) references patient (id)
            on delete cascade,
    constraint fk_assessment_plan_scheme
        foreign key (medical_scheme_id) references medical_scheme (id)
            on delete set null
)
    comment '患者评定方案表';

create table assessment_execution_detail
(
    id                    bigint auto_increment comment '主键ID'
        primary key,
    assessment_plan_id    int                                not null comment '评定方案ID',
    patient_id            int                                not null comment '患者ID',
    record_date           date                               not null comment '记录日期',
    question1_answer      varchar(500)                       null comment '问题1答案：最近7天内，您有几天做了剧烈的体育活动',
    question1_skip_reason varchar(100)                       null comment '问题1跳过原因：无相关体育活动',
    question2_answer      varchar(500)                       null comment '问题2答案：在这其中一天您通常会花多少时间在剧烈的体育活动上',
    question2_unknown     tinyint  default 0                 null comment '问题2不知道或不确定：0-否，1-是',
    question3_answer      varchar(500)                       null comment '问题3答案：最近7天内，您有几天做了这度的体育活动',
    question3_skip_reason varchar(100)                       null comment '问题3跳过原因：无适度体育活动',
    question4_answer      varchar(500)                       null comment '问题4答案：在这其中一天您通常会花多少时间在适度的体育活动上',
    question4_unknown     tinyint  default 0                 null comment '问题4不知道或不确定：0-否，1-是',
    question5_answer      varchar(500)                       null comment '问题5答案：最近7天内，您有几天是步行',
    question5_skip_reason varchar(100)                       null comment '问题5跳过原因：没有步行',
    question6_answer      varchar(500)                       null comment '问题6答案：在这其中一天您通常会花多少时间在步行上',
    question6_unknown     tinyint  default 0                 null comment '问题6不知道或不确定：0-否，1-是',
    question7_answer      varchar(500)                       null comment '问题7答案：最近七天内，工作日您有多久时间是坐着的',
    question7_unknown     tinyint  default 0                 null comment '问题7不知道或不确定：0-否，1-是',
    executor              varchar(100)                       not null comment '执行人员',
    execution_time        datetime                           not null comment '执行时间',
    status                tinyint  default 1                 not null comment '状态：1-暂存，2-已确定',
    created_time          datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updated_time          datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint fk_execution_detail_patient
        foreign key (patient_id) references patient (id)
            on delete cascade,
    constraint fk_execution_detail_plan
        foreign key (assessment_plan_id) references patient_assessment_plan (id)
            on delete cascade
)
    comment '评估执行详细记录表（IPAQ问卷）';

create index idx_assessment_plan_id
    on assessment_execution_detail (assessment_plan_id);

create index idx_patient_id
    on assessment_execution_detail (patient_id);

create index idx_record_date
    on assessment_execution_detail (record_date);

create index idx_status
    on assessment_execution_detail (status);

create table assessment_execution_record
(
    id                    int auto_increment comment '主键ID'
        primary key,
    patient_id            int                                not null comment '患者ID',
    assessment_plan_id    int                                not null comment '对应评定方案ID',
    execution_institution varchar(255)                       null comment '执行机构',
    status                tinyint  default 1                 not null comment '状态：1-已创建, 2-执行中, 3-已完成, 4-已结束',
    prescribing_doctor    varchar(100)                       not null comment '开方医生',
    created_time          datetime default CURRENT_TIMESTAMP null,
    updated_time          datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    constraint fk_execution_record_patient
        foreign key (patient_id) references patient (id)
            on delete cascade,
    constraint fk_execution_record_plan
        foreign key (assessment_plan_id) references patient_assessment_plan (id)
            on delete cascade
)
    comment '评定执行记录表';

create index idx_patient_id
    on assessment_execution_record (patient_id);

create index idx_plan_id
    on assessment_execution_record (assessment_plan_id);

create index idx_status
    on assessment_execution_record (status);

create index idx_patient_id
    on patient_assessment_plan (patient_id);

create index idx_scheme_id
    on patient_assessment_plan (medical_scheme_id);

create index idx_status
    on patient_assessment_plan (status);

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
    diagnosis                      varchar(200) null comment '诊断信息',
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

create table patient_enrollment_audit
(
    id            int auto_increment comment '审核记录ID'
        primary key,
    patient_id    int                                  not null comment '关联患者ID',
    status        tinyint(1) default 0                 not null comment '状态：0-待审核，1-审核通过，2-审核拒绝',
    reject_reason varchar(200)                         null comment '拒绝原因',
    created_time  datetime   default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_time  datetime   default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint uk_patient_enrollment
        unique (patient_id),
    constraint fk_patient_enrollment_audit
        foreign key (patient_id) references patient (id)
            on update cascade on delete cascade
)
    comment '患者入组审核表' collate = utf8mb4_unicode_ci;

create table patient_exercise_prescription
(
    id                    int auto_increment comment '主键ID'
        primary key,
    patient_id            int                                not null comment '患者ID',
    medical_scheme_id     bigint                             null comment '所属医疗方案ID（可选）',
    prescription_name     varchar(255)                       not null comment '运动处方名称',
    execution_institution varchar(255)                       null comment '执行机构',
    prescription_status   tinyint  default 1                 not null comment '状态：1-已创建, 2-执行中, 3-已完成, 4-已结束',
    prescribing_doctor    varchar(100)                       not null comment '开方医生',
    created_time          datetime default CURRENT_TIMESTAMP null,
    updated_time          datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    constraint fk_exer_presc_patient
        foreign key (patient_id) references patient (id)
            on delete cascade,
    constraint fk_exer_presc_scheme
        foreign key (medical_scheme_id) references medical_scheme (id)
            on delete set null
)
    comment '患者运动处方表（主表）';

create index idx_created_time
    on patient_exercise_prescription (created_time);

create index idx_patient_id
    on patient_exercise_prescription (patient_id);

create index idx_scheme_id
    on patient_exercise_prescription (medical_scheme_id);

create index idx_status
    on patient_exercise_prescription (prescription_status);

create table patient_prescribed_exercise
(
    id                       int auto_increment comment '主键ID'
        primary key,
    patient_id               int                                   not null comment '患者ID',
    exercise_prescription_id int                                   not null comment '对应运动处方ID',
    execution_institution    varchar(255)                          null comment '执行机构',
    prescription_status      tinyint     default 1                 not null comment '状态：1-已创建, 2-执行中, 3-已完成, 4-已结束',
    prescribing_doctor       varchar(100)                          not null comment '开方医生',
    created_time             datetime    default CURRENT_TIMESTAMP null,
    updated_time             datetime    default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    exercise_scene           varchar(20) default '机构'            not null comment '运动场景：机构/居家',
    exercise_type            varchar(20)                           not null comment '运动类型：如 有氧运动、呼吸训练、平衡训练、抗阻训练、柔韧性训练',
    exercise_method          varchar(100)                          not null comment '运动方式：如 单腿站立（扶椅背）、直线行走、弹力带训练等',
    precautions              text                                  null comment '注意事项：运动过程中的安全提示或个体化建议',
    duration_minutes         int         default 0                 not null comment '单次运动时间（分钟）',
    exercise_frequency       varchar(20) default '1次/周'          not null comment '运动频率：如 1次/周, 3次/周, 7次/周 等',
    exercise_intensity       varchar(20) default '中等'            not null comment '运动强度：高强度、中等、低强度',
    cycle_weeks              int         default 1                 not null comment '运动周期（周）',
    total_sessions           int         default 1                 not null comment '计划总次数（次）',
    weekly_total_minutes     int         default 0                 not null comment '每周建议累计运动时长（分钟）',
    progression              text                                  null comment '运动进阶要求描述',
    remarks                  text                                  null comment '补充说明，如调整依据、特殊情况等',
    exercise_image           varchar(255)                          null comment '运动图示图片路径',
    constraint fk_prescribed_exer_patient
        foreign key (patient_id) references patient (id)
            on delete cascade,
    constraint fk_prescribed_exer_presc
        foreign key (exercise_prescription_id) references patient_exercise_prescription (id)
            on delete cascade
)
    comment '患者运动处方执行记录表';

create index idx_patient_id
    on patient_prescribed_exercise (patient_id);

create index idx_prescription_id
    on patient_prescribed_exercise (exercise_prescription_id);

create index idx_status
    on patient_prescribed_exercise (prescription_status);

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

create table prescription_execution_record
(
    id               int auto_increment comment '主键ID'
        primary key,
    patient_id       int                                not null comment '患者ID',
    prescription_id  int                                not null comment '对应运动处方ID',
    exercise_id      int                                null comment '运动项目ID（单个执行时有值，批量执行时为NULL）',
    execution_count  int      default 1                 not null comment '执行次数',
    executor         varchar(100)                       not null comment '执行人员',
    start_time       datetime                           not null comment '开始时间',
    end_time         datetime                           not null comment '结束时间',
    execution_record text                               null comment '执行记录',
    status           tinyint  default 3                 not null comment '状态：1-已创建, 2-执行中, 3-已完成, 4-已结束',
    created_time     datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updated_time     datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint fk_prescription_exec_patient
        foreign key (patient_id) references patient (id)
            on delete cascade,
    constraint fk_prescription_exec_prescription
        foreign key (prescription_id) references patient_exercise_prescription (id)
            on delete cascade
)
    comment '处方执行记录表';

create index idx_exercise_id
    on prescription_execution_record (exercise_id);

create index idx_patient_id
    on prescription_execution_record (patient_id);

create index idx_prescription_id
    on prescription_execution_record (prescription_id);

create index idx_start_time
    on prescription_execution_record (start_time);

create index idx_status
    on prescription_execution_record (status);

create table registration_application
(
    id              bigint auto_increment comment '申请ID'
        primary key,
    username        varchar(50)                           not null comment '账户用户名',
    phone           varchar(20)                           not null comment '联系电话',
    gender          varchar(10)                           not null comment '性别',
    birth_date      date                                  not null comment '出生日期',
    platform        varchar(50) default '体医融合平台'    not null comment '所在平台',
    organization    varchar(100)                          not null comment '所在机构',
    apply_role      varchar(50)                           not null comment '申请角色',
    certificate_url varchar(500)                          not null comment '证件URL',
    status          tinyint(1)  default 0                 not null comment '审核状态：0-待审核，1-已通过，2-已拒绝',
    password        varchar(100)                          not null comment '密码（加密存储）',
    reject_reason   varchar(500)                          null comment '拒绝原因',
    审核时间        datetime                              null comment '审核时间',
    审核人          bigint                                null comment '审核人ID',
    create_time     datetime    default CURRENT_TIMESTAMP not null comment '申请时间'
)
    comment '注册申请表' collate = utf8mb4_unicode_ci;

create index idx_phone
    on registration_application (phone);

create index idx_status
    on registration_application (status);

create table scheme_cycle_config
(
    id           bigint auto_increment comment '主键ID'
        primary key,
    patient_id   int                                not null comment '关联患者ID',
    scheme_id    varchar(100)                       null comment '方案编号（如果已生成方案）',
    cycle_months int                                not null comment '方案周期（月数：1-12）',
    start_date   date                               null comment '方案开始日期',
    end_date     date                               null comment '方案结束日期（根据开始日期+周期月数计算）',
    status       tinyint  default 1                 null comment '状态：1-有效，0-已失效',
    created_by   varchar(50)                        null comment '创建人（医生姓名）',
    created_at   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updated_at   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    remarks      text                               null comment '备注',
    constraint uk_patient_scheme
        unique (patient_id, scheme_id),
    constraint fk_scheme_cycle_patient
        foreign key (patient_id) references patient (id)
            on update cascade on delete cascade
)
    comment '方案周期配置表' collate = utf8mb4_unicode_ci;

create index idx_end_date
    on scheme_cycle_config (end_date);

create index idx_patient_id
    on scheme_cycle_config (patient_id);

create index idx_scheme_id
    on scheme_cycle_config (scheme_id);

create index idx_start_date
    on scheme_cycle_config (start_date);

create index idx_status
    on scheme_cycle_config (status);

create table shared_file
(
    id             bigint auto_increment
        primary key,
    patient_id     bigint                                                                   not null,
    patient_name   varchar(100)                                                             not null,
    file_name      varchar(255)                                                             not null,
    file_type      enum ('image', 'pdf', 'doc', 'excel', 'other') default 'other'           null,
    file_url       varchar(500)                                                             not null comment '文件在OSS/S3中的URL',
    upload_by      varchar(100)                                                             not null comment '上传人姓名',
    upload_time    datetime                                       default CURRENT_TIMESTAMP null,
    download_count int                                            default 0                 null
)
    comment '医患共享文件表';

create index idx_file_type
    on shared_file (file_type);

create index idx_patient
    on shared_file (patient_id);

create index idx_upload_time
    on shared_file (upload_time);

create table user
(
    id              bigint auto_increment comment '用户ID'
        primary key,
    username        varchar(50)                           not null comment '用户名',
    phone           varchar(20)                           not null comment '手机号码',
    password        varchar(100)                          not null comment '密码（加密存储）',
    gender          varchar(10)                           null comment '性别：男、女',
    birth_date      date                                  null comment '出生日期',
    platform        varchar(50) default '体医融合平台'    null comment '所在平台',
    organization    varchar(100)                          null comment '所在机构',
    role            varchar(50)                           null comment '角色',
    certificate_url varchar(500)                          null comment '证件URL（运动处方培训合格证书）',
    status          tinyint(1)  default 0                 not null comment '状态：0-待审核，1-已通过，2-已拒绝',
    create_time     datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time     datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint uk_phone
        unique (phone),
    constraint uk_username
        unique (username)
)
    comment '用户表' collate = utf8mb4_unicode_ci;

create index idx_status
    on user (status);

