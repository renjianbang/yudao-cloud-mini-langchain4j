package cn.iocoder.yudao.module.travel.service.passengers;

import cn.iocoder.yudao.module.travel.dal.dataobject.passengers.PassengersDO;
import cn.iocoder.yudao.module.travel.dal.mysql.passengers.PassengersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * 乘客 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class PassengersServiceImpl implements PassengersService {

    @Autowired
    private PassengersMapper passengersMapper;

    @Override
    public List<PassengersDO> getPassengersByOrderId(Long orderId) {
        return passengersMapper.selectListByOrderId(orderId);
    }

    @Override
    public PassengersDO getPassenger(Long id) {
        return passengersMapper.selectById(id);
    }

}