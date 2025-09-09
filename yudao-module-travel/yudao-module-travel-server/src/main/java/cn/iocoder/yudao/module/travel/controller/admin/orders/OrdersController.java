package cn.iocoder.yudao.module.travel.controller.admin.orders;

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

import cn.iocoder.yudao.module.travel.controller.admin.orders.vo.*;
import cn.iocoder.yudao.module.travel.dal.dataobject.orders.OrdersDO;
import cn.iocoder.yudao.module.travel.service.orders.OrdersService;

@Tag(name = "管理后台 - 订单主")
@RestController
@RequestMapping("/studio/orders")
@Validated
public class OrdersController {

    @Resource
    private OrdersService ordersService;

    @PostMapping("/create")
    @Operation(summary = "创建订单主")
    @PreAuthorize("@ss.hasPermission('studio:orders:create')")
    public CommonResult<Long> createOrders(@Valid @RequestBody OrdersSaveReqVO createReqVO) {
        return success(ordersService.createOrders(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新订单主")
    @PreAuthorize("@ss.hasPermission('studio:orders:update')")
    public CommonResult<Boolean> updateOrders(@Valid @RequestBody OrdersSaveReqVO updateReqVO) {
        ordersService.updateOrders(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除订单主")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('studio:orders:delete')")
    public CommonResult<Boolean> deleteOrders(@RequestParam("id") Long id) {
        ordersService.deleteOrders(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除订单主")
                @PreAuthorize("@ss.hasPermission('studio:orders:delete')")
    public CommonResult<Boolean> deleteOrdersList(@RequestParam("ids") List<Long> ids) {
        ordersService.deleteOrdersListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得订单主")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('studio:orders:query')")
    public CommonResult<OrdersRespVO> getOrders(@RequestParam("id") Long id) {
        OrdersDO orders = ordersService.getOrders(id);
        return success(BeanUtils.toBean(orders, OrdersRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得订单主分页")
    @PreAuthorize("@ss.hasPermission('studio:orders:query')")
    public CommonResult<PageResult<OrdersRespVO>> getOrdersPage(@Valid OrdersPageReqVO pageReqVO) {
        PageResult<OrdersDO> pageResult = ordersService.getOrdersPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, OrdersRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出订单主 Excel")
    @PreAuthorize("@ss.hasPermission('studio:orders:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportOrdersExcel(@Valid OrdersPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<OrdersDO> list = ordersService.getOrdersPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "订单主.xls", "数据", OrdersRespVO.class,
                        BeanUtils.toBean(list, OrdersRespVO.class));
    }

}