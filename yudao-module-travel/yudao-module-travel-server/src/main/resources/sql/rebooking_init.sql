-- 改签模块数据库表创建脚本

-- 删除已存在的表（如果存在）
DROP TABLE IF EXISTS `studio_rebooking_operation_log`;
DROP TABLE IF EXISTS `studio_rebooking_application`;

-- 创建改签申请表
CREATE TABLE `studio_rebooking_application` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` bigint(20) NOT NULL COMMENT '关联的订单ID',
  `original_segment_id` bigint(20) NOT NULL COMMENT '原航段ID',
  `passenger_id` bigint(20) NOT NULL COMMENT '乘客ID',
  `new_flight_no` varchar(20) NOT NULL COMMENT '新航班号',
  `new_airline_code` varchar(10) NOT NULL COMMENT '新航司代码',
  `new_departure_airport_code` varchar(10) NOT NULL COMMENT '新出发机场',
  `new_arrival_airport_code` varchar(10) NOT NULL COMMENT '新到达机场',
  `new_departure_time` datetime NOT NULL COMMENT '新出发时间',
  `new_arrival_time` datetime NOT NULL COMMENT '新到达时间',
  `new_cabin_class` varchar(20) NOT NULL COMMENT '新舱位等级',
  `change_fee` decimal(10,2) DEFAULT 0.00 COMMENT '改签费用',
  `fare_difference` decimal(10,2) DEFAULT 0.00 COMMENT '票价差额',
  `total_fee` decimal(10,2) DEFAULT 0.00 COMMENT '总费用',
  `reason` varchar(50) NOT NULL COMMENT '改签原因',
  `status` tinyint(4) NOT NULL DEFAULT 10 COMMENT '状态 (10: 待审核, 20: 已通过, 30: 已拒绝, 40: 已完成)',
  `approver` varchar(100) DEFAULT NULL COMMENT '审核人',
  `approve_time` datetime DEFAULT NULL COMMENT '审核时间',
  `remarks` text COMMENT '备注',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_passenger_id` (`passenger_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='改签申请表';

-- 创建改签操作日志表
CREATE TABLE `studio_rebooking_operation_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `rebooking_id` bigint(20) NOT NULL COMMENT '改签申请ID',
  `operation_type` varchar(50) NOT NULL COMMENT '操作类型',
  `operation_desc` varchar(500) DEFAULT NULL COMMENT '操作描述',
  `operator` varchar(100) NOT NULL COMMENT '操作人',
  `operation_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `idx_rebooking_id` (`rebooking_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='改签操作日志表';