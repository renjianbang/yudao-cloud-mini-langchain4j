package cn.iocoder.yudao.module.travel.controller.admin.orderfees;

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

import cn.iocoder.yudao.module.travel.controller.admin.orderfees.vo.*;
import cn.iocoder.yudao.module.travel.dal.dataobject.orderfees.OrderFeesDO;
import cn.iocoder.yudao.module.travel.service.orderfees.OrderFeesService;

@Tag(name = "管理后台 - 订单费用明细")
@RestController
@RequestMapping("/studio/order-fees")
@Validated
public class OrderFeesController {

    @Resource
    private OrderFeesService orderFeesService;

    @PostMapping("/create")
    @Operation(summary = "创建订单费用明细")
    @PreAuthorize("@ss.hasPermission('studio:order-fees:create')")
    public CommonResult<Long> createOrderFees(@Valid @RequestBody OrderFeesSaveReqVO createReqVO) {
        return success(orderFeesService.createOrderFees(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新订单费用明细")
    @PreAuthorize("@ss.hasPermission('studio:order-fees:update')")
    public CommonResult<Boolean> updateOrderFees(@Valid @RequestBody OrderFeesSaveReqVO updateReqVO) {
        orderFeesService.updateOrderFees(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除订单费用明细")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('studio:order-fees:delete')")
    public CommonResult<Boolean> deleteOrderFees(@RequestParam("id") Long id) {
        orderFeesService.deleteOrderFees(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除订单费用明细")
                @PreAuthorize("@ss.hasPermission('studio:order-fees:delete')")
    public CommonResult<Boolean> deleteOrderFeesList(@RequestParam("ids") List<Long> ids) {
        orderFeesService.deleteOrderFeesListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得订单费用明细")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('studio:order-fees:query')")
    public CommonResult<OrderFeesRespVO> getOrderFees(@RequestParam("id") Long id) {
        OrderFeesDO orderFees = orderFeesService.getOrderFees(id);
        return success(BeanUtils.toBean(orderFees, OrderFeesRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得订单费用明细分页")
    @PreAuthorize("@ss.hasPermission('studio:order-fees:query')")
    public CommonResult<PageResult<OrderFeesRespVO>> getOrderFeesPage(@Valid OrderFeesPageReqVO pageReqVO) {
        PageResult<OrderFeesDO> pageResult = orderFeesService.getOrderFeesPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, OrderFeesRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出订单费用明细 Excel")
    @PreAuthorize("@ss.hasPermission('studio:order-fees:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportOrderFeesExcel(@Valid OrderFeesPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<OrderFeesDO> list = orderFeesService.getOrderFeesPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "订单费用明细.xls", "数据", OrderFeesRespVO.class,
                        BeanUtils.toBean(list, OrderFeesRespVO.class));
    }

}