package cn.iocoder.yudao.module.travel.service.refund;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.travel.controller.admin.refund.vo.*;

import jakarta.validation.Valid;

/**
 * 退票申请 Service 接口
 * 
 * @author 芋道源码
 */
public interface RefundApplicationService {

    /**
     * 提交退票申请
     *
     * @param createReqVO 创建信息
     * @return 退票申请ID
     */
    Long submitRefund(@Valid RefundApplicationCreateReqVO createReqVO);

    /**
     * 审核退票申请
     *
     * @param approveReqVO 审核信息
     */
    void approveRefund(@Valid RefundApplicationApproveReqVO approveReqVO);

    /**
     * 执行退票
     *
     * @param id 退票申请ID
     */
    void executeRefund(Long id);

    /**
     * 取消退票申请
     *
     * @param cancelReqVO 取消信息
     */
    void cancelRefund(@Valid RefundApplicationCancelReqVO cancelReqVO);

    /**
     * 计算退票费用
     *
     * @param calculateReqVO 计算信息
     * @return 费用信息
     */
    RefundFeeRespVO calculateRefundFee(@Valid RefundFeeCalculateReqVO calculateReqVO);

    /**
     * 获得退票申请
     *
     * @param id 编号
     * @return 退票申请
     */
    RefundApplicationRespVO getRefundApplication(Long id);

    /**
     * 获得退票申请分页
     *
     * @param pageReqVO 分页查询
     * @return 退票申请分页
     */
    PageResult<RefundApplicationRespVO> getRefundPage(@Valid RefundApplicationPageReqVO pageReqVO);

    /**
     * 获取可退票信息
     *
     * @param orderId 订单ID
     * @return 可退票信息
     */
    RefundInfoRespVO getRefundInfo(Long orderId);

}