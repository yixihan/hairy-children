# CREATE DATABASE IF NOT EXISTS hairy_children;

/**
  表名
  user,user_info,comment_root,comment_reply,user_collection,collection_title,title,adopt,clue
 */

/**
  用户表 -- 重要信息表, 包含注册, 登录等 重要信息
 */
CREATE TABLE IF NOT EXISTS user (
    user_id BIGINT AUTO_INCREMENT COMMENT '用户 id',
    user_name VARCHAR(20) UNIQUE not null default '' comment '用户名',
    user_password VARCHAR(255) not null default '' comment '用户密码',
    user_salt VARCHAR(10) not null default '' comment '用户密码盐',
    user_phone VARCHAR(20)  comment '用户手机',
    user_email VARCHAR(40)  comment '用户邮箱',
    gmt_create DATETIME not null comment '创建时间',
    gmt_modified DATETIME not null comment '修改时间',
    version INT not null default 1 comment '乐观锁',
    is_deleted INT not null default 0 comment '逻辑删除',

    PRIMARY KEY (user_id)
) ENGINE = INNODB AUTO_INCREMENT = 1 DeFAULT CHARSET = utf8;

/**
  用户表 -- 信息表
 */
create table if not exists user_info (
    id BIGINT auto_increment comment '主键 id',
    user_id BIGINT unique not null comment '用户 id',
    user_real_name VARCHAR(20) comment '用户真实姓名',
    user_identity_card VARCHAR(20) comment '用户身份证',
    user_address VARCHAR(100) comment '用户地址',
    address_show INT not null default 0 comment '是否展示地址 0 : 不展示, 1 : 展示',
    user_gender VARCHAR(10) comment '用户性别',
    gender_show INT not null default 0 comment '是否展示性别 0 : 不展示, 1 : 展示',
    user_birth DATETIME comment '用户生日',
    birth_show INT not null default 0 comment '是否展示生日 0 : 不展示, 1 : 展示',
    user_avatar VARCHAR(255) comment '用户头像',
    user_autograph VARCHAR(40) comment '用户个性签名',
    user_pet_cond VARCHAR(100) comment '目前养宠情况',
    gmt_create DATETIME not null comment '创建时间',
    gmt_modified DATETIME not null comment '修改时间',
    version INT not null default 1 comment '乐观锁',
    is_deleted INT not null default 0 comment '逻辑删除',

    PRIMARY KEY (id)
)  ENGINE = INNODB AUTO_INCREMENT = 1 DeFAULT CHARSET = utf8;

/**
  评论表 - 父评论
 */
create table if not exists comment_root (
    root_id BIGINT auto_increment comment '父评论 id',
    answer_id BIGINT not null comment '文章 id',
    user_id BIGINT not null comment '评论者 id',
    content TEXT not null comment '评论内容',
    like_count INT not null default 0 comment '点赞数',
    reply_count INT not null default 0 comment '回复数',
    gmt_create DATETIME not null comment '创建时间',
    gmt_modified DATETIME not null comment '修改时间',
    version INT not null default 1 comment '乐观锁',
    is_deleted INT not null default 0 comment '逻辑删除',

    PRIMARY KEY (root_id)
) ENGINE = INNODB AUTO_INCREMENT = 1 DeFAULT CHARSET = utf8;

/**
  评论表 - 子评论
 */
create table if not exists comment_reply (
    reply_id BIGINT auto_increment comment '子评论 id',
    root_id BIGINT not null comment '父评论 id',
    reply_comment_id BIGINT comment '回复的子评论 id',
    reply_user_id BIGINT comment '回复的子评论 用户 id',
    user_id BIGINT not null comment '评论者 id',
    content TEXT not null comment '评论内容',
    gmt_create DATETIME not null comment '创建时间',
    gmt_modified DATETIME not null comment '修改时间',
    version INT not null default 1 comment '乐观锁',
    is_deleted INT not null default 0 comment '逻辑删除',

    PRIMARY KEY (reply_id)
) ENGINE = INNODB AUTO_INCREMENT = 1 DeFAULT CHARSET = utf8;

/**
  收藏表 -- 收藏夹
 */
create table if not exists user_collection (
    collection_id BIGINT auto_increment comment '收藏夹 id',
    user_id BIGINT not null comment '用户 id',
    collection_name VARCHAR(25) not null comment '收藏夹名',
    collection_count INT not null default 0 comment '收藏数量',
    gmt_create DATETIME not null comment '创建时间',
    gmt_modified DATETIME not null comment '修改时间',
    version INT not null default 1 comment '乐观锁',
    is_deleted INT not null default 0 comment '逻辑删除',

    PRIMARY KEY (collection_id)
) ENGINE = INNODB AUTO_INCREMENT = 1 DeFAULT CHARSET = utf8;

/**
  收藏表 -- 收藏内容
 */
create table if not exists collection_title (
    id BIGINT auto_increment comment '主键 id',
    collection_id BIGINT not null comment '收藏夹 id',
    title_id BIGINT not null comment '文章 id',
    gmt_create DATETIME not null comment '创建时间',
    gmt_modified DATETIME not null comment '修改时间',
    version INT not null default 1 comment '乐观锁',
    is_deleted INT not null default 0 comment '逻辑删除',

    PRIMARY KEY (id)
) ENGINE = INNODB AUTO_INCREMENT = 1 DeFAULT CHARSET = utf8;

/**
  文章表 -- 领养文章/寻宠文章表
 */
create table if not exists title (
    title_id BIGINT auto_increment comment '文章 id',
    user_id BIGINT not null comment '作者 id',
    user_address VARCHAR(40) not null comment '作者 城市',
    title_type INT not null comment '文章类型, 1 : 领养, 2 : 寻宠',
    title_name VARCHAR(40) not null comment '文章标题',
    title_content TEXT not null comment '文章正文',
    title_dir TEXT comment '文章图片路径',
    collection_count INT not null default 0 comment '收藏数',
    comment_count INT not null default 0 comment '评论数',
    like_count INT not null default 0 comment '点赞数',
    is_finish INT not null default 0 comment '是否完成, 0 : 未完成, 1 : 完成',
    gmt_create DATETIME not null comment '创建时间',
    gmt_modified DATETIME not null comment '修改时间',
    version INT not null default 1 comment '乐观锁',
    is_deleted INT not null default 0 comment '逻辑删除',

    PRIMARY KEY (title_id)
) ENGINE = INNODB AUTO_INCREMENT = 1 DeFAULT CHARSET = utf8;

/**
  文章表 -- 领养表
 */
create table if not exists adopt (
    adopt_id BIGINT auto_increment comment '领养 id',
    user_id BIGINT not null comment '申请人 id',
    title_id BIGINT not null comment '文章 id',
    adopt_reason TEXT not null comment '申请理由',
    adopt_user_address VARCHAR(100) not null comment '申请人当前所在城市',
    adopt_user_phone VARCHAR(30) not null comment '申请人联系方式',
    adopt_concept VARCHAR(100) not null comment '养宠理念',
    adopt_way VARCHAR(20) not null comment '接动物方式',
    adopt_user_age INT not null comment '申请人年龄',
    adopt_dir TEXT comment '图片',
    is_return_visit INT not null default 1 comment '是否接受定期回访, 0 : 否, 1 : 是',
    is_feedback INT not null default 1 comment '是否接受定期反馈领养信息, 0 : 否, 1 : 是',
    is_success INT not null default 0 comment '是否成功, 0 : 未成功, 1 : 成功',
    gmt_create DATETIME not null comment '创建时间',
    gmt_modified DATETIME not null comment '修改时间',
    version INT not null default 1 comment '乐观锁',
    is_deleted INT not null default 0 comment '逻辑删除',

    PRIMARY KEY (adopt_id)
) ENGINE = INNODB AUTO_INCREMENT = 1 DeFAULT CHARSET = utf8;

# drop table if exists adopt;

/**
  文章表 -- 线索表
 */
create table if not exists clue (
    clue_id BIGINT auto_increment comment '线索 id',
    user_id BIGINT not null comment '提供者 id',
    title_id BIGINT not null comment '文章 id',
    clue_content TEXT not null comment '线索内容',
    clue_dir TEXT not null comment '线索图片',
    is_success INT not null default 0 comment '是否成功, 0 : 未成功, 1 : 成功',
    gmt_create DATETIME not null comment '创建时间',
    gmt_modified DATETIME not null comment '修改时间',
    version INT not null default 1 comment '乐观锁',
    is_deleted INT not null default 0 comment '逻辑删除',

    PRIMARY KEY (clue_id)
) ENGINE = INNODB AUTO_INCREMENT = 1 DeFAULT CHARSET = utf8;