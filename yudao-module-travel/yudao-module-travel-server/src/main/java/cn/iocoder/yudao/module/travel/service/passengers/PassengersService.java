package cn.iocoder.yudao.module.travel.service.passengers;

import cn.iocoder.yudao.module.travel.dal.dataobject.passengers.PassengersDO;

import java.util.List;

/**
 * 乘客 Service 接口
 *
 * @author 芋道源码
 */
public interface PassengersService {

    /**
     * 根据订单ID获取乘客列表
     *
     * @param orderId 订单ID
     * @return 乘客列表
     */
    List<PassengersDO> getPassengersByOrderId(Long orderId);

    /**
     * 获取乘客信息
     *
     * @param id 乘客ID
     * @return 乘客信息
     */
    PassengersDO getPassenger(Long id);

}