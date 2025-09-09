package cn.iocoder.yudao.module.travel.service.orders;

import java.util.*;
import jakarta.validation.*;
import cn.iocoder.yudao.module.travel.controller.admin.orders.vo.*;
import cn.iocoder.yudao.module.travel.dal.dataobject.orders.OrdersDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 订单主 Service 接口
 *
 * @author 芋道源码
 */
public interface OrdersService {

    /**
     * 创建订单主
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createOrders(@Valid OrdersSaveReqVO createReqVO);

    /**
     * 更新订单主
     *
     * @param updateReqVO 更新信息
     */
    void updateOrders(@Valid OrdersSaveReqVO updateReqVO);

    /**
     * 删除订单主
     *
     * @param id 编号
     */
    void deleteOrders(Long id);

    /**
    * 批量删除订单主
    *
    * @param ids 编号
    */
    void deleteOrdersListByIds(List<Long> ids);

    /**
     * 获得订单主
     *
     * @param id 编号
     * @return 订单主
     */
    OrdersDO getOrders(Long id);

    /**
     * 获得订单主分页
     *
     * @param pageReqVO 分页查询
     * @return 订单主分页
     */
    PageResult<OrdersDO> getOrdersPage(OrdersPageReqVO pageReqVO);

}