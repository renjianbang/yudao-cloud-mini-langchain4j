package cn.iocoder.yudao.module.travel.service.orderfees;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.travel.controller.admin.orderfees.vo.*;
import cn.iocoder.yudao.module.travel.dal.dataobject.orderfees.OrderFeesDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.travel.dal.mysql.orderfees.OrderFeesMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
//import static cn.iocoder.yudao.module.travel.enums.ErrorCodeConstants.*;

/**
 * 订单费用明细 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class OrderFeesServiceImpl implements OrderFeesService {

    @Resource
    private OrderFeesMapper orderFeesMapper;

    @Override
    public Long createOrderFees(OrderFeesSaveReqVO createReqVO) {
        // 插入
        OrderFeesDO orderFees = BeanUtils.toBean(createReqVO, OrderFeesDO.class);
        orderFeesMapper.insert(orderFees);

        // 返回
        return orderFees.getId();
    }

    @Override
    public void updateOrderFees(OrderFeesSaveReqVO updateReqVO) {
        // 校验存在
        validateOrderFeesExists(updateReqVO.getId());
        // 更新
        OrderFeesDO updateObj = BeanUtils.toBean(updateReqVO, OrderFeesDO.class);
        orderFeesMapper.updateById(updateObj);
    }

    @Override
    public void deleteOrderFees(Long id) {
        // 校验存在
        validateOrderFeesExists(id);
        // 删除
        orderFeesMapper.deleteById(id);
    }

    @Override
        public void deleteOrderFeesListByIds(List<Long> ids) {
        // 删除
        orderFeesMapper.deleteByIds(ids);
        }


    private void validateOrderFeesExists(Long id) {
        if (orderFeesMapper.selectById(id) == null) {
//            throw exception(ORDER_FEES_NOT_EXISTS);
        }
    }

    @Override
    public OrderFeesDO getOrderFees(Long id) {
        return orderFeesMapper.selectById(id);
    }

    @Override
    public PageResult<OrderFeesDO> getOrderFeesPage(OrderFeesPageReqVO pageReqVO) {
        return orderFeesMapper.selectPage(pageReqVO);
    }

}