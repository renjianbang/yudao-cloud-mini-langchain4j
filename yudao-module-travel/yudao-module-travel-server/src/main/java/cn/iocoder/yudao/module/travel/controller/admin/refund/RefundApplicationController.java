package cn.iocoder.yudao.module.travel.controller.admin.refund;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.travel.controller.admin.refund.vo.*;
import cn.iocoder.yudao.module.travel.service.refund.RefundApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 * 退票申请 Controller
 *
 * @author 芋道源码
 */
@Tag(name = "管理后台 - 退票申请")
@RestController
@RequestMapping("/studio/refund")
@Validated
@Slf4j
public class RefundApplicationController {

    @Resource
    private RefundApplicationService refundApplicationService;

    @PostMapping("/submit")
    @Operation(summary = "提交退票申请")
    @PreAuthorize("@ss.hasPermission('studio:refund:create')")
    public CommonResult<Long> submitRefund(@Valid @RequestBody RefundApplicationCreateReqVO createReqVO) {
        Long refundId = refundApplicationService.submitRefund(createReqVO);
        log.info("[退票申请] 提交成功，退票申请ID: {}", refundId);
        return success(refundId);
    }

    @PutMapping("/approve")
    @Operation(summary = "审核退票申请")
    @PreAuthorize("@ss.hasPermission('studio:refund:approve')")
    public CommonResult<Boolean> approveRefund(@Valid @RequestBody RefundApplicationApproveReqVO approveReqVO) {
        refundApplicationService.approveRefund(approveReqVO);
        log.info("[退票申请] 审核完成，退票申请ID: {}, 审核结果: {}", 
                approveReqVO.getId(), approveReqVO.getApproved() ? "通过" : "拒绝");
        return success(true);
    }

    @PutMapping("/execute")
    @Operation(summary = "执行退票")
    @Parameter(name = "id", description = "退票申请ID", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('studio:refund:execute')")
    public CommonResult<Boolean> executeRefund(@RequestParam("id") Long id) {
        refundApplicationService.executeRefund(id);
        log.info("[退票申请] 执行完成，退票申请ID: {}", id);
        return success(true);
    }

    @PutMapping("/cancel")
    @Operation(summary = "取消退票申请")
    @PreAuthorize("@ss.hasPermission('studio:refund:cancel')")
    public CommonResult<Boolean> cancelRefund(@Valid @RequestBody RefundApplicationCancelReqVO cancelReqVO) {
        refundApplicationService.cancelRefund(cancelReqVO);
        log.info("[退票申请] 取消成功，退票申请ID: {}", cancelReqVO.getId());
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "查询退票申请详情")
    @Parameter(name = "id", description = "退票申请ID", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('studio:refund:query')")
    public CommonResult<RefundApplicationRespVO> getRefundApplication(@RequestParam("id") Long id) {
        RefundApplicationRespVO refundApplication = refundApplicationService.getRefundApplication(id);
        return success(refundApplication);
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询退票申请")
    @PreAuthorize("@ss.hasPermission('studio:refund:query')")
    public CommonResult<PageResult<RefundApplicationRespVO>> getRefundPage(@Valid RefundApplicationPageReqVO pageReqVO) {
        PageResult<RefundApplicationRespVO> pageResult = refundApplicationService.getRefundPage(pageReqVO);
        return success(pageResult);
    }

    @GetMapping("/info")
    @Operation(summary = "获取可退票信息")
    @Parameter(name = "orderId", description = "订单ID", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('studio:refund:query')")
    public CommonResult<RefundInfoRespVO> getRefundInfo(@RequestParam("orderId") Long orderId) {
        RefundInfoRespVO refundInfo = refundApplicationService.getRefundInfo(orderId);
        return success(refundInfo);
    }

    @PostMapping("/calculate-fee")
    @Operation(summary = "计算退票费用")
    @PreAuthorize("@ss.hasPermission('studio:refund:calculate')")
    public CommonResult<RefundFeeRespVO> calculateRefundFee(@Valid @RequestBody RefundFeeCalculateReqVO calculateReqVO) {
        RefundFeeRespVO feeInfo = refundApplicationService.calculateRefundFee(calculateReqVO);
        return success(feeInfo);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出退票记录")
    @PreAuthorize("@ss.hasPermission('studio:refund:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportRefundExcel(@Valid RefundApplicationPageReqVO pageReqVO,
                                 HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<RefundApplicationRespVO> list = refundApplicationService.getRefundPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "退票记录.xlsx", "退票数据", RefundApplicationRespVO.class, list);
    }

}