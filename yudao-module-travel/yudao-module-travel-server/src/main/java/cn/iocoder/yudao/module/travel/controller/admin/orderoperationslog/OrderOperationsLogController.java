package cn.iocoder.yudao.module.travel.controller.admin.orderoperationslog;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.constraints.*;
import jakarta.validation.*;
import jakarta.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.travel.controller.admin.orderoperationslog.vo.*;
import cn.iocoder.yudao.module.travel.dal.dataobject.orderoperationslog.OrderOperationsLogDO;
import cn.iocoder.yudao.module.travel.service.orderoperationslog.OrderOperationsLogService;

@Tag(name = "管理后台 - 订单操作日志")
@RestController
@RequestMapping("/studio/order-operations-log")
@Validated
public class OrderOperationsLogController {

    @Resource
    private OrderOperationsLogService orderOperationsLogService;

    @PostMapping("/create")
    @Operation(summary = "创建订单操作日志")
    @PreAuthorize("@ss.hasPermission('studio:order-operations-log:create')")
    public CommonResult<Long> createOrderOperationsLog(@Valid @RequestBody OrderOperationsLogSaveReqVO createReqVO) {
        return success(orderOperationsLogService.createOrderOperationsLog(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新订单操作日志")
    @PreAuthorize("@ss.hasPermission('studio:order-operations-log:update')")
    public CommonResult<Boolean> updateOrderOperationsLog(@Valid @RequestBody OrderOperationsLogSaveReqVO updateReqVO) {
        orderOperationsLogService.updateOrderOperationsLog(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除订单操作日志")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('studio:order-operations-log:delete')")
    public CommonResult<Boolean> deleteOrderOperationsLog(@RequestParam("id") Long id) {
        orderOperationsLogService.deleteOrderOperationsLog(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除订单操作日志")
                @PreAuthorize("@ss.hasPermission('studio:order-operations-log:delete')")
    public CommonResult<Boolean> deleteOrderOperationsLogList(@RequestParam("ids") List<Long> ids) {
        orderOperationsLogService.deleteOrderOperationsLogListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得订单操作日志")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('studio:order-operations-log:query')")
    public CommonResult<OrderOperationsLogRespVO> getOrderOperationsLog(@RequestParam("id") Long id) {
        OrderOperationsLogDO orderOperationsLog = orderOperationsLogService.getOrderOperationsLog(id);
        return success(BeanUtils.toBean(orderOperationsLog, OrderOperationsLogRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得订单操作日志分页")
    @PreAuthorize("@ss.hasPermission('studio:order-operations-log:query')")
    public CommonResult<PageResult<OrderOperationsLogRespVO>> getOrderOperationsLogPage(@Valid OrderOperationsLogPageReqVO pageReqVO) {
        PageResult<OrderOperationsLogDO> pageResult = orderOperationsLogService.getOrderOperationsLogPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, OrderOperationsLogRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出订单操作日志 Excel")
    @PreAuthorize("@ss.hasPermission('studio:order-operations-log:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportOrderOperationsLogExcel(@Valid OrderOperationsLogPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<OrderOperationsLogDO> list = orderOperationsLogService.getOrderOperationsLogPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "订单操作日志.xls", "数据", OrderOperationsLogRespVO.class,
                        BeanUtils.toBean(list, OrderOperationsLogRespVO.class));
    }

}