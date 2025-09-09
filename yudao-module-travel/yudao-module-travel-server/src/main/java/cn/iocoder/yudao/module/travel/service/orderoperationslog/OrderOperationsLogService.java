package cn.iocoder.yudao.module.travel.service.orderoperationslog;

import java.util.*;
import jakarta.validation.*;
import cn.iocoder.yudao.module.travel.controller.admin.orderoperationslog.vo.*;
import cn.iocoder.yudao.module.travel.dal.dataobject.orderoperationslog.OrderOperationsLogDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 订单操作日志 Service 接口
 *
 * @author 芋道源码
 */
public interface OrderOperationsLogService {

    /**
     * 创建订单操作日志
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createOrderOperationsLog(@Valid OrderOperationsLogSaveReqVO createReqVO);

    /**
     * 更新订单操作日志
     *
     * @param updateReqVO 更新信息
     */
    void updateOrderOperationsLog(@Valid OrderOperationsLogSaveReqVO updateReqVO);

    /**
     * 删除订单操作日志
     *
     * @param id 编号
     */
    void deleteOrderOperationsLog(Long id);

    /**
    * 批量删除订单操作日志
    *
    * @param ids 编号
    */
    void deleteOrderOperationsLogListByIds(List<Long> ids);

    /**
     * 获得订单操作日志
     *
     * @param id 编号
     * @return 订单操作日志
     */
    OrderOperationsLogDO getOrderOperationsLog(Long id);

    /**
     * 获得订单操作日志分页
     *
     * @param pageReqVO 分页查询
     * @return 订单操作日志分页
     */
    PageResult<OrderOperationsLogDO> getOrderOperationsLogPage(OrderOperationsLogPageReqVO pageReqVO);

}