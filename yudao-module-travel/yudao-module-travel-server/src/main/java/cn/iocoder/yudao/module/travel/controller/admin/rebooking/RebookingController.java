package cn.iocoder.yudao.module.travel.controller.admin.rebooking;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.travel.controller.admin.rebooking.vo.*;
import cn.iocoder.yudao.module.travel.service.rebooking.RebookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 改签管理")
@RestController
@RequestMapping("/admin-api/studio/rebooking")
@Validated
@Slf4j
public class RebookingController {

    @Autowired
    private RebookingService rebookingService;

    @GetMapping("/info")
    @Operation(summary = "获取改签信息")
    @Parameter(name = "orderId", description = "订单ID", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('studio:rebooking:query')")
    public CommonResult<RebookingInfoRespVO> getRebookingInfo(@RequestParam("orderId") Long orderId) {
        RebookingInfoRespVO respVO = rebookingService.getRebookingInfo(orderId);
        return success(respVO);
    }

    @PostMapping("/search-flights")
    @Operation(summary = "搜索可用航班")
    @PreAuthorize("@ss.hasPermission('studio:rebooking:query')")
    public CommonResult<List<FlightSearchRespVO>> searchFlights(@Valid @RequestBody FlightSearchReqVO reqVO) {
        List<FlightSearchRespVO> flights = rebookingService.searchFlights(reqVO);
        return success(flights);
    }

    @PostMapping("/calculate-fee")
    @Operation(summary = "计算改签费用")
    @PreAuthorize("@ss.hasPermission('studio:rebooking:query')")
    public CommonResult<RebookingFeeRespVO> calculateFee(@Valid @RequestBody RebookingFeeReqVO reqVO) {
        RebookingFeeRespVO feeVO = rebookingService.calculateFee(reqVO);
        return success(feeVO);
    }

    @PostMapping("/submit")
    @Operation(summary = "提交改签申请")
    // @PreAuthorize("@ss.hasPermission('studio:rebooking:create')")
    public CommonResult<Long> submitRebooking(@Valid @RequestBody RebookingApplicationCreateReqVO createReqVO) {
        Long rebookingId = rebookingService.submitRebooking(createReqVO);
        return success(rebookingId);
    }

    @PutMapping("/approve")
    @Operation(summary = "审核改签申请")
    @PreAuthorize("@ss.hasPermission('studio:rebooking:approve')")
    public CommonResult<Boolean> approveRebooking(@Valid @RequestBody RebookingApproveReqVO reqVO) {
        rebookingService.approveRebooking(reqVO);
        return success(true);
    }

    @PutMapping("/execute")
    @Operation(summary = "执行改签")
    @Parameter(name = "id", description = "改签申请ID", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('studio:rebooking:execute')")
    public CommonResult<Boolean> executeRebooking(@RequestParam("id") Long id) {
        rebookingService.executeRebooking(id);
        return success(true);
    }

    @GetMapping("/page")
    @Operation(summary = "获得改签申请分页")
    @PreAuthorize("@ss.hasPermission('studio:rebooking:query')")
    public CommonResult<PageResult<RebookingApplicationRespVO>> getRebookingPage(@Valid RebookingApplicationPageReqVO pageReqVO) {
        PageResult<RebookingApplicationRespVO> pageResult = rebookingService.getRebookingPage(pageReqVO);
        return success(pageResult);
    }

    @GetMapping("/airports")
    @Operation(summary = "获取机场信息列表")
    // @PreAuthorize("@ss.hasPermission('studio:rebooking:query')")
    public CommonResult<List<AirportInfoVO>> getAirports() {
        List<AirportInfoVO> airports = rebookingService.getAirports();
        return success(airports);
    }

    @GetMapping("/airlines")
    @Operation(summary = "获取航空公司信息列表")
    @PreAuthorize("@ss.hasPermission('studio:rebooking:query')")
    public CommonResult<List<AirlineInfoVO>> getAirlines() {
        List<AirlineInfoVO> airlines = rebookingService.getAirlines();
        return success(airlines);
    }

}