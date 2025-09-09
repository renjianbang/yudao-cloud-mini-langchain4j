package cn.iocoder.yudao.module.travel.service.ticket;

import cn.iocoder.yudao.module.travel.controller.admin.ticket.vo.*;
import jakarta.validation.Valid;

/**
 * 出票 Service 接口
 *
 * @author 芋道源码
 */
public interface TicketService {

    /**
     * 执行订单出票操作
     *
     * @param reqVO 出票请求信息
     * @return 出票结果
     */
    TicketIssueRespVO issueTicket(@Valid TicketIssueReqVO reqVO);

}