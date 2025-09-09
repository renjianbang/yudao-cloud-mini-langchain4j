# 改签模块后端开发设计文档

## 1. 概述

### 1.1 模块描述
改签模块用于处理机票订单的航班变更申请，包括改签申请、审核、执行和费用计算等功能。

### 1.2 技术架构
- **框架**: Spring Boot + MyBatis Plus
- **数据库**: MySQL 8.0+
- **缓存**: Redis
- **权限**: Spring Security + JWT

## 2. 数据库设计

### 2.1 改签申请表 (rebooking_application)

```sql
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
```

### 2.2 改签操作日志表 (rebooking_operation_log)

```sql
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
```

## 3. API接口设计

### 3.1 Controller: RebookingController

```java
@RestController
@RequestMapping("/admin-api/studio/rebooking")
@Tag(name = "管理后台 - 改签管理")
@Validated
public class RebookingController {

    @GetMapping("/info")
    @Operation(summary = "获取改签信息")
    public CommonResult<RebookingInfoRespVO> getRebookingInfo(@RequestParam("orderId") Long orderId)

    @PostMapping("/search-flights")
    @Operation(summary = "搜索可用航班")
    public CommonResult<List<FlightSearchRespVO>> searchFlights(@RequestBody FlightSearchReqVO reqVO)

    @PostMapping("/calculate-fee")
    @Operation(summary = "计算改签费用")
    public CommonResult<RebookingFeeRespVO> calculateFee(@RequestBody RebookingFeeReqVO reqVO)

    @PostMapping("/submit")
    @Operation(summary = "提交改签申请")
    public CommonResult<Boolean> submitRebooking(@RequestBody RebookingApplicationCreateReqVO createReqVO)

    @PutMapping("/approve")
    @Operation(summary = "审核改签申请")
    public CommonResult<Boolean> approveRebooking(@RequestBody RebookingApproveReqVO reqVO)

    @PutMapping("/execute")
    @Operation(summary = "执行改签")
    public CommonResult<Boolean> executeRebooking(@RequestParam("id") Long id)

    @GetMapping("/page")
    @Operation(summary = "获得改签申请分页")
    public CommonResult<PageResult<RebookingApplicationRespVO>> getRebookingPage(@Valid RebookingApplicationPageReqVO pageReqVO)
}
```

### 3.2 VO对象设计

#### 3.2.1 改签信息响应VO
```java
@Data
public class RebookingInfoRespVO {
    private OrderInfoVO orderInfo;
    private List<PassengerVO> passengers;
    private List<FlightSegmentVO> segments;
}
```

#### 3.2.2 航班搜索请求VO
```java
@Data
public class FlightSearchReqVO {
    @NotBlank(message = "出发机场不能为空")
    private String departureAirportCode;
    
    @NotBlank(message = "到达机场不能为空")
    private String arrivalAirportCode;
    
    @NotBlank(message = "出发日期不能为空")
    private String departureDate;
    
    private String cabinClass;
}
```

#### 3.2.3 费用计算请求VO
```java
@Data
public class RebookingFeeReqVO {
    @NotNull(message = "原航段ID不能为空")
    private Long originalSegmentId;
    
    @NotNull(message = "新航班信息不能为空")
    private FlightInfoVO newFlightInfo;
    
    @NotNull(message = "乘客ID不能为空")
    private Long passengerId;
}
```

## 4. Service层设计

### 4.1 RebookingService接口

```java
public interface RebookingService {
    
    /**
     * 获取改签信息
     */
    RebookingInfoRespVO getRebookingInfo(Long orderId);
    
    /**
     * 搜索可用航班
     */
    List<FlightSearchRespVO> searchFlights(FlightSearchReqVO reqVO);
    
    /**
     * 计算改签费用
     */
    RebookingFeeRespVO calculateFee(RebookingFeeReqVO reqVO);
    
    /**
     * 提交改签申请
     */
    Long submitRebooking(RebookingApplicationCreateReqVO createReqVO);
    
    /**
     * 审核改签申请
     */
    void approveRebooking(RebookingApproveReqVO reqVO);
    
    /**
     * 执行改签
     */
    void executeRebooking(Long id);
    
    /**
     * 获得改签申请分页
     */
    PageResult<RebookingApplicationRespVO> getRebookingPage(RebookingApplicationPageReqVO pageReqVO);
}
```

### 4.2 核心业务逻辑

#### 4.2.1 费用计算逻辑
```java
public RebookingFeeRespVO calculateFee(RebookingFeeReqVO reqVO) {
    // 1. 获取原航段信息
    FlightSegmentDO originalSegment = flightSegmentService.getFlightSegment(reqVO.getOriginalSegmentId());
    
    // 2. 获取新航班价格
    BigDecimal newPrice = getFlightPrice(reqVO.getNewFlightInfo());
    
    // 3. 计算改签手续费
    BigDecimal changeFee = calculateChangeFee(originalSegment, reqVO.getNewFlightInfo());
    
    // 4. 计算票价差额
    BigDecimal fareDifference = newPrice.subtract(originalSegment.getPrice());
    
    // 5. 计算服务费
    BigDecimal serviceFee = calculateServiceFee();
    
    // 6. 计算总费用
    BigDecimal totalFee = changeFee.add(fareDifference).add(serviceFee);
    
    return new RebookingFeeRespVO(changeFee, fareDifference, serviceFee, totalFee);
}
```

#### 4.2.2 改签执行逻辑
```java
@Transactional(rollbackFor = Exception.class)
public void executeRebooking(Long id) {
    // 1. 获取改签申请
    RebookingApplicationDO application = getRebookingApplication(id);
    
    // 2. 验证状态
    validateRebookingStatus(application);
    
    // 3. 更新原航段状态
    updateOriginalSegmentStatus(application.getOriginalSegmentId());
    
    // 4. 创建新航段
    createNewFlightSegment(application);
    
    // 5. 处理费用
    processFee(application);
    
    // 6. 更新申请状态
    updateRebookingStatus(id, RebookingStatusEnum.COMPLETED);
    
    // 7. 记录操作日志
    logOperation(id, "执行改签", "改签执行成功");
}
```

## 5. 配置和权限

### 5.1 权限配置
```java
// 权限标识
public static final String REBOOKING_QUERY = "studio:rebooking:query";
public static final String REBOOKING_CREATE = "studio:rebooking:create";
public static final String REBOOKING_APPROVE = "studio:rebooking:approve";
public static final String REBOOKING_EXECUTE = "studio:rebooking:execute";
```

### 5.2 菜单配置
```sql
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted) VALUES 
('改签管理', '', 1, 5, 2000, 'rebooking', 'ep:refresh', NULL, NULL, 0, b'1', b'1', b'1', '1', '2025-01-08 18:00:00', '1', '2025-01-08 18:00:00', b'0'),
('改签申请查询', 'studio:rebooking:query', 3, 1, 上级菜单ID, '', '', '', '', 0, b'1', b'1', b'1', '1', '2025-01-08 18:00:00', '1', '2025-01-08 18:00:00', b'0'),
('改签申请审核', 'studio:rebooking:approve', 3, 2, 上级菜单ID, '', '', '', '', 0, b'1', b'1', b'1', '1', '2025-01-08 18:00:00', '1', '2025-01-08 18:00:00', b'0');
```

## 6. 实现要点

### 6.1 异常处理
- 订单状态验证
- 航段状态验证
- 费用计算异常
- 第三方API调用异常

### 6.2 事务管理
- 改签执行使用@Transactional
- 费用处理事务一致性
- 状态更新原子性

### 6.3 日志记录
- 操作日志记录
- 异常日志记录
- 业务流程跟踪

### 6.4 缓存策略
- 航班信息缓存
- 机场信息缓存
- 费用规则缓存

## 7. 测试用例

### 7.1 单元测试
- 费用计算测试
- 状态流转测试
- 权限验证测试

### 7.2 集成测试
- 完整改签流程测试
- 异常场景测试
- 并发场景测试

## 8. 部署说明

### 8.1 数据库脚本
执行上述建表SQL

### 8.2 配置项
```yaml
rebooking:
  # 改签手续费配置
  change-fee:
    domestic: 200  # 国内航班改签费
    international: 500  # 国际航班改签费
  # 服务费配置
  service-fee: 50
```

## 9. 监控和告警

### 9.1 业务监控
- 改签申请数量
- 改签成功率
- 平均处理时间

### 9.2 技术监控
- API响应时间
- 数据库性能
- 缓存命中率

此文档提供了改签模块后端开发的完整指导，包含了数据库设计、API接口、业务逻辑、配置部署等所有必要信息。