package cn.iocoder.yudao.module.travel.service.rebooking;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.travel.controller.admin.rebooking.vo.*;

import java.util.List;

/**
 * 改签服务接口
 *
 * @author 芋道源码
 */
public interface RebookingService {

    /**
     * 获取改签信息
     *
     * @param orderId 订单ID
     * @return 改签信息
     */
    RebookingInfoRespVO getRebookingInfo(Long orderId);

    /**
     * 搜索可用航班
     *
     * @param reqVO 搜索请求
     * @return 航班列表
     */
    List<FlightSearchRespVO> searchFlights(FlightSearchReqVO reqVO);

    /**
     * 计算改签费用
     *
     * @param reqVO 费用计算请求
     * @return 费用信息
     */
    RebookingFeeRespVO calculateFee(RebookingFeeReqVO reqVO);

    /**
     * 提交改签申请
     *
     * @param createReqVO 创建请求
     * @return 改签申请ID
     */
    Long submitRebooking(RebookingApplicationCreateReqVO createReqVO);

    /**
     * 审核改签申请
     *
     * @param reqVO 审核请求
     */
    void approveRebooking(RebookingApproveReqVO reqVO);

    /**
     * 执行改签
     *
     * @param id 改签申请ID
     */
    void executeRebooking(Long id);

    /**
     * 获得改签申请分页
     *
     * @param pageReqVO 分页请求
     * @return 改签申请分页
     */
    PageResult<RebookingApplicationRespVO> getRebookingPage(RebookingApplicationPageReqVO pageReqVO);

    /**
     * 获取机场信息列表
     *
     * @return 机场信息列表
     */
    List<AirportInfoVO> getAirports();

    /**
     * 获取航空公司信息列表
     *
     * @return 航空公司信息列表
     */
    List<AirlineInfoVO> getAirlines();

}