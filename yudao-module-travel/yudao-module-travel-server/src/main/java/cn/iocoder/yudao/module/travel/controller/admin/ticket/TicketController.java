package cn.iocoder.yudao.module.travel.controller.admin.ticket;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.travel.controller.admin.ticket.vo.*;
import cn.iocoder.yudao.module.travel.service.ticket.TicketService;
import cn.iocoder.yudao.module.travel.service.base.BaseDataService;
import cn.iocoder.yudao.module.travel.service.flight.FlightService;
import cn.iocoder.yudao.module.travel.service.fee.FeeCalculateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 出票管理")
@RestController
@RequestMapping("/studio/ticket")
@Validated
public class TicketController {

    @Resource
    private TicketService ticketService;

    @Resource
    private BaseDataService baseDataService;

    @Resource
    private FlightService flightService;

    @Resource
    private FeeCalculateService feeCalculateService;

    @PostMapping("/issue")
    @Operation(summary = "执行订单出票")
    @PreAuthorize("@ss.hasPermission('studio:ticket:issue')")
    public CommonResult<TicketIssueRespVO> issueTicket(@Valid @RequestBody TicketIssueReqVO reqVO) {
        TicketIssueRespVO result = ticketService.issueTicket(reqVO);
        return success(result);
    }

    @GetMapping("/airports")
    @Operation(summary = "获取机场列表")
    @PreAuthorize("@ss.hasPermission('studio:ticket:query')")
    public CommonResult<List<AirportInfoVO>> getAirports() {
        List<AirportInfoVO> result = baseDataService.getAirports();
        return success(result);
    }

    @GetMapping("/airlines")
    @Operation(summary = "获取航司列表")
    @PreAuthorize("@ss.hasPermission('studio:ticket:query')")
    public CommonResult<List<AirlineInfoVO>> getAirlines() {
        List<AirlineInfoVO> result = baseDataService.getAirlines();
        return success(result);
    }

    @GetMapping("/validate-flight")
    @Operation(summary = "验证航班信息")
    @PreAuthorize("@ss.hasPermission('studio:ticket:validate')")
    public CommonResult<FlightValidationRespVO> validateFlight(
            @Parameter(description = "航司二字码", required = true, example = "MU") @RequestParam String airlineCode,
            @Parameter(description = "航班号", required = true, example = "MU583") @RequestParam String flightNo,
            @Parameter(description = "出发日期", required = true, example = "2024-12-01") @RequestParam String departureDate) {
        FlightValidationRespVO result = flightService.validateFlight(airlineCode, flightNo, departureDate);
        return success(result);
    }

    @PostMapping("/calculate-fees")
    @Operation(summary = "计算费用")
    @PreAuthorize("@ss.hasPermission('studio:ticket:calculate')")
    public CommonResult<FeeCalculateRespVO> calculateFees(@Valid @RequestBody FeeCalculateReqVO reqVO) {
        FeeCalculateRespVO result = feeCalculateService.calculateFees(reqVO);
        return success(result);
    }

}