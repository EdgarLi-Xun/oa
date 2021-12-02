

-- 2021.4.7
ALTER TABLE user ADD COLUMN daily int(11) DEFAULT 1 COMMENT '是否需要发日报 0 ： 不需要 1：需要' AFTER email_password;

