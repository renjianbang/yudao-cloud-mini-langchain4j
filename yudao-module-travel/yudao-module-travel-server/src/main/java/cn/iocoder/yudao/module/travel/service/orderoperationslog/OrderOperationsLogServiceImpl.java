package cn.iocoder.yudao.module.travel.service.orderoperationslog;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.travel.controller.admin.orderoperationslog.vo.*;
import cn.iocoder.yudao.module.travel.dal.dataobject.orderoperationslog.OrderOperationsLogDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.travel.dal.mysql.orderoperationslog.OrderOperationsLogMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;


/**
 * 订单操作日志 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class OrderOperationsLogServiceImpl implements OrderOperationsLogService {

    @Resource
    private OrderOperationsLogMapper orderOperationsLogMapper;

    @Override
    public Long createOrderOperationsLog(OrderOperationsLogSaveReqVO createReqVO) {
        // 插入
        OrderOperationsLogDO orderOperationsLog = BeanUtils.toBean(createReqVO, OrderOperationsLogDO.class);
        orderOperationsLogMapper.insert(orderOperationsLog);

        // 返回
        return orderOperationsLog.getId();
    }

    @Override
    public void updateOrderOperationsLog(OrderOperationsLogSaveReqVO updateReqVO) {
        // 校验存在
        validateOrderOperationsLogExists(updateReqVO.getId());
        // 更新
        OrderOperationsLogDO updateObj = BeanUtils.toBean(updateReqVO, OrderOperationsLogDO.class);
        orderOperationsLogMapper.updateById(updateObj);
    }

    @Override
    public void deleteOrderOperationsLog(Long id) {
        // 校验存在
        validateOrderOperationsLogExists(id);
        // 删除
        orderOperationsLogMapper.deleteById(id);
    }

    @Override
        public void deleteOrderOperationsLogListByIds(List<Long> ids) {
        // 删除
        orderOperationsLogMapper.deleteByIds(ids);
        }


    private void validateOrderOperationsLogExists(Long id) {
        if (orderOperationsLogMapper.selectById(id) == null) {
//            throw exception(ORDER_OPERATIONS_LOG_NOT_EXISTS);
        }
    }

    @Override
    public OrderOperationsLogDO getOrderOperationsLog(Long id) {
        return orderOperationsLogMapper.selectById(id);
    }

    @Override
    public PageResult<OrderOperationsLogDO> getOrderOperationsLogPage(OrderOperationsLogPageReqVO pageReqVO) {
        return orderOperationsLogMapper.selectPage(pageReqVO);
    }

}