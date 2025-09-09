package cn.iocoder.yudao.module.travel.service.orders;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.travel.controller.admin.orders.vo.*;
import cn.iocoder.yudao.module.travel.dal.dataobject.orders.OrdersDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.travel.dal.mysql.orders.OrdersMapper;


import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
//import static cn.iocoder.yudao.module.travel.enums.ErrorCodeConstants.*;

/**
 * 订单主 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class OrdersServiceImpl implements OrdersService {

    @Resource
    private OrdersMapper ordersMapper;

    @Override
    public Long createOrders(OrdersSaveReqVO createReqVO) {
        // 插入
        OrdersDO orders = BeanUtils.toBean(createReqVO, OrdersDO.class);
        ordersMapper.insert(orders);

        // 返回
        return orders.getId();
    }

    @Override
    public void updateOrders(OrdersSaveReqVO updateReqVO) {
        // 校验存在
        validateOrdersExists(updateReqVO.getId());
        // 更新
        OrdersDO updateObj = BeanUtils.toBean(updateReqVO, OrdersDO.class);
        ordersMapper.updateById(updateObj);
    }

    @Override
    public void deleteOrders(Long id) {
        // 校验存在
        validateOrdersExists(id);
        // 删除
        ordersMapper.deleteById(id);
    }

    @Override
        public void deleteOrdersListByIds(List<Long> ids) {
        // 删除
        ordersMapper.deleteByIds(ids);
        }


    private void validateOrdersExists(Long id) {
        if (ordersMapper.selectById(id) == null) {
//            throw exception(ORDERS_NOT_EXISTS);
        }
    }

    @Override
    public OrdersDO getOrders(Long id) {
        return ordersMapper.selectById(id);
    }

    @Override
    public PageResult<OrdersDO> getOrdersPage(OrdersPageReqVO pageReqVO) {
        return ordersMapper.selectPage(pageReqVO);
    }

}