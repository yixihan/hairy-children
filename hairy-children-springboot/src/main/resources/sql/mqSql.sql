drop table if exists adopt_mailbox;
create table if not exists adopt_mailbox (
    id BIGINT auto_increment comment '主键 id',
    adopt_id BIGINT not null comment '领养 id',
    title_id BIGINT not null comment '文章 id',
    send_user_id BIGINT not null comment '发送者 id',
    receive_user_id BIGINT not null comment '接受者 id',
    is_read INT not null default 0 comment '是否已阅读, 0 : 否, 1 :是',
    gmt_create DATETIME not null comment '创建时间',
    gmt_modified DATETIME NOT null comment '修改时间',
    version INT not null default 1 comment '乐观锁',
    is_deleted INT not null default 0 comment '逻辑删除',

    PRIMARY KEY (id)
) ENGINE = INNODB AUTO_INCREMENT = 1 DeFAULT CHARSET = utf8;

drop table if exists clue_mailbox;
create table if not exists clue_mailbox (
   id BIGINT auto_increment comment '主键 id',
   clue_id BIGINT not null comment '线索 id',
   title_id BIGINT not null comment '文章 id',
   send_user_id BIGINT not null comment '发送者 id',
   receive_user_id BIGINT not null comment '接受者 id',
   is_read INT not null default 0 comment '是否已阅读, 0 : 否, 1 :是',
   gmt_create DATETIME not null comment '创建时间',
   gmt_modified DATETIME not null comment '修改时间',
   version INT not null default 1 comment '乐观锁',
   is_deleted INT NOT null default 0 comment '逻辑删除',

   PRIMARY KEY (id)
) ENGINE = INNODB AUTO_INCREMENT = 1 DeFAULT CHARSET = utf8;

drop table if exists comment_mailbox;
create table if not exists comment_mailbox (
    id BIGINT auto_increment comment '主键 id',
    title_id BIGINT not null comment '文章 id',
    root_id BIGINT not null comment '父评论 id',
    comment_content TEXT not null comment '评论内容',
    send_user_id BIGINT not null comment '发送者 id',
    receive_user_id BIGINT not null comment '接受者 id',
    is_read INT not null default 0 comment '是否已阅读, 0 : 否, 1 :是',
    gmt_create DATETIME not null comment '创建时间',
    gmt_modified DATETIME not null comment '修改时间',
    version INT not null default 1 comment '乐观锁',
    is_deleted INT NOT null default 0 comment '逻辑删除',

    PRIMARY KEY (id)
) ENGINE = INNODB AUTO_INCREMENT = 1 DeFAULT CHARSET = utf8;

drop table if exists reply_mailbox;
create table if not exists reply_mailbox (
     id BIGINT auto_increment comment '主键 id',
     title_id BIGINT not null comment '文章 id',
     root_id BIGINT not null comment '父评论 id',
     reply_id BIGINT not null comment '子评论 id',
     reply_content TEXT not null comment '评论内容',
     send_user_id BIGINT not null comment '发送者 id',
     receive_user_id BIGINT not null comment '接受者 id',
     is_read INT not null default 0 comment '是否已阅读, 0 : 否, 1 :是',
     gmt_create DATETIME not null comment '创建时间',
     gmt_modified DATETIME not null comment '修改时间',
     version INT not null default 1 comment '乐观锁',
     is_deleted INT NOT null default 0 comment '逻辑删除',

     PRIMARY KEY (id)
) ENGINE = INNODB AUTO_INCREMENT = 1 DeFAULT CHARSET = utf8;

drop table if exists title_like_mailbox;
create table if not exists title_like_mailbox (
   id BIGINT auto_increment comment '主键 id',
   title_id BIGINT not null comment '文章 id',
   send_user_id BIGINT not null comment '发送者 id',
   receive_user_id BIGINT not null comment '接受者 id',
   is_read INT not null default 0 comment '是否已阅读, 0 : 否, 1 :是',
   gmt_create DATETIME not null comment '创建时间',
   gmt_modified DATETIME not null comment '修改时间',
   version INT not null default 1 comment '乐观锁',
   is_deleted INT NOT null default 0 comment '逻辑删除',

   PRIMARY KEY (id)
) ENGINE = INNODB AUTO_INCREMENT = 1 DeFAULT CHARSET = utf8;

drop table if exists comment_like_mailbox;
create table if not exists comment_like_mailbox (
    id BIGINT auto_increment comment '主键 id',
    title_id BIGINT not null comment '文章 id',
    root_id BIGINT not null comment '父评论 id',
    send_user_id BIGINT not null comment '发送者 id',
    receive_user_id BIGINT not null comment '接受者 id',
    is_read INT not null default 0 comment '是否已阅读, 0 : 否, 1 :是',
    gmt_create DATETIME not null comment '创建时间',
    gmt_modified DATETIME not null comment '修改时间',
    version INT not null default 1 comment '乐观锁',
    is_deleted INT NOT null default 0 comment '逻辑删除',

    PRIMARY KEY (id)
) ENGINE = INNODB AUTO_INCREMENT = 1 DeFAULT CHARSET = utf8;