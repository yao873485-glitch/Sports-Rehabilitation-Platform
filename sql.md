-- auto-generated definition
create table edu_video_asset
(
    id                bigint auto_increment comment '主键ID，系统内部唯一标识'
        primary key,
    video_title       varchar(255)                                                         not null comment '视频标题：视频的显示名称，用于列表展示和搜索，如“孕期营养指导”',
    video_id          varchar(50)                                                          not null comment '视频ID：全局唯一的标识符（如系统生成ID或UUID），用于接口调用、数据追踪和外部引用',
    category_section  varchar(100)                                                         not null comment '所属板块：视频归类的业务板块，如“医教”、“康复”、“术后护理”等，便于分类管理',
    author            varchar(100)                                                         not null comment '作者：视频内容的创建者或责任编辑姓名',
    content_status    enum ('草稿', '待审核', '已上架', '未上架', '已下架') default '草稿' not null comment '内容状态：标识视频当前所处的生命周期阶段，用于权限控制与前端展示逻辑',
    publish_time      datetime                                                             null comment '发布时间：视频正式对外发布的具体时间；未发布时可为空',
    video_description text                                                                 null comment '视频描述：对视频内容的简要说明或摘要，可用于预览和SEO',
    view_count        int                                                   default 0      not null comment '播放量：记录该视频被播放的总次数，用于效果评估（注：原“阅读量”应为播放量）',
    like_count        int                                                   default 0      not null comment '点赞量：记录用户点赞的总数，反映内容受欢迎程度',
    constraint video_id
        unique (video_id)
)
    comment '宣教视频资源表 - 存储所有健康教育类视频素材的元信息、状态及统计指标' collate = utf8mb4_unicode_ci;

