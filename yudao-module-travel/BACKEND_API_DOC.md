# 订单出票系统 - 后端接口文档

## 接口概述

本文档定义了订单出票系统所需的后端接口规范，包括出票流程、基础数据查询、费用计算等功能。

## 基础信息

- **接口前缀**: `/studio/ticket`
- **数据格式**: JSON
- **编码**: UTF-8
- **权限**: 需要登录用户，具有 `studio:ticket:issue` 权限

---

## 1. 核心出票接口

### 1.1 订单出票

**接口地址**: `POST /studio/ticket/issue`

**接口描述**: 执行订单出票操作，创建订单并生成电子票号

**请求参数**:
```json
{
  "orderId": 123,                    // 可选，现有订单ID
  "orderNo": "ORD20241201001",       // 可选，订单号
  "passengers": [                     // 必须，乘客信息列表
    {
      "id": 1,                       // 可选，现有乘客ID
      "passengerType": 1,            // 必须，乘客类型 (1:成人, 2:儿童, 3:婴儿)
      "lastName": "ZHANG",           // 必须，姓（拼音）
      "firstName": "SAN",            // 必须，名（拼音）
      "lastNameCn": "张",            // 可选，姓（中文）
      "firstNameCn": "三",           // 可选，名（中文）
      "gender": 1,                   // 必须，性别 (1:男, 2:女)
      "birthday": "1990-01-01",      // 必须，出生日期
      "nationality": "CN",           // 必须，国籍代码
      "documentType": 1,             // 必须，证件类型 (1:身份证, 2:护照, 3:港澳通行证, 4:台胞证)
      "documentNo": "110101199001011234", // 必须，证件号码
      "documentExpiry": "2030-12-31", // 可选，证件有效期
      "mobile": "13812345678",       // 可选，手机号
      "email": "zhangsan@example.com" // 可选，邮箱
    }
  ],
  "flightSegments": [                // 必须，航段信息列表
    {
      "id": 1,                       // 可选，现有航段ID
      "segmentType": 1,              // 必须，航段类型 (1:去程, 2:返程)
      "airlineCode": "MU",           // 必须，航司二字码
      "flightNo": "MU583",           // 必须，航班号
      "departureAirportCode": "PVG", // 必须，出发机场三字码
      "arrivalAirportCode": "LAX",   // 必须，到达机场三字码
      "departureTime": "2024-12-01 08:00:00", // 必须，出发时间
      "arrivalTime": "2024-12-01 12:00:00",   // 必须，到达时间
      "cabinClass": "ECONOMY"        // 必须，舱位等级
    }
  ],
  "fees": [                          // 必须，费用信息列表
    {
      "feeType": "TICKET_PRICE",     // 必须，费用类型
      "amount": 1000.00,             // 必须，费用金额
      "currency": "CNY",             // 必须，币种
      "description": "机票基础价格"   // 可选，费用描述
    }
  ],
  "totalAmount": 1170.00,            // 必须，总金额
  "currency": "CNY",                 // 必须，币种
  "remark": "备注信息"               // 可选，备注
}
```

**响应参数**:
```json
{
  "code": 200,
  "message": "出票成功",
  "data": {
    "orderId": 12345,                      // 订单ID
    "orderNo": "ORD20241201001",           // 订单号
    "ticketNos": [                         // 电子票号列表
      "7812345678901",
      "7812345678902"
    ],
    "totalAmount": 1170.00,                // 总金额
    "currency": "CNY",                     // 币种
    "status": 10,                          // 出票状态 (10:成功, 20:失败, 30:处理中)
    "message": "出票成功，请保存好电子票号",  // 结果消息
    "issueTime": "2024-12-01 10:30:00"     // 出票时间
  }
}
```

**错误响应**:
```json
{
  "code": 400,
  "message": "出票失败：航班已满员",
  "data": null
}
```

---

## 2. 基础数据查询接口

### 2.1 获取机场列表

**接口地址**: `GET /studio/ticket/airports`

**接口描述**: 获取所有可用机场信息

**请求参数**: 无

**响应参数**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": [
    {
      "code": "PVG",                 // 机场三字码
      "name": "上海浦东国际机场",     // 机场名称
      "city": "上海",                // 所属城市
      "country": "CN",               // 国家代码
      "timezone": "Asia/Shanghai"    // 时区
    },
    {
      "code": "PEK",
      "name": "北京首都国际机场",
      "city": "北京",
      "country": "CN",
      "timezone": "Asia/Shanghai"
    }
  ]
}
```

### 2.2 获取航司列表

**接口地址**: `GET /studio/ticket/airlines`

**接口描述**: 获取所有可用航空公司信息

**请求参数**: 无

**响应参数**:
```json
{
  "code": 200,
  "message": "查询成功",
  "data": [
    {
      "code": "MU",                  // 航司二字码
      "name": "中国东方航空",         // 航司名称
      "fullName": "China Eastern Airlines", // 航司全称
      "country": "CN"                // 国家代码
    },
    {
      "code": "CA",
      "name": "中国国际航空",
      "fullName": "Air China",
      "country": "CN"
    }
  ]
}
```

---

## 3. 航班验证接口

### 3.1 验证航班信息

**接口地址**: `GET /studio/ticket/validate-flight`

**接口描述**: 验证指定航班在指定日期是否存在且可用

**请求参数**:
- `airlineCode` (string, 必须): 航司二字码，如 "MU"
- `flightNo` (string, 必须): 航班号，如 "MU583"  
- `departureDate` (string, 必须): 出发日期，如 "2024-12-01"

**请求示例**: 
```
GET /studio/ticket/validate-flight?airlineCode=MU&flightNo=MU583&departureDate=2024-12-01
```

**响应参数**:
```json
{
  "code": 200,
  "message": "航班验证成功",
  "data": {
    "valid": true,                         // 是否有效
    "flightInfo": {
      "airlineCode": "MU",                 // 航司代码
      "flightNo": "MU583",                 // 航班号
      "departureAirport": "PVG",           // 出发机场
      "arrivalAirport": "LAX",             // 到达机场
      "departureTime": "2024-12-01 08:00:00", // 出发时间
      "arrivalTime": "2024-12-01 12:00:00",   // 到达时间
      "aircraft": "B777-300ER",            // 机型
      "availableSeats": 150,               // 可用座位数
      "status": "NORMAL"                   // 航班状态
    }
  }
}
```

**航班不存在响应**:
```json
{
  "code": 404,
  "message": "航班不存在或已取消",
  "data": {
    "valid": false,
    "reason": "FLIGHT_NOT_FOUND"
  }
}
```

---

## 4. 费用计算接口

### 4.1 计算费用

**接口地址**: `POST /studio/ticket/calculate-fees`

**接口描述**: 根据航段和乘客信息计算各项费用

**请求参数**:
```json
{
  "passengers": [                    // 乘客信息（简化版）
    {
      "passengerType": 1,            // 乘客类型
      "age": 30                      // 年龄
    }
  ],
  "flightSegments": [                // 航段信息（简化版）
    {
      "airlineCode": "MU",
      "flightNo": "MU583",
      "departureAirportCode": "PVG",
      "arrivalAirportCode": "LAX",
      "departureTime": "2024-12-01 08:00:00",
      "cabinClass": "ECONOMY"
    }
  ],
  "currency": "CNY"                  // 目标币种
}
```

**响应参数**:
```json
{
  "code": 200,
  "message": "费用计算成功",
  "data": {
    "fees": [
      {
        "feeType": "TICKET_PRICE",
        "amount": 1000.00,
        "currency": "CNY",
        "description": "机票基础价格"
      },
      {
        "feeType": "AIRPORT_TAX",
        "amount": 50.00,
        "currency": "CNY",
        "description": "机场建设费"
      },
      {
        "feeType": "FUEL_SURCHARGE",
        "amount": 100.00,
        "currency": "CNY",
        "description": "燃油附加费"
      },
      {
        "feeType": "SERVICE_FEE",
        "amount": 20.00,
        "currency": "CNY",
        "description": "服务费"
      }
    ],
    "totalAmount": 1170.00,
    "currency": "CNY",
    "breakdown": {
      "basePrice": 1000.00,           // 基础票价
      "taxes": 150.00,                // 税费合计
      "fees": 20.00                   // 其他费用合计
    }
  }
}
```

---

## 5. 数据字典

### 5.1 乘客类型
- `1`: 成人 (Adult)
- `2`: 儿童 (Child) 
- `3`: 婴儿 (Infant)

### 5.2 性别
- `1`: 男 (Male)
- `2`: 女 (Female)

### 5.3 证件类型
- `1`: 身份证 (ID Card)
- `2`: 护照 (Passport)
- `3`: 港澳通行证 (Hong Kong and Macao Travel Permit)
- `4`: 台胞证 (Taiwan Compatriot Pass)

### 5.4 航段类型
- `1`: 去程 (Outbound)
- `2`: 返程 (Return)

### 5.5 舱位等级
- `ECONOMY`: 经济舱
- `PREMIUM_ECONOMY`: 超级经济舱
- `BUSINESS`: 商务舱
- `FIRST`: 头等舱

### 5.6 费用类型
- `TICKET_PRICE`: 机票费
- `AIRPORT_TAX`: 机场建设费
- `FUEL_SURCHARGE`: 燃油附加费
- `SERVICE_FEE`: 服务费
- `CHANGE_FEE`: 改签费
- `REFUND_FEE`: 退票费
- `INSURANCE_FEE`: 保险费
- `OTHER_FEE`: 其他费用

### 5.7 出票状态
- `10`: 出票成功
- `20`: 出票失败
- `30`: 出票处理中

### 5.8 订单状态
- `10`: 待支付
- `20`: 待出票
- `30`: 已出票
- `40`: 部分改签
- `50`: 部分退票
- `60`: 已完成
- `70`: 已取消
- `80`: 改签中
- `90`: 退票中

---

## 6. 错误码定义

| 错误码 | 错误信息 | 说明 |
|--------|----------|------|
| 200 | 成功 | 操作成功 |
| 400 | 请求参数错误 | 参数验证失败 |
| 401 | 未授权 | 用户未登录 |
| 403 | 权限不足 | 用户无相关权限 |
| 404 | 资源不存在 | 航班、机场等不存在 |
| 409 | 业务冲突 | 航班已满员、重复出票等 |
| 500 | 系统内部错误 | 服务器异常 |

### 业务错误码 (40000+)

| 错误码 | 错误信息 | 说明 |
|--------|----------|------|
| 40001 | 航班不存在 | 指定航班不存在或已取消 |
| 40002 | 航班已满员 | 没有可用座位 |
| 40003 | 证件信息无效 | 证件号码格式错误或已过期 |
| 40004 | 乘客年龄不符 | 乘客年龄与类型不匹配 |
| 40005 | 费用计算失败 | 无法获取准确费用信息 |
| 40006 | 重复出票 | 该乘客在同一航班已有有效票 |
| 40007 | 出票超时 | 出票处理超时 |

---

## 7. 数据库设计建议

### 7.1 核心表结构

#### orders (订单表)
```sql
CREATE TABLE `orders` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_no` varchar(50) NOT NULL COMMENT '订单号',
  `group_name` varchar(100) DEFAULT NULL COMMENT '团组名称',
  `order_status` int(11) NOT NULL COMMENT '订单状态',
  `total_amount` decimal(10,2) NOT NULL COMMENT '总金额',
  `currency` varchar(10) NOT NULL COMMENT '币种',
  `creator_id` bigint(20) NOT NULL COMMENT '创建人ID',
  `creator_name` varchar(50) NOT NULL COMMENT '创建人姓名',
  `remark` text COMMENT '备注',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_at` datetime NOT NULL COMMENT '更新时间',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`)
) COMMENT='订单主表';
```

#### flight_segments (航段表)
```sql
CREATE TABLE `flight_segments` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `passenger_id` bigint(20) NOT NULL COMMENT '乘客ID',
  `segment_type` int(11) NOT NULL COMMENT '航段类型',
  `airline_code` varchar(10) NOT NULL COMMENT '航司代码',
  `flight_no` varchar(20) NOT NULL COMMENT '航班号',
  `departure_airport_code` varchar(10) NOT NULL COMMENT '出发机场',
  `arrival_airport_code` varchar(10) NOT NULL COMMENT '到达机场',
  `departure_time` datetime NOT NULL COMMENT '出发时间',
  `arrival_time` datetime NOT NULL COMMENT '到达时间',
  `cabin_class` varchar(20) NOT NULL COMMENT '舱位等级',
  `ticket_no` varchar(50) DEFAULT NULL COMMENT '电子票号',
  `status` int(11) NOT NULL COMMENT '航段状态',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_at` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_passenger_id` (`passenger_id`)
) COMMENT='航段表';
```

#### passengers (乘客表)
```sql
CREATE TABLE `passengers` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `passenger_type` int(11) NOT NULL COMMENT '乘客类型',
  `last_name` varchar(50) NOT NULL COMMENT '姓（拼音）',
  `first_name` varchar(50) NOT NULL COMMENT '名（拼音）',
  `last_name_cn` varchar(50) DEFAULT NULL COMMENT '姓（中文）',
  `first_name_cn` varchar(50) DEFAULT NULL COMMENT '名（中文）',
  `gender` int(11) NOT NULL COMMENT '性别',
  `birthday` date NOT NULL COMMENT '出生日期',
  `nationality` varchar(10) NOT NULL COMMENT '国籍',
  `document_type` int(11) NOT NULL COMMENT '证件类型',
  `document_no` varchar(50) NOT NULL COMMENT '证件号码',
  `document_expiry` date DEFAULT NULL COMMENT '证件有效期',
  `mobile` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_at` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_document` (`document_type`, `document_no`)
) COMMENT='乘客表';
```

#### order_fees (订单费用表)
```sql
CREATE TABLE `order_fees` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `passenger_id` bigint(20) DEFAULT NULL COMMENT '乘客ID',
  `fee_type` varchar(50) NOT NULL COMMENT '费用类型',
  `amount` decimal(10,2) NOT NULL COMMENT '费用金额',
  `currency` varchar(10) NOT NULL COMMENT '币种',
  `description` varchar(200) DEFAULT NULL COMMENT '费用描述',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_at` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`)
) COMMENT='订单费用表';
```

---

## 8. 接口权限说明

### 8.1 权限标识
- `studio:ticket:issue` - 出票权限
- `studio:ticket:calculate` - 费用计算权限
- `studio:ticket:validate` - 航班验证权限
- `studio:ticket:query` - 基础数据查询权限

### 8.2 角色建议
- **出票员**: 具有 `studio:ticket:issue`、`studio:ticket:calculate`、`studio:ticket:validate`、`studio:ticket:query` 权限
- **查询员**: 仅具有 `studio:ticket:query` 权限
- **管理员**: 具有所有权限

---

## 9. 性能要求

### 9.1 响应时间
- 基础数据查询: < 500ms
- 航班验证: < 1s
- 费用计算: < 2s  
- 出票操作: < 5s

### 9.2 并发要求
- 支持至少 100 并发用户同时操作
- 出票接口需要防重复提交

### 9.3 缓存策略
- 机场、航司数据建议缓存 24 小时
- 航班验证结果缓存 5 分钟
- 费用计算结果缓存 10 分钟

---

## 10. 安全要求

### 10.1 数据验证
- 所有输入参数必须进行格式验证
- 证件号码需要格式校验
- 手机号、邮箱格式验证

### 10.2 防重复
- 出票接口需要防重复提交机制
- 同一乘客同一航班不能重复出票

### 10.3 日志记录
- 记录所有出票操作日志
- 记录关键业务操作的审计日志
- 异常情况需要详细记录

---

## 11. 测试用例建议

### 11.1 正常流程测试
1. 完整出票流程（单人单程）
2. 多人多程出票
3. 不同舱位等级出票
4. 不同乘客类型出票

### 11.2 异常情况测试
1. 航班不存在
2. 航班已满员
3. 证件信息错误
4. 重复出票
5. 网络超时

### 11.3 边界值测试
1. 最大乘客数量
2. 最大航段数量
3. 最大金额限制
4. 特殊字符处理

本文档涵盖了订单出票系统的完整后端接口需求，请根据此文档进行后端开发和实现。