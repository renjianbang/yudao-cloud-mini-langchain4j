# 退票功能后端开发设计文档

## 1. 项目概述

### 1.1 功能描述
退票功能模块是航空票务系统的重要组成部分，支持乘客对已出票的航班进行退票操作。系统需要支持自愿退票和非自愿退票两种类型，并提供完整的审核和费用计算流程。

### 1.2 技术架构
- **框架**: Spring Boot 2.7+ + MyBatis Plus 3.5+
- **数据库**: MySQL 8.0+
- **缓存**: Redis 6.0+
- **权限**: Spring Security + JWT
- **API文档**: Swagger 3.0

### 1.3 模块依赖
- 订单管理模块 (studio-orders)
- 乘客管理模块 (studio-passengers) 
- 航段管理模块 (studio-segments)
- 权限管理模块 (system)

## 2. 数据库设计

### 2.1 核心表结构

#### 2.1.1 退票申请表 (studio_refund_application)

```sql
CREATE TABLE studio_refund_application (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    order_id BIGINT NOT NULL COMMENT '关联的订单ID',
    order_no VARCHAR(50) NOT NULL COMMENT '订单号',
    segment_id BIGINT NOT NULL COMMENT '航段ID',
    passenger_id BIGINT NOT NULL COMMENT '乘客ID',
    passenger_name VARCHAR(50) NOT NULL COMMENT '乘客姓名',
    
    -- 航班信息
    flight_no VARCHAR(10) NOT NULL COMMENT '航班号',
    airline_code VARCHAR(3) NOT NULL COMMENT '航司代码',
    airline_name VARCHAR(100) COMMENT '航司名称',
    departure_airport_code VARCHAR(3) NOT NULL COMMENT '出发机场代码',
    departure_airport_name VARCHAR(100) COMMENT '出发机场名称',
    arrival_airport_code VARCHAR(3) NOT NULL COMMENT '到达机场代码',
    arrival_airport_name VARCHAR(100) COMMENT '到达机场名称',
    departure_time DATETIME NOT NULL COMMENT '出发时间',
    arrival_time DATETIME NOT NULL COMMENT '到达时间',
    cabin_class VARCHAR(2) NOT NULL COMMENT '舱位等级',
    
    -- 费用信息
    original_ticket_price DECIMAL(10,2) NOT NULL COMMENT '原票价',
    refund_fee DECIMAL(10,2) NOT NULL COMMENT '退票费',
    refund_amount DECIMAL(10,2) NOT NULL COMMENT '退票金额',
    actual_refund_amount DECIMAL(10,2) NOT NULL COMMENT '实际退款金额',
    currency VARCHAR(3) DEFAULT 'CNY' COMMENT '币种',
    
    -- 业务信息
    reason TEXT NOT NULL COMMENT '退票原因',
    refund_type TINYINT NOT NULL COMMENT '退票类型 (1: 自愿退票, 2: 非自愿退票)',
    status TINYINT DEFAULT 10 COMMENT '状态 (10: 待审核, 20: 已通过, 30: 已拒绝, 40: 已完成, 50: 已取消)',
    
    -- 审核信息
    approver VARCHAR(50) COMMENT '审核人',
    approve_time DATETIME COMMENT '审核时间',
    approve_remarks TEXT COMMENT '审核备注',
    
    -- 系统信息
    remarks TEXT COMMENT '申请备注',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    creator_id BIGINT COMMENT '创建人ID',
    creator_name VARCHAR(50) COMMENT '创建人姓名',
    updater_id BIGINT COMMENT '更新人ID',
    tenant_id BIGINT DEFAULT 1 COMMENT '租户ID',
    deleted TINYINT DEFAULT 0 COMMENT '是否删除 (0: 未删除, 1: 已删除)',
    
    -- 索引
    INDEX idx_order_id (order_id),
    INDEX idx_order_no (order_no),
    INDEX idx_passenger_id (passenger_id),
    INDEX idx_flight_no (flight_no),
    INDEX idx_status (status),
    INDEX idx_refund_type (refund_type),
    INDEX idx_created_at (created_at),
    INDEX idx_departure_time (departure_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='退票申请表';
```

#### 2.1.2 退票操作日志表 (studio_refund_operation_log)

```sql
CREATE TABLE studio_refund_operation_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    refund_id BIGINT NOT NULL COMMENT '退票申请ID',
    operation_type VARCHAR(20) NOT NULL COMMENT '操作类型 (CREATE, APPROVE, REJECT, EXECUTE, CANCEL)',
    operation_desc VARCHAR(200) COMMENT '操作描述',
    content_before JSON COMMENT '操作前的数据快照',
    content_after JSON COMMENT '操作后的数据快照',
    operator_id BIGINT COMMENT '操作人ID',
    operator_name VARCHAR(50) COMMENT '操作人姓名',
    operator_ip VARCHAR(50) COMMENT '操作IP',
    remark TEXT COMMENT '操作备注',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    
    INDEX idx_refund_id (refund_id),
    INDEX idx_operation_type (operation_type),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='退票操作日志表';
```

#### 2.1.3 退票政策配置表 (studio_refund_policy)

```sql
CREATE TABLE studio_refund_policy (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    airline_code VARCHAR(3) NOT NULL COMMENT '航司代码',
    cabin_class VARCHAR(2) NOT NULL COMMENT '舱位等级',
    refund_type TINYINT NOT NULL COMMENT '退票类型 (1: 自愿, 2: 非自愿)',
    fee_rate DECIMAL(5,4) NOT NULL COMMENT '退票费率 (0.0000-1.0000)',
    min_fee DECIMAL(10,2) DEFAULT 0 COMMENT '最低退票费',
    max_fee DECIMAL(10,2) COMMENT '最高退票费',
    time_limit_hours INT DEFAULT 24 COMMENT '起飞前时间限制(小时)',
    policy_desc TEXT COMMENT '政策描述',
    effective_date DATE NOT NULL COMMENT '生效日期',
    expire_date DATE COMMENT '失效日期',
    status TINYINT DEFAULT 1 COMMENT '状态 (0: 禁用, 1: 启用)',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    UNIQUE KEY uk_airline_cabin_type (airline_code, cabin_class, refund_type, effective_date),
    INDEX idx_airline_code (airline_code),
    INDEX idx_effective_date (effective_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='退票政策配置表';
```

### 2.2 初始数据

```sql
-- 插入默认退票政策
INSERT INTO studio_refund_policy (airline_code, cabin_class, refund_type, fee_rate, min_fee, time_limit_hours, policy_desc, effective_date) VALUES
('CA', 'Y', 1, 0.2000, 50.00, 24, '中国国际航空经济舱自愿退票政策', '2025-01-01'),
('CA', 'Y', 2, 0.1000, 20.00, 2, '中国国际航空经济舱非自愿退票政策', '2025-01-01'),
('CA', 'C', 1, 0.1500, 100.00, 24, '中国国际航空商务舱自愿退票政策', '2025-01-01'),
('CA', 'C', 2, 0.0500, 50.00, 2, '中国国际航空商务舱非自愿退票政策', '2025-01-01'),
('MU', 'Y', 1, 0.2000, 50.00, 24, '中国东方航空经济舱自愿退票政策', '2025-01-01'),
('MU', 'Y', 2, 0.1000, 20.00, 2, '中国东方航空经济舱非自愿退票政策', '2025-01-01'),
('CZ', 'Y', 1, 0.2000, 50.00, 24, '中国南方航空经济舱自愿退票政策', '2025-01-01'),
('CZ', 'Y', 2, 0.1000, 20.00, 2, '中国南方航空经济舱非自愿退票政策', '2025-01-01');
```

## 3. API接口清单

### 3.1 核心接口列表

| 接口名称 | HTTP方法 | 路径 | 描述 |
|---------|----------|------|------|
| 提交退票申请 | POST | /studio/refund/submit | 提交退票申请 |
| 审核退票申请 | PUT | /studio/refund/approve | 审核退票申请 |
| 执行退票 | PUT | /studio/refund/execute | 执行退票操作 |
| 取消退票申请 | PUT | /studio/refund/cancel | 取消退票申请 |
| 查询退票详情 | GET | /studio/refund/get | 根据ID查询退票详情 |
| 分页查询退票 | GET | /studio/refund/page | 分页查询退票申请列表 |
| 获取可退票信息 | GET | /studio/refund/info | 根据订单ID获取可退票信息 |
| 计算退票费用 | POST | /studio/refund/calculate-fee | 计算退票费用 |
| 获取退票政策 | GET | /studio/refund/policy | 获取退票政策信息 |
| 导出退票记录 | GET | /studio/refund/export-excel | 导出退票记录Excel |

### 3.2 关键接口详情

#### 3.2.1 提交退票申请

**请求参数:**
```json
{
  "orderId": 123,
  "segmentId": 1,
  "passengerId": 1,
  "flightNo": "CA1234",
  "airlineCode": "CA",
  "departureAirportCode": "PEK",
  "arrivalAirportCode": "PVG",
  "departureTime": "2025-01-15T08:00:00",
  "arrivalTime": "2025-01-15T10:30:00",
  "cabinClass": "Y",
  "originalTicketPrice": 1500.00,
  "refundFee": 300.00,
  "actualRefundAmount": 1200.00,
  "currency": "CNY",
  "reason": "个人行程变更",
  "refundType": 1,
  "remarks": "紧急退票"
}
```

**响应结果:**
```json
{
  "code": 0,
  "data": 1,
  "msg": "退票申请提交成功"
}
```

#### 3.2.2 计算退票费用

**请求参数:**
```json
{
  "segmentId": 1,
  "passengerId": 1,
  "refundType": 1,
  "flightNo": "CA1234",
  "cabinClass": "Y",
  "departureTime": "2025-01-15T08:00:00"
}
```

**响应结果:**
```json
{
  "code": 0,
  "data": {
    "originalTicketPrice": 1500.00,
    "refundFee": 300.00,
    "refundAmount": 1200.00,
    "actualRefundAmount": 1200.00,
    "currency": "CNY",
    "feeDetails": [
      {
        "type": "原票价",
        "amount": 1500.00,
        "description": "票面价格"
      },
      {
        "type": "退票费",
        "amount": 300.00,
        "description": "自愿退票费"
      }
    ]
  },
  "msg": "操作成功"
}
```

## 4. 业务逻辑设计

### 4.1 退票条件检查
- 订单状态必须为：已出票(30)、部分改签(40)、部分退票(50)
- 航班必须未起飞
- 乘客信息必须有效
- 航段信息必须有效

### 4.2 费用计算规则
- **自愿退票**: 退票费 = 票价 × 20%
- **非自愿退票**: 退票费 = 票价 × 10%
- **实际退款**: 原票价 - 退票费

### 4.3 状态流转
```
待审核(10) → 已通过(20) → 已完成(40)
           → 已拒绝(30)
任何状态 → 已取消(50)
```

### 4.4 权限控制
- **普通用户**: 可提交退票申请、查看自己的申请
- **管理员**: 可审核退票申请、执行退票操作
- **超级管理员**: 拥有所有权限

## 5. 开发任务分解

### 5.1 第一阶段：基础框架 (5个工作日)
1. 数据库表创建和基础数据初始化
2. 实体类(DO)设计和实现
3. Mapper接口设计和SQL实现
4. 基础VO类设计

### 5.2 第二阶段：核心业务 (6个工作日)
1. Service接口和实现类
2. Controller接口实现
3. 费用计算逻辑实现
4. 退票政策管理

### 5.3 第三阶段：完善功能 (4个工作日)
1. 操作日志记录
2. 权限控制集成
3. Excel导出功能
4. 异常处理和参数验证
5. 单元测试编写

## 6. 技术要求

### 6.1 性能要求
- 接口响应时间 < 2秒
- 支持并发用户数 > 1000
- 数据库查询优化

### 6.2 安全要求
- 接口需要JWT认证
- 操作日志记录
- 数据脱敏处理
- 防止重复提交

### 6.3 其他要求
- 事务控制
- 异常处理
- 参数验证
- 返回值统一格式

## 7. 对接说明

### 7.1 前后端对接
1. 确保前端API调用路径与后端接口路径一致
2. 确保请求参数和响应数据结构匹配
3. 处理错误码和异常情况
4. 添加Loading状态和用户提示

### 7.2 测试数据
建议使用模拟数据进行前端测试，待后端接口完成后再进行联调。

### 7.3 部署要求
- 开发环境：本地MySQL + Redis
- 测试环境：独立数据库实例
- 生产环境：高可用数据库集群

**预估总开发工期：15个工作日**

---

**文档版本**: 1.0  
**创建日期**: 2025-01-09  
**更新日期**: 2025-01-09  
**创建人**: 系统架构师