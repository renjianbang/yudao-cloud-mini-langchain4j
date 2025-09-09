package cn.iocoder.yudao.module.travel.controller.admin.flightsegments;

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

import cn.iocoder.yudao.module.travel.controller.admin.flightsegments.vo.*;
import cn.iocoder.yudao.module.travel.dal.dataobject.flightsegments.FlightSegmentsDO;
import cn.iocoder.yudao.module.travel.service.flightsegments.FlightSegmentsService;

@Tag(name = "管理后台 - 航班航段")
@RestController
@RequestMapping("/studio/flight-segments")
@Validated
public class FlightSegmentsController {

    @Resource
    private FlightSegmentsService flightSegmentsService;

    @PostMapping("/create")
    @Operation(summary = "创建航班航段")
    @PreAuthorize("@ss.hasPermission('studio:flight-segments:create')")
    public CommonResult<Long> createFlightSegments(@Valid @RequestBody FlightSegmentsSaveReqVO createReqVO) {
        return success(flightSegmentsService.createFlightSegments(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新航班航段")
    @PreAuthorize("@ss.hasPermission('studio:flight-segments:update')")
    public CommonResult<Boolean> updateFlightSegments(@Valid @RequestBody FlightSegmentsSaveReqVO updateReqVO) {
        flightSegmentsService.updateFlightSegments(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除航班航段")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('studio:flight-segments:delete')")
    public CommonResult<Boolean> deleteFlightSegments(@RequestParam("id") Long id) {
        flightSegmentsService.deleteFlightSegments(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除航班航段")
                @PreAuthorize("@ss.hasPermission('studio:flight-segments:delete')")
    public CommonResult<Boolean> deleteFlightSegmentsList(@RequestParam("ids") List<Long> ids) {
        flightSegmentsService.deleteFlightSegmentsListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得航班航段")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('studio:flight-segments:query')")
    public CommonResult<FlightSegmentsRespVO> getFlightSegments(@RequestParam("id") Long id) {
        FlightSegmentsDO flightSegments = flightSegmentsService.getFlightSegments(id);
        return success(BeanUtils.toBean(flightSegments, FlightSegmentsRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得航班航段分页")
    @PreAuthorize("@ss.hasPermission('studio:flight-segments:query')")
    public CommonResult<PageResult<FlightSegmentsRespVO>> getFlightSegmentsPage(@Valid FlightSegmentsPageReqVO pageReqVO) {
        PageResult<FlightSegmentsDO> pageResult = flightSegmentsService.getFlightSegmentsPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, FlightSegmentsRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出航班航段 Excel")
    @PreAuthorize("@ss.hasPermission('studio:flight-segments:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportFlightSegmentsExcel(@Valid FlightSegmentsPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<FlightSegmentsDO> list = flightSegmentsService.getFlightSegmentsPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "航班航段.xls", "数据", FlightSegmentsRespVO.class,
                        BeanUtils.toBean(list, FlightSegmentsRespVO.class));
    }

}