-- 差旅系统数据库初始化脚本

-- 删除已存在的表（如果存在）
DROP TABLE IF EXISTS `studio_rebooking_operation_log`;
DROP TABLE IF EXISTS `studio_rebooking_application`;
DROP TABLE IF EXISTS `studio_order_fees`;
DROP TABLE IF EXISTS `studio_flight_segments`;
DROP TABLE IF EXISTS `studio_passengers`;
DROP TABLE IF EXISTS `studio_orders`;

-- 创建订单表
CREATE TABLE `studio_orders` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `order_no` varchar(64) NOT NULL COMMENT '订单号',
    `user_id` bigint NOT NULL COMMENT '下单用户ID',
    `total_amount` decimal(10,2) NOT NULL COMMENT '订单总金额',
    `currency` varchar(10) NOT NULL DEFAULT 'CNY' COMMENT '币种',
    `order_status` int NOT NULL DEFAULT 10 COMMENT '订单状态 (10: 待支付, 20: 已支付, 30: 已出票, 40: 已完成, 90: 已取消)',
    `payment_status` int NOT NULL DEFAULT 10 COMMENT '支付状态 (10: 未支付, 20: 已支付, 30: 部分退款, 40: 全额退款)',
    `booking_type` varchar(20) NOT NULL DEFAULT 'ONLINE' COMMENT '预订类型 (ONLINE: 在线预订, OFFLINE: 线下预订)',
    `contact_name` varchar(100) NOT NULL COMMENT '联系人姓名',
    `contact_phone` varchar(20) NOT NULL COMMENT '联系人电话',
    `contact_email` varchar(100) COMMENT '联系人邮箱',
    `remark` text COMMENT '订单备注',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    `creator` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_order_status` (`order_status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

-- 创建乘客表
CREATE TABLE `studio_passengers` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `order_id` bigint NOT NULL COMMENT '关联订单ID',
    `passenger_type` int NOT NULL COMMENT '乘客类型 (1: 成人, 2: 儿童, 3: 婴儿)',
    `chinese_name` varchar(100) COMMENT '中文姓名',
    `english_name` varchar(100) NOT NULL COMMENT '英文姓名',
    `gender` varchar(10) NOT NULL COMMENT '性别 (MALE: 男, FEMALE: 女)',
    `birthday` date NOT NULL COMMENT '出生日期',
    `id_type` varchar(20) NOT NULL DEFAULT 'ID_CARD' COMMENT '证件类型 (ID_CARD: 身份证, PASSPORT: 护照, OTHER: 其他)',
    `id_number` varchar(50) NOT NULL COMMENT '证件号码',
    `id_expiry_date` date COMMENT '证件有效期',
    `nationality` varchar(10) NOT NULL DEFAULT 'CN' COMMENT '国籍',
    `phone` varchar(20) COMMENT '联系电话',
    `email` varchar(100) COMMENT '邮箱',
    `frequent_flyer_no` varchar(50) COMMENT '常旅客号',
    `meal_preference` varchar(20) COMMENT '餐食偏好',
    `seat_preference` varchar(20) COMMENT '座位偏好',
    `special_service` varchar(200) COMMENT '特殊服务需求',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    `creator` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_id_number` (`id_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='乘客表';

-- 创建航班航段表
CREATE TABLE `studio_flight_segments` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `order_id` bigint NOT NULL COMMENT '关联的订单ID',
    `passenger_id` bigint NOT NULL COMMENT '关联的乘客ID',
    `segment_type` int NOT NULL COMMENT '航段类型 (1: 去程, 2: 返程)',
    `airline_code` varchar(10) NOT NULL COMMENT '航司二字码 (例如: MU, CA)',
    `flight_no` varchar(20) NOT NULL COMMENT '航班号 (例如: MU583)',
    `departure_airport_code` varchar(10) NOT NULL COMMENT '出发机场三字码 (例如: PVG)',
    `arrival_airport_code` varchar(10) NOT NULL COMMENT '到达机场三字码 (例如: LAX)',
    `departure_time` datetime NOT NULL COMMENT '计划出发时间 (当地时间)',
    `arrival_time` datetime NOT NULL COMMENT '计划到达时间 (当地时间)',
    `cabin_class` varchar(20) NOT NULL COMMENT '舱位等级 (例如: ECONOMY, BUSINESS)',
    `ticket_no` varchar(50) COMMENT '电子票号 (出票后回填)',
    `status` int NOT NULL COMMENT '航段状态 (10: 正常, 20: 已改签, 30: 已退票)',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    `creator` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_passenger_id` (`passenger_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='航班航段表';

-- 创建订单费用明细表
CREATE TABLE `studio_order_fees` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `order_id` bigint NOT NULL COMMENT '关联的订单ID',
    `passenger_id` bigint COMMENT '关联的乘客ID (某些费用可能与特定乘客关联)',
    `fee_type` varchar(50) NOT NULL COMMENT '费用类型 (例如: TICKET_PRICE, AIRPORT_TAX, CHANGE_FEE, SERVICE_FEE)',
    `amount` decimal(10,2) NOT NULL COMMENT '费用金额',
    `currency` varchar(10) NOT NULL COMMENT '币种',
    `description` varchar(200) COMMENT '费用描述',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
    `creator` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单费用明细表';

-- 插入测试数据
-- 插入订单数据
INSERT INTO `studio_orders` (`id`, `order_no`, `user_id`, `total_amount`, `currency`, `order_status`, `payment_status`, `booking_type`, `contact_name`, `contact_phone`, `contact_email`, `remark`, `created_at`, `updated_at`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`) VALUES
(1, 'ORD20241201001', 1, 6450.00, 'CNY', 30, 20, 'ONLINE', '张三', '13812345678', 'zhangsan@example.com', '上海至洛杉矶商务出行', '2024-12-01 10:30:00', '2024-12-01 14:30:00', 'admin', '2024-12-01 10:30:00', 'admin', '2024-12-01 14:30:00', b'0', 1),
(2, 'ORD20241201002', 2, 3200.00, 'CNY', 20, 20, 'ONLINE', '李四', '13987654321', 'lisi@example.com', '北京至首尔出差', '2024-12-01 11:15:00', '2024-12-01 11:15:00', 'admin', '2024-12-01 11:15:00', 'admin', '2024-12-01 11:15:00', b'0', 1),
(3, 'ORD20241201003', 3, 8800.00, 'CNY', 10, 10, 'ONLINE', '王五', '13611112222', 'wangwu@example.com', '广州至新加坡家庭旅行', '2024-12-01 12:00:00', '2024-12-01 12:00:00', 'admin', '2024-12-01 12:00:00', 'admin', '2024-12-01 12:00:00', b'0', 1);

-- 插入乘客数据
INSERT INTO `studio_passengers` (`id`, `order_id`, `passenger_type`, `chinese_name`, `english_name`, `gender`, `birthday`, `id_type`, `id_number`, `id_expiry_date`, `nationality`, `phone`, `email`, `frequent_flyer_no`, `meal_preference`, `seat_preference`, `special_service`, `created_at`, `updated_at`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`) VALUES
(1, 1, 1, '张三', 'ZHANG SAN', 'MALE', '1985-03-15', 'ID_CARD', '310101198503150123', '2030-12-31', 'CN', '13812345678', 'zhangsan@example.com', 'MU123456789', 'NORMAL', 'AISLE', NULL, '2024-12-01 10:30:00', '2024-12-01 10:30:00', 'admin', '2024-12-01 10:30:00', 'admin', '2024-12-01 10:30:00', b'0', 1),
(2, 2, 1, '李四', 'LI SI', 'FEMALE', '1990-08-22', 'ID_CARD', '110101199008220456', '2028-05-20', 'CN', '13987654321', 'lisi@example.com', 'CA987654321', 'VEGETARIAN', 'WINDOW', NULL, '2024-12-01 11:15:00', '2024-12-01 11:15:00', 'admin', '2024-12-01 11:15:00', 'admin', '2024-12-01 11:15:00', b'0', 1),
(3, 3, 1, '王五', 'WANG WU', 'MALE', '1978-12-10', 'PASSPORT', 'G12345678', '2029-12-10', 'CN', '13611112222', 'wangwu@example.com', NULL, 'NORMAL', 'AISLE', NULL, '2024-12-01 12:00:00', '2024-12-01 12:00:00', 'admin', '2024-12-01 12:00:00', 'admin', '2024-12-01 12:00:00', b'0', 1),
(4, 3, 1, '王夫人', 'WANG FUREN', 'FEMALE', '1982-05-18', 'PASSPORT', 'G12345679', '2029-12-10', 'CN', '13611112223', 'wangfuren@example.com', NULL, 'NORMAL', 'AISLE', NULL, '2024-12-01 12:00:00', '2024-12-01 12:00:00', 'admin', '2024-12-01 12:00:00', 'admin', '2024-12-01 12:00:00', b'0', 1),
(5, 3, 2, '王小明', 'WANG XIAOMING', 'MALE', '2016-09-25', 'ID_CARD', '440101201609250123', NULL, 'CN', NULL, NULL, NULL, 'CHILD_MEAL', 'WINDOW', '无成人陪伴儿童', '2024-12-01 12:00:00', '2024-12-01 12:00:00', 'admin', '2024-12-01 12:00:00', 'admin', '2024-12-01 12:00:00', b'0', 1);

-- 插入航班航段数据
INSERT INTO `studio_flight_segments` (`id`, `order_id`, `passenger_id`, `segment_type`, `airline_code`, `flight_no`, `departure_airport_code`, `arrival_airport_code`, `departure_time`, `arrival_time`, `cabin_class`, `ticket_no`, `status`, `created_at`, `updated_at`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`) VALUES
(1, 1, 1, 1, 'MU', 'MU583', 'PVG', 'LAX', '2024-12-15 08:00:00', '2024-12-15 12:00:00', 'BUSINESS', '7811234567890', 10, '2024-12-01 10:30:00', '2024-12-01 14:30:00', 'admin', '2024-12-01 10:30:00', 'admin', '2024-12-01 14:30:00', b'0', 1),
(2, 1, 1, 2, 'MU', 'MU584', 'LAX', 'PVG', '2024-12-25 14:30:00', '2024-12-26 19:30:00', 'BUSINESS', '7811234567891', 10, '2024-12-01 10:30:00', '2024-12-01 14:30:00', 'admin', '2024-12-01 10:30:00', 'admin', '2024-12-01 14:30:00', b'0', 1),
(3, 2, 2, 1, 'CA', 'CA123', 'PEK', 'ICN', '2024-12-10 13:20:00', '2024-12-10 16:40:00', 'ECONOMY', NULL, 10, '2024-12-01 11:15:00', '2024-12-01 11:15:00', 'admin', '2024-12-01 11:15:00', 'admin', '2024-12-01 11:15:00', b'0', 1),
(4, 2, 2, 2, 'CA', 'CA124', 'ICN', 'PEK', '2024-12-15 18:30:00', '2024-12-15 19:50:00', 'ECONOMY', NULL, 10, '2024-12-01 11:15:00', '2024-12-01 11:15:00', 'admin', '2024-12-01 11:15:00', 'admin', '2024-12-01 11:15:00', b'0', 1),
(5, 3, 3, 1, 'CZ', 'CZ327', 'CAN', 'SIN', '2024-12-20 10:30:00', '2024-12-20 14:45:00', 'ECONOMY', NULL, 10, '2024-12-01 12:00:00', '2024-12-01 12:00:00', 'admin', '2024-12-01 12:00:00', 'admin', '2024-12-01 12:00:00', b'0', 1),
(6, 3, 3, 2, 'CZ', 'CZ328', 'SIN', 'CAN', '2024-12-28 16:20:00', '2024-12-28 20:35:00', 'ECONOMY', NULL, 10, '2024-12-01 12:00:00', '2024-12-01 12:00:00', 'admin', '2024-12-01 12:00:00', 'admin', '2024-12-01 12:00:00', b'0', 1),
(7, 3, 4, 1, 'CZ', 'CZ327', 'CAN', 'SIN', '2024-12-20 10:30:00', '2024-12-20 14:45:00', 'ECONOMY', NULL, 10, '2024-12-01 12:00:00', '2024-12-01 12:00:00', 'admin', '2024-12-01 12:00:00', 'admin', '2024-12-01 12:00:00', b'0', 1),
(8, 3, 4, 2, 'CZ', 'CZ328', 'SIN', 'CAN', '2024-12-28 16:20:00', '2024-12-28 20:35:00', 'ECONOMY', NULL, 10, '2024-12-01 12:00:00', '2024-12-01 12:00:00', 'admin', '2024-12-01 12:00:00', 'admin', '2024-12-01 12:00:00', b'0', 1),
(9, 3, 5, 1, 'CZ', 'CZ327', 'CAN', 'SIN', '2024-12-20 10:30:00', '2024-12-20 14:45:00', 'ECONOMY', NULL, 10, '2024-12-01 12:00:00', '2024-12-01 12:00:00', 'admin', '2024-12-01 12:00:00', 'admin', '2024-12-01 12:00:00', b'0', 1),
(10, 3, 5, 2, 'CZ', 'CZ328', 'SIN', 'CAN', '2024-12-28 16:20:00', '2024-12-28 20:35:00', 'ECONOMY', NULL, 10, '2024-12-01 12:00:00', '2024-12-01 12:00:00', 'admin', '2024-12-01 12:00:00', 'admin', '2024-12-01 12:00:00', b'0', 1);

-- 插入订单费用明细数据
INSERT INTO `studio_order_fees` (`id`, `order_id`, `passenger_id`, `fee_type`, `amount`, `currency`, `description`, `created_at`, `updated_at`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`) VALUES
-- 订单1的费用
(1, 1, 1, 'TICKET_PRICE', 5800.00, 'CNY', '机票基础价格 - MU583', '2024-12-01 10:30:00', '2024-12-01 10:30:00', 'admin', '2024-12-01 10:30:00', 'admin', '2024-12-01 10:30:00', b'0', 1),
(2, 1, NULL, 'AIRPORT_TAX', 100.00, 'CNY', '机场建设费', '2024-12-01 10:30:00', '2024-12-01 10:30:00', 'admin', '2024-12-01 10:30:00', 'admin', '2024-12-01 10:30:00', b'0', 1),
(3, 1, NULL, 'FUEL_SURCHARGE', 450.00, 'CNY', '燃油附加费', '2024-12-01 10:30:00', '2024-12-01 10:30:00', 'admin', '2024-12-01 10:30:00', 'admin', '2024-12-01 10:30:00', b'0', 1),
(4, 1, NULL, 'SERVICE_FEE', 100.00, 'CNY', '服务费', '2024-12-01 10:30:00', '2024-12-01 10:30:00', 'admin', '2024-12-01 10:30:00', 'admin', '2024-12-01 10:30:00', b'0', 1),
-- 订单2的费用
(5, 2, 2, 'TICKET_PRICE', 2800.00, 'CNY', '机票基础价格 - CA123', '2024-12-01 11:15:00', '2024-12-01 11:15:00', 'admin', '2024-12-01 11:15:00', 'admin', '2024-12-01 11:15:00', b'0', 1),
(6, 2, NULL, 'AIRPORT_TAX', 50.00, 'CNY', '机场建设费', '2024-12-01 11:15:00', '2024-12-01 11:15:00', 'admin', '2024-12-01 11:15:00', 'admin', '2024-12-01 11:15:00', b'0', 1),
(7, 2, NULL, 'FUEL_SURCHARGE', 280.00, 'CNY', '燃油附加费', '2024-12-01 11:15:00', '2024-12-01 11:15:00', 'admin', '2024-12-01 11:15:00', 'admin', '2024-12-01 11:15:00', b'0', 1),
(8, 2, NULL, 'SERVICE_FEE', 70.00, 'CNY', '服务费', '2024-12-01 11:15:00', '2024-12-01 11:15:00', 'admin', '2024-12-01 11:15:00', 'admin', '2024-12-01 11:15:00', b'0', 1),
-- 订单3的费用
(9, 3, 3, 'TICKET_PRICE', 1800.00, 'CNY', '机票基础价格 - CZ327 (成人)', '2024-12-01 12:00:00', '2024-12-01 12:00:00', 'admin', '2024-12-01 12:00:00', 'admin', '2024-12-01 12:00:00', b'0', 1),
(10, 3, 4, 'TICKET_PRICE', 1800.00, 'CNY', '机票基础价格 - CZ327 (成人)', '2024-12-01 12:00:00', '2024-12-01 12:00:00', 'admin', '2024-12-01 12:00:00', 'admin', '2024-12-01 12:00:00', b'0', 1),
(11, 3, 5, 'TICKET_PRICE', 1350.00, 'CNY', '机票基础价格 - CZ327 (儿童)', '2024-12-01 12:00:00', '2024-12-01 12:00:00', 'admin', '2024-12-01 12:00:00', 'admin', '2024-12-01 12:00:00', b'0', 1),
(12, 3, NULL, 'AIRPORT_TAX', 150.00, 'CNY', '机场建设费', '2024-12-01 12:00:00', '2024-12-01 12:00:00', 'admin', '2024-12-01 12:00:00', 'admin', '2024-12-01 12:00:00', b'0', 1),
(13, 3, NULL, 'FUEL_SURCHARGE', 400.00, 'CNY', '燃油附加费', '2024-12-01 12:00:00', '2024-12-01 12:00:00', 'admin', '2024-12-01 12:00:00', 'admin', '2024-12-01 12:00:00', b'0', 1),
(14, 3, NULL, 'SERVICE_FEE', 100.00, 'CNY', '服务费', '2024-12-01 12:00:00', '2024-12-01 12:00:00', 'admin', '2024-12-01 12:00:00', 'admin', '2024-12-01 12:00:00', b'0', 1),
(15, 3, NULL, 'INSURANCE_FEE', 200.00, 'CNY', '保险费', '2024-12-01 12:00:00', '2024-12-01 12:00:00', 'admin', '2024-12-01 12:00:00', 'admin', '2024-12-01 12:00:00', b'0', 1);

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