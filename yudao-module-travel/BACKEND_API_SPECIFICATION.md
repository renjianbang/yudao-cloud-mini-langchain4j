# 退票功能API接口详细文档

## 接口概述

本文档详细描述了退票功能模块的所有API接口，包括请求参数、响应格式、错误码等信息。

## 通用说明

### 请求头
```
Content-Type: application/json
Authorization: Bearer {JWT_TOKEN}
```

### 通用响应格式
```json
{
  "code": 0,           // 状态码，0表示成功
  "data": {},          // 响应数据
  "msg": "操作成功"     // 响应消息
}
```

### 错误码定义
| 错误码 | 错误信息 | 说明 |
|--------|----------|------|
| 400001 | 订单不存在 | 指定的订单ID不存在 |
| 400002 | 订单状态不允许退票 | 订单状态不支持退票操作 |
| 400003 | 航段不存在 | 指定的航段ID不存在 |
| 400004 | 乘客不存在 | 指定的乘客ID不存在 |
| 400005 | 退票申请不存在 | 指定的退票申请ID不存在 |
| 400006 | 退票申请状态不允许操作 | 当前状态不支持该操作 |
| 400007 | 权限不足 | 没有执行该操作的权限 |
| 400008 | 费用计算失败 | 退票费用计算出错 |
| 400009 | 退票政策获取失败 | 无法获取航班退票政策 |
| 500001 | 系统内部错误 | 服务器内部错误 |

## API接口详情

### 1. 提交退票申请

**接口信息**
- **URL**: `POST /studio/refund/submit`
- **权限**: `studio:refund:create`

**请求参数**
```json
{
  "orderId": 123,                              // 订单ID (必填)
  "segmentId": 1,                              // 航段ID (必填)
  "passengerId": 1,                            // 乘客ID (必填)
  "flightNo": "CA1234",                        // 航班号 (必填)
  "airlineCode": "CA",                         // 航司代码 (必填)
  "departureAirportCode": "PEK",               // 出发机场代码 (必填)
  "arrivalAirportCode": "PVG",                 // 到达机场代码 (必填)
  "departureTime": "2025-01-15T08:00:00",      // 出发时间 (必填)
  "arrivalTime": "2025-01-15T10:30:00",        // 到达时间 (必填)
  "cabinClass": "Y",                           // 舱位等级 (必填)
  "originalTicketPrice": 1500.00,              // 原票价 (必填)
  "refundFee": 300.00,                         // 退票费 (必填)
  "actualRefundAmount": 1200.00,               // 实际退款金额 (必填)
  "currency": "CNY",                           // 币种 (可选，默认CNY)
  "reason": "个人行程变更",                     // 退票原因 (必填)
  "refundType": 1,                             // 退票类型 (必填，1:自愿 2:非自愿)
  "remarks": "紧急退票"                        // 备注 (可选)
}
```

**响应示例**
```json
{
  "code": 0,
  "data": 1,                    // 返回退票申请ID
  "msg": "退票申请提交成功"
}
```

### 2. 审核退票申请

**接口信息**
- **URL**: `PUT /studio/refund/approve`
- **权限**: `studio:refund:approve`

**请求参数**
```json
{
  "id": 1,                      // 退票申请ID (必填)
  "approved": true,             // 是否通过 (必填)
  "remarks": "审核通过"          // 审核备注 (可选)
}
```

**响应示例**
```json
{
  "code": 0,
  "data": null,
  "msg": "审核成功"
}
```

### 3. 执行退票

**接口信息**
- **URL**: `PUT /studio/refund/execute?id={id}`
- **权限**: `studio:refund:execute`

**请求参数**
- `id`: 退票申请ID (必填)

**响应示例**
```json
{
  "code": 0,
  "data": null,
  "msg": "退票执行成功"
}
```

### 4. 取消退票申请

**接口信息**
- **URL**: `PUT /studio/refund/cancel`
- **权限**: `studio:refund:cancel`

**请求参数**
```json
{
  "id": 1,                      // 退票申请ID (必填)
  "reason": "取消原因"           // 取消原因 (必填)
}
```

**响应示例**
```json
{
  "code": 0,
  "data": null,
  "msg": "退票申请已取消"
}
```

### 5. 查询退票申请详情

**接口信息**
- **URL**: `GET /studio/refund/get?id={id}`
- **权限**: `studio:refund:query`

**请求参数**
- `id`: 退票申请ID (必填)

**响应示例**
```json
{
  "code": 0,
  "data": {
    "id": 1,
    "orderId": 123,
    "orderNo": "ORD202501090001",
    "segmentId": 1,
    "passengerId": 1,
    "passengerName": "张三",
    "flightNo": "CA1234",
    "airlineCode": "CA",
    "airlineName": "中国国际航空",
    "departureAirportCode": "PEK",
    "departureAirportName": "北京首都国际机场",
    "arrivalAirportCode": "PVG",
    "arrivalAirportName": "上海浦东国际机场",
    "departureTime": "2025-01-15T08:00:00",
    "arrivalTime": "2025-01-15T10:30:00",
    "cabinClass": "Y",
    "originalTicketPrice": 1500.00,
    "refundFee": 300.00,
    "refundAmount": 1200.00,
    "actualRefundAmount": 1200.00,
    "currency": "CNY",
    "reason": "个人行程变更",
    "refundType": 1,
    "status": 10,
    "approver": null,
    "approveTime": null,
    "approveRemarks": null,
    "remarks": "紧急退票",
    "createdAt": "2025-01-09T10:00:00",
    "updatedAt": "2025-01-09T10:00:00",
    "creatorName": "张三"
  },
  "msg": "操作成功"
}
```

### 6. 分页查询退票申请

**接口信息**
- **URL**: `GET /studio/refund/page`
- **权限**: `studio:refund:query`

**请求参数**
```json
{
  "pageNo": 1,                              // 页码 (必填，默认1)
  "pageSize": 10,                           // 页大小 (必填，默认10)
  "orderNo": "ORD202501090001",             // 订单号 (可选)
  "passengerName": "张三",                   // 乘客姓名 (可选)
  "flightNo": "CA1234",                     // 航班号 (可选)
  "refundType": 1,                          // 退票类型 (可选)
  "status": 10,                             // 状态 (可选)
  "createdTimeStart": "2025-01-01T00:00:00", // 创建时间开始 (可选)
  "createdTimeEnd": "2025-01-31T23:59:59"   // 创建时间结束 (可选)
}
```

**响应示例**
```json
{
  "code": 0,
  "data": {
    "list": [
      {
        "id": 1,
        "orderId": 123,
        "orderNo": "ORD202501090001",
        "segmentId": 1,
        "passengerId": 1,
        "passengerName": "张三",
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
        "status": 10,
        "createdAt": "2025-01-09T10:00:00"
      }
    ],
    "total": 100
  },
  "msg": "操作成功"
}
```

### 7. 获取可退票信息

**接口信息**
- **URL**: `GET /studio/refund/info?orderId={orderId}`
- **权限**: `studio:refund:query`

**请求参数**
- `orderId`: 订单ID (必填)

**响应示例**
```json
{
  "code": 0,
  "data": {
    "orderId": 123,
    "orderNo": "ORD202501090001",
    "segments": [
      {
        "id": 1,
        "flightNo": "CA1234",
        "airlineCode": "CA",
        "airlineName": "中国国际航空",
        "departureAirportCode": "PEK",
        "departureAirportName": "北京首都国际机场",
        "arrivalAirportCode": "PVG",
        "arrivalAirportName": "上海浦东国际机场",
        "departureTime": "2025-01-15T08:00:00",
        "arrivalTime": "2025-01-15T10:30:00",
        "cabinClass": "Y",
        "ticketPrice": 1500.00,
        "canRefund": true
      }
    ],
    "passengers": [
      {
        "id": 1,
        "name": "张三",
        "documentType": 2,
        "documentNo": "110101199001011234",
        "phoneNumber": "13800138000"
      }
    ]
  },
  "msg": "操作成功"
}
```

### 8. 计算退票费用

**接口信息**
- **URL**: `POST /studio/refund/calculate-fee`
- **权限**: `studio:refund:calculate`

**请求参数**
```json
{
  "segmentId": 1,                           // 航段ID (必填)
  "passengerId": 1,                         // 乘客ID (必填)
  "refundType": 1,                          // 退票类型 (必填)
  "flightNo": "CA1234",                     // 航班号 (必填)
  "cabinClass": "Y",                        // 舱位等级 (必填)
  "departureTime": "2025-01-15T08:00:00"    // 出发时间 (必填)
}
```

**响应示例**
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

### 9. 获取退票政策

**接口信息**
- **URL**: `GET /studio/refund/policy?flightNo={flightNo}&cabinClass={cabinClass}`
- **权限**: `studio:refund:query`

**请求参数**
- `flightNo`: 航班号 (必填)
- `cabinClass`: 舱位等级 (必填)

**响应示例**
```json
{
  "code": 0,
  "data": {
    "voluntaryRefundFeeRate": 0.2,
    "involuntaryRefundFeeRate": 0.1,
    "refundPolicy": "自愿退票费用为票价的20%，非自愿退票费用为票价的10%",
    "refundTimeLimit": "起飞前2小时",
    "specialRules": []
  },
  "msg": "操作成功"
}
```

### 10. 导出退票记录

**接口信息**
- **URL**: `GET /studio/refund/export-excel`
- **权限**: `studio:refund:export`

**请求参数**
```json
{
  "orderNo": "ORD202501090001",             // 订单号 (可选)
  "passengerName": "张三",                   // 乘客姓名 (可选)
  "refundType": 1,                          // 退票类型 (可选)
  "status": 10,                             // 状态 (可选)
  "createdTimeStart": "2025-01-01T00:00:00", // 创建时间开始 (可选)
  "createdTimeEnd": "2025-01-31T23:59:59"   // 创建时间结束 (可选)
}
```

**响应**: Excel文件下载

## 数据字典

### 退票类型 (refund_type)
| 值 | 描述 |
|----|------|
| 1  | 自愿退票 |
| 2  | 非自愿退票 |

### 退票状态 (status)
| 值 | 描述 |
|----|------|
| 10 | 待审核 |
| 20 | 已通过 |
| 30 | 已拒绝 |
| 40 | 已完成 |
| 50 | 已取消 |

### 舱位等级 (cabin_class)
| 值 | 描述 |
|----|------|
| Y  | 经济舱 |
| C  | 商务舱 |
| F  | 头等舱 |

## 业务规则

### 退票条件检查
1. 订单状态必须为：已出票(30)、部分改签(40)、部分退票(50)
2. 航班必须未起飞
3. 乘客信息必须有效
4. 航段信息必须有效

### 费用计算规则
1. 自愿退票：退票费 = 票价 × 20%
2. 非自愿退票：退票费 = 票价 × 10%
3. 实际退款 = 原票价 - 退票费

### 状态流转规则
```
待审核(10) → 已通过(20) → 已完成(40)
           → 已拒绝(30)
任何状态 → 已取消(50)
```

## 注意事项

1. 所有接口都需要JWT认证
2. 权限验证基于Spring Security注解
3. 所有操作都会记录操作日志
4. 金额字段精确到分(保留2位小数)
5. 时间格式统一使用ISO 8601格式
6. 分页查询默认按创建时间倒序排列
7. 导出功能需要考虑大数据量的处理

---

**文档版本**: 1.0  
**创建日期**: 2025-01-09  
**维护团队**: 后端开发组