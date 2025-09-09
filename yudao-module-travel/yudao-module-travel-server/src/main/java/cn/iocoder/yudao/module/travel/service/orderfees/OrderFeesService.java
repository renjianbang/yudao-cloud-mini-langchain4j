package cn.iocoder.yudao.module.travel.service.orderfees;

import java.util.*;
import jakarta.validation.*;
import cn.iocoder.yudao.module.travel.controller.admin.orderfees.vo.*;
import cn.iocoder.yudao.module.travel.dal.dataobject.orderfees.OrderFeesDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 订单费用明细 Service 接口
 *
 * @author 芋道源码
 */
public interface OrderFeesService {

    /**
     * 创建订单费用明细
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createOrderFees(@Valid OrderFeesSaveReqVO createReqVO);

    /**
     * 更新订单费用明细
     *
     * @param updateReqVO 更新信息
     */
    void updateOrderFees(@Valid OrderFeesSaveReqVO updateReqVO);

    /**
     * 删除订单费用明细
     *
     * @param id 编号
     */
    void deleteOrderFees(Long id);

    /**
    * 批量删除订单费用明细
    *
    * @param ids 编号
    */
    void deleteOrderFeesListByIds(List<Long> ids);

    /**
     * 获得订单费用明细
     *
     * @param id 编号
     * @return 订单费用明细
     */
    OrderFeesDO getOrderFees(Long id);

    /**
     * 获得订单费用明细分页
     *
     * @param pageReqVO 分页查询
     * @return 订单费用明细分页
     */
    PageResult<OrderFeesDO> getOrderFeesPage(OrderFeesPageReqVO pageReqVO);

}