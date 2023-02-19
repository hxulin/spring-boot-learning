DROP TABLE IF EXISTS user;

CREATE TABLE user
(
    id BIGINT(20) NOT NULL COMMENT '主键ID',
    name VARCHAR(30) NULL DEFAULT NULL COMMENT '姓名',
    age INT(11) NULL DEFAULT NULL COMMENT '年龄',
    email VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱',
    create_time DATETIME COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    test_local_date DATE COMMENT 'LocalDate test field',
    test_local_time TIME COMMENT 'LocalTime test field',
    test_date DATETIME COMMENT 'Date test field',
    PRIMARY KEY (id)
);