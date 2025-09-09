package cn.iocoder.yudao.module.travel.service.fee;

import cn.iocoder.yudao.module.travel.controller.admin.ticket.vo.*;
import jakarta.validation.Valid;

/**
 * 费用计算 Service 接口
 *
 * @author 芋道源码
 */
public interface FeeCalculateService {

    /**
     * 计算费用
     *
     * @param reqVO 计算请求
     * @return 费用计算结果
     */
    FeeCalculateRespVO calculateFees(@Valid FeeCalculateReqVO reqVO);

}