CREATE TABLE IF NOT EXISTS `link_configs` (
  `id`                 BIGINT        NOT NULL   AUTO_INCREMENT COMMENT '自增主键',
  `key`                VARCHAR(32)   NULL       COMMENT '键',
  `value`              TEXT          NULL       COMMENT '值',
  `description`        VARCHAR(128 ) NULL       COMMENT '描述',
  `scope`              VARCHAR(32)   NULL       COMMENT '适用范围',
  `created_at`         DATETIME      NULL       COMMENT '创建时间',
  `updated_at`         DATETIME      NULL       COMMENT '更新时间',
  PRIMARY KEY (`id`)
);



INSERT INTO `link`.`link_configs` (`key`, `value`, `description`, `created_at`, `updated_at`) VALUES ('test', 'hello world!', '', '2016-06-01 13:44:38', '2016-06-01 13:44:38');
