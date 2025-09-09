# 退票功能Java代码实现示例

## 1. 实体类设计

### 1.1 RefundApplicationDO.java

```java
package cn.iocoder.yudao.module.studio.dal.dataobject.refund;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 退票申请 DO
 */
@TableName("studio_refund_application")
@KeySequence("studio_refund_application_seq")
@Data
@EqualsAndHashCode(callSuper = true)
public class RefundApplicationDO extends BaseDO {

    @TableId
    private Long id;
    private Long orderId;
    private String orderNo;
    private Long segmentId;
    private Long passengerId;
    private String passengerName;
    private String flightNo;
    private String airlineCode;
    private String airlineName;
    private String departureAirportCode;
    private String departureAirportName;
    private String arrivalAirportCode;
    private String arrivalAirportName;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String cabinClass;
    private BigDecimal originalTicketPrice;
    private BigDecimal refundFee;
    private BigDecimal refundAmount;
    private BigDecimal actualRefundAmount;
    private String currency;
    private String reason;
    private Integer refundType;
    private Integer status;
    private String approver;
    private LocalDateTime approveTime;
    private String approveRemarks;
    private String remarks;

}
```

### 1.2 RefundPolicyDO.java

```java
package cn.iocoder.yudao.module.studio.dal.dataobject.refund;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 退票政策 DO
 */
@TableName("studio_refund_policy")
@KeySequence("studio_refund_policy_seq")
@Data
@EqualsAndHashCode(callSuper = true)
public class RefundPolicyDO extends BaseDO {

    @TableId
    private Long id;
    private String airlineCode;
    private String cabinClass;
    private Integer refundType;
    private BigDecimal feeRate;
    private BigDecimal minFee;
    private BigDecimal maxFee;
    private Integer timeLimitHours;
    private String policyDesc;
    private LocalDate effectiveDate;
    private LocalDate expireDate;
    private Integer status;

}
```

## 2. 枚举类定义

### 2.1 RefundStatusEnum.java

```java
package cn.iocoder.yudao.module.studio.enums.refund;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 退票状态枚举
 */
@Getter
@AllArgsConstructor
public enum RefundStatusEnum {

    PENDING(10, "待审核"),
    APPROVED(20, "已通过"), 
    REJECTED(30, "已拒绝"),
    COMPLETED(40, "已完成"),
    CANCELLED(50, "已取消");

    private final Integer status;
    private final String name;

    public static RefundStatusEnum valueOf(Integer status) {
        for (RefundStatusEnum value : values()) {
            if (value.getStatus().equals(status)) {
                return value;
            }
        }
        return null;
    }

}
```

### 2.2 RefundTypeEnum.java

```java
package cn.iocoder.yudao.module.studio.enums.refund;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 退票类型枚举
 */
@Getter
@AllArgsConstructor
public enum RefundTypeEnum {

    VOLUNTARY(1, "自愿退票"),
    INVOLUNTARY(2, "非自愿退票");

    private final Integer type;
    private final String name;

    public static RefundTypeEnum valueOf(Integer type) {
        for (RefundTypeEnum value : values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }

}
```

## 3. VO类设计

### 3.1 RefundApplicationCreateReqVO.java

```java
package cn.iocoder.yudao.module.studio.controller.admin.refund.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 退票申请创建 Request VO")
@Data
public class RefundApplicationCreateReqVO {

    @Schema(description = "订单ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @Schema(description = "航段ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "航段ID不能为空")
    private Long segmentId;

    @Schema(description = "乘客ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "乘客ID不能为空")
    private Long passengerId;

    @Schema(description = "航班号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "航班号不能为空")
    @Size(max = 10, message = "航班号长度不能超过10个字符")
    private String flightNo;

    @Schema(description = "航司代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "航司代码不能为空")
    private String airlineCode;

    @Schema(description = "出发机场代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "出发机场代码不能为空")
    private String departureAirportCode;

    @Schema(description = "到达机场代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "到达机场代码不能为空")
    private String arrivalAirportCode;

    @Schema(description = "出发时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "出发时间不能为空")
    private LocalDateTime departureTime;

    @Schema(description = "到达时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "到达时间不能为空")
    private LocalDateTime arrivalTime;

    @Schema(description = "舱位等级", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "舱位等级不能为空")
    private String cabinClass;

    @Schema(description = "原票价", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "原票价不能为空")
    @DecimalMin(value = "0", message = "原票价必须大于等于0")
    private BigDecimal originalTicketPrice;

    @Schema(description = "退票费", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "退票费不能为空")
    @DecimalMin(value = "0", message = "退票费必须大于等于0")
    private BigDecimal refundFee;

    @Schema(description = "实际退款金额", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "实际退款金额不能为空")
    @DecimalMin(value = "0", message = "实际退款金额必须大于等于0")
    private BigDecimal actualRefundAmount;

    @Schema(description = "币种")
    private String currency = "CNY";

    @Schema(description = "退票原因", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "退票原因不能为空")
    @Size(max = 500, message = "退票原因长度不能超过500个字符")
    private String reason;

    @Schema(description = "退票类型", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "退票类型不能为空")
    @Min(value = 1, message = "退票类型必须是1或2")
    @Max(value = 2, message = "退票类型必须是1或2")
    private Integer refundType;

    @Schema(description = "备注")
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remarks;

}
```

### 3.2 RefundFeeRespVO.java

```java
package cn.iocoder.yudao.module.studio.controller.admin.refund.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "管理后台 - 退票费用计算 Response VO")
@Data
public class RefundFeeRespVO {

    @Schema(description = "原票价")
    private BigDecimal originalTicketPrice;

    @Schema(description = "退票费")
    private BigDecimal refundFee;

    @Schema(description = "退票金额")
    private BigDecimal refundAmount;

    @Schema(description = "实际退款金额")
    private BigDecimal actualRefundAmount;

    @Schema(description = "币种")
    private String currency;

    @Schema(description = "费用明细")
    private List<FeeDetail> feeDetails;

    @Data
    public static class FeeDetail {
        @Schema(description = "费用类型")
        private String type;

        @Schema(description = "金额")
        private BigDecimal amount;

        @Schema(description = "说明")
        private String description;
    }

}
```

## 4. Service接口和实现

### 4.1 RefundApplicationService.java

```java
package cn.iocoder.yudao.module.studio.service.refund;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.studio.controller.admin.refund.vo.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 退票申请 Service 接口
 */
public interface RefundApplicationService {

    /**
     * 提交退票申请
     */
    Long submitRefund(@Valid RefundApplicationCreateReqVO createReqVO);

    /**
     * 审核退票申请
     */
    void approveRefund(@Valid RefundApplicationApproveReqVO approveReqVO);

    /**
     * 执行退票
     */
    void executeRefund(Long id);

    /**
     * 计算退票费用
     */
    RefundFeeRespVO calculateRefundFee(@Valid RefundFeeCalculateReqVO calculateReqVO);

    /**
     * 获得退票申请分页
     */
    PageResult<RefundApplicationRespVO> getRefundPage(@Valid RefundApplicationPageReqVO pageReqVO);

    /**
     * 获取可退票信息
     */
    RefundInfoRespVO getRefundInfo(Long orderId);

}
```

### 4.2 RefundApplicationServiceImpl.java (关键方法)

```java
package cn.iocoder.yudao.module.studio.service.refund.impl;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.studio.controller.admin.refund.vo.*;
import cn.iocoder.yudao.module.studio.dal.dataobject.refund.RefundApplicationDO;
import cn.iocoder.yudao.module.studio.dal.dataobject.refund.RefundPolicyDO;
import cn.iocoder.yudao.module.studio.dal.mysql.refund.RefundApplicationMapper;
import cn.iocoder.yudao.module.studio.dal.mysql.refund.RefundPolicyMapper;
import cn.iocoder.yudao.module.studio.enums.refund.RefundStatusEnum;
import cn.iocoder.yudao.module.studio.enums.refund.RefundTypeEnum;
import cn.iocoder.yudao.module.studio.service.refund.RefundApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.studio.enums.ErrorCodeConstants.*;

@Service
@Slf4j
public class RefundApplicationServiceImpl implements RefundApplicationService {

    @Resource
    private RefundApplicationMapper refundApplicationMapper;
    
    @Resource
    private RefundPolicyMapper refundPolicyMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long submitRefund(RefundApplicationCreateReqVO createReqVO) {
        // 1. 数据验证
        validateRefundApplication(createReqVO);
        
        // 2. 构建DO对象
        RefundApplicationDO refundApplication = BeanUtils.toBean(createReqVO, RefundApplicationDO.class);
        refundApplication.setStatus(RefundStatusEnum.PENDING.getStatus());
        refundApplication.setCurrency("CNY");
        
        // 3. 补充订单和乘客信息
        enrichRefundApplicationInfo(refundApplication);
        
        // 4. 保存到数据库
        refundApplicationMapper.insert(refundApplication);
        
        // 5. 记录操作日志
        logRefundOperation(refundApplication.getId(), "CREATE", "提交退票申请", null, refundApplication);
        
        log.info("退票申请提交成功，ID: {}, 订单号: {}", refundApplication.getId(), refundApplication.getOrderNo());
        return refundApplication.getId();
    }

    @Override
    public RefundFeeRespVO calculateRefundFee(RefundFeeCalculateReqVO calculateReqVO) {
        // 1. 获取退票政策
        RefundPolicyDO policy = getRefundPolicy(calculateReqVO.getFlightNo(), 
                calculateReqVO.getCabinClass(), calculateReqVO.getRefundType());
        
        if (policy == null) {
            throw exception(REFUND_POLICY_NOT_FOUND);
        }
        
        // 2. 获取原票价
        BigDecimal originalPrice = getOriginalTicketPrice(calculateReqVO.getSegmentId(), 
                calculateReqVO.getPassengerId());
        
        // 3. 计算退票费
        BigDecimal refundFee = calculateRefundFeeAmount(originalPrice, policy);
        
        // 4. 计算实际退款金额
        BigDecimal actualRefundAmount = originalPrice.subtract(refundFee);
        
        // 5. 构建响应
        RefundFeeRespVO response = new RefundFeeRespVO();
        response.setOriginalTicketPrice(originalPrice);
        response.setRefundFee(refundFee);
        response.setRefundAmount(actualRefundAmount);
        response.setActualRefundAmount(actualRefundAmount);
        response.setCurrency("CNY");
        
        // 6. 构建费用明细
        List<RefundFeeRespVO.FeeDetail> feeDetails = new ArrayList<>();
        feeDetails.add(createFeeDetail("原票价", originalPrice, "票面价格"));
        feeDetails.add(createFeeDetail("退票费", refundFee, getRefundFeeDescription(calculateReqVO.getRefundType())));
        response.setFeeDetails(feeDetails);
        
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approveRefund(RefundApplicationApproveReqVO approveReqVO) {
        // 1. 获取退票申请
        RefundApplicationDO refundApplication = getRefundApplicationById(approveReqVO.getId());
        
        // 2. 验证状态
        if (!RefundStatusEnum.PENDING.getStatus().equals(refundApplication.getStatus())) {
            throw exception(REFUND_STATUS_NOT_ALLOW_APPROVE);
        }
        
        // 3. 更新状态
        RefundApplicationDO updateObj = new RefundApplicationDO();
        updateObj.setId(approveReqVO.getId());
        updateObj.setStatus(approveReqVO.getApproved() ? 
                RefundStatusEnum.APPROVED.getStatus() : RefundStatusEnum.REJECTED.getStatus());
        updateObj.setApprover(getLoginUserNickname());
        updateObj.setApproveTime(LocalDateTime.now());
        updateObj.setApproveRemarks(approveReqVO.getRemarks());
        
        refundApplicationMapper.updateById(updateObj);
        
        // 4. 记录操作日志
        String operationType = approveReqVO.getApproved() ? "APPROVE" : "REJECT";
        String operationDesc = approveReqVO.getApproved() ? "审核通过" : "审核拒绝";
        logRefundOperation(approveReqVO.getId(), operationType, operationDesc, 
                refundApplication, updateObj);
        
        log.info("退票申请审核完成，ID: {}, 结果: {}", approveReqVO.getId(), 
                approveReqVO.getApproved() ? "通过" : "拒绝");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void executeRefund(Long id) {
        // 1. 获取退票申请
        RefundApplicationDO refundApplication = getRefundApplicationById(id);
        
        // 2. 验证状态
        if (!RefundStatusEnum.APPROVED.getStatus().equals(refundApplication.getStatus())) {
            throw exception(REFUND_STATUS_NOT_ALLOW_EXECUTE);
        }
        
        // 3. 执行退票业务逻辑
        executeRefundBusiness(refundApplication);
        
        // 4. 更新状态为已完成
        RefundApplicationDO updateObj = new RefundApplicationDO();
        updateObj.setId(id);
        updateObj.setStatus(RefundStatusEnum.COMPLETED.getStatus());
        
        refundApplicationMapper.updateById(updateObj);
        
        // 5. 记录操作日志
        logRefundOperation(id, "EXECUTE", "执行退票", refundApplication, updateObj);
        
        log.info("退票执行完成，ID: {}, 退款金额: {}", id, refundApplication.getActualRefundAmount());
    }

    // ========== 私有方法 ==========

    private void validateRefundApplication(RefundApplicationCreateReqVO createReqVO) {
        // 1. 验证订单状态
        validateOrderStatus(createReqVO.getOrderId());
        
        // 2. 验证航班时间
        validateFlightTime(createReqVO.getDepartureTime());
        
        // 3. 验证退票类型
        if (RefundTypeEnum.valueOf(createReqVO.getRefundType()) == null) {
            throw exception(REFUND_TYPE_INVALID);
        }
    }

    private BigDecimal calculateRefundFeeAmount(BigDecimal originalPrice, RefundPolicyDO policy) {
        // 1. 按费率计算
        BigDecimal feeAmount = originalPrice.multiply(policy.getFeeRate())
                .setScale(2, RoundingMode.HALF_UP);
        
        // 2. 检查最低费用
        if (policy.getMinFee() != null && feeAmount.compareTo(policy.getMinFee()) < 0) {
            feeAmount = policy.getMinFee();
        }
        
        // 3. 检查最高费用
        if (policy.getMaxFee() != null && feeAmount.compareTo(policy.getMaxFee()) > 0) {
            feeAmount = policy.getMaxFee();
        }
        
        return feeAmount;
    }

    private RefundPolicyDO getRefundPolicy(String flightNo, String cabinClass, Integer refundType) {
        // 从航班号提取航司代码
        String airlineCode = extractAirlineCode(flightNo);
        
        // 查询退票政策
        return refundPolicyMapper.selectByAirlineAndCabinAndType(airlineCode, cabinClass, refundType);
    }

    private void executeRefundBusiness(RefundApplicationDO refundApplication) {
        // 1. 调用支付系统退款
        executePaymentRefund(refundApplication);
        
        // 2. 更新订单状态
        updateOrderStatus(refundApplication.getOrderId());
        
        // 3. 发送退票通知
        sendRefundNotification(refundApplication);
    }

    private RefundFeeRespVO.FeeDetail createFeeDetail(String type, BigDecimal amount, String description) {
        RefundFeeRespVO.FeeDetail detail = new RefundFeeRespVO.FeeDetail();
        detail.setType(type);
        detail.setAmount(amount);
        detail.setDescription(description);
        return detail;
    }

    private String getRefundFeeDescription(Integer refundType) {
        return RefundTypeEnum.VOLUNTARY.getType().equals(refundType) ? 
                "自愿退票费" : "非自愿退票费";
    }

    // ... 其他私有方法

}
```

## 5. Mapper接口设计

### 5.1 RefundApplicationMapper.java

```java
package cn.iocoder.yudao.module.studio.dal.mysql.refund;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.studio.controller.admin.refund.vo.RefundApplicationPageReqVO;
import cn.iocoder.yudao.module.studio.dal.dataobject.refund.RefundApplicationDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 退票申请 Mapper
 */
@Mapper
public interface RefundApplicationMapper extends BaseMapperX<RefundApplicationDO> {

    default PageResult<RefundApplicationDO> selectPage(RefundApplicationPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<RefundApplicationDO>()
                .likeIfPresent(RefundApplicationDO::getOrderNo, reqVO.getOrderNo())
                .likeIfPresent(RefundApplicationDO::getPassengerName, reqVO.getPassengerName())
                .eqIfPresent(RefundApplicationDO::getFlightNo, reqVO.getFlightNo())
                .eqIfPresent(RefundApplicationDO::getRefundType, reqVO.getRefundType())
                .eqIfPresent(RefundApplicationDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(RefundApplicationDO::getCreateTime, 
                        reqVO.getCreatedTimeStart(), reqVO.getCreatedTimeEnd())
                .orderByDesc(RefundApplicationDO::getId));
    }

}
```

## 6. 异常处理

### 6.1 ErrorCodeConstants.java

```java
package cn.iocoder.yudao.module.studio.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * 退票模块错误码
 */
public interface ErrorCodeConstants {

    // ========== 退票申请 400001-400099 ==========
    ErrorCode REFUND_APPLICATION_NOT_EXISTS = new ErrorCode(400001, "退票申请不存在");
    ErrorCode REFUND_ORDER_NOT_EXISTS = new ErrorCode(400002, "订单不存在");
    ErrorCode REFUND_ORDER_STATUS_NOT_ALLOW = new ErrorCode(400003, "订单状态不允许退票");
    ErrorCode REFUND_FLIGHT_ALREADY_DEPARTED = new ErrorCode(400004, "航班已起飞，无法退票");
    ErrorCode REFUND_TYPE_INVALID = new ErrorCode(400005, "退票类型无效");
    ErrorCode REFUND_STATUS_NOT_ALLOW_APPROVE = new ErrorCode(400006, "退票申请状态不允许审核");
    ErrorCode REFUND_STATUS_NOT_ALLOW_EXECUTE = new ErrorCode(400007, "退票申请状态不允许执行");
    ErrorCode REFUND_POLICY_NOT_FOUND = new ErrorCode(400008, "未找到适用的退票政策");
    ErrorCode REFUND_FEE_CALCULATE_FAILED = new ErrorCode(400009, "退票费用计算失败");

}
```

## 7. 配置文件

### 7.1 权限配置 (sql)

```sql
-- 退票管理菜单
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, status) 
VALUES ('退票管理', '', 1, 4, 2060, 'refund', 'ep:RefreshLeft', '', 0);

-- 退票申请菜单
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, status) 
VALUES ('退票申请', 'studio:refund:query', 2, 1, LAST_INSERT_ID(), 'application', 'ep:DocumentChecked', 'studio/refund/index', 0);

-- 退票权限
INSERT INTO system_menu (name, permission, type, sort, parent_id) VALUES ('退票申请查询', 'studio:refund:query', 3, 1, LAST_INSERT_ID());
INSERT INTO system_menu (name, permission, type, sort, parent_id) VALUES ('退票申请创建', 'studio:refund:create', 3, 2, LAST_INSERT_ID());
INSERT INTO system_menu (name, permission, type, sort, parent_id) VALUES ('退票申请审核', 'studio:refund:approve', 3, 3, LAST_INSERT_ID());
INSERT INTO system_menu (name, permission, type, sort, parent_id) VALUES ('退票申请执行', 'studio:refund:execute', 3, 4, LAST_INSERT_ID());
INSERT INTO system_menu (name, permission, type, sort, parent_id) VALUES ('退票申请取消', 'studio:refund:cancel', 3, 5, LAST_INSERT_ID());
INSERT INTO system_menu (name, permission, type, sort, parent_id) VALUES ('退票申请导出', 'studio:refund:export', 3, 6, LAST_INSERT_ID());
```

---

**说明**: 
- 以上代码示例展示了退票功能的核心Java实现
- 实际开发中需要根据项目具体架构进行调整
- 关键的业务逻辑如支付退款、通知发送等需要集成具体的第三方服务
- 建议按照示例结构逐步实现，并编写完整的单元测试