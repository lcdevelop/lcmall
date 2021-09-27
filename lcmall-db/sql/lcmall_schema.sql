drop database if exists lcmall;
drop user if exists 'lcmall'@'%';
-- 支持emoji：需要mysql数据库参数： character_set_server=utf8mb4
create database lcmall default character set utf8mb4 collate utf8mb4_unicode_ci;
use lcmall;
create user 'lcmall'@'%' identified by 'lcmall123456';
grant all privileges on lcmall.* to 'lcmall'@'%';
flush privileges;