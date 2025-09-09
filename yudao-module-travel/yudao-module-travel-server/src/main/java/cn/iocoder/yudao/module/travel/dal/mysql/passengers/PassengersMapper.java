package cn.iocoder.yudao.module.travel.dal.mysql.passengers;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.travel.dal.dataobject.passengers.PassengersDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.travel.controller.admin.passengers.vo.*;

/**
 * 乘客 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface PassengersMapper extends BaseMapperX<PassengersDO> {

    default PageResult<PassengersDO> selectPage(PassengersPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<PassengersDO>()
                .eqIfPresent(PassengersDO::getOrderId, reqVO.getOrderId())
                .eqIfPresent(PassengersDO::getPassengerType, reqVO.getPassengerType())
                .likeIfPresent(PassengersDO::getChineseName, reqVO.getChineseName())
                .likeIfPresent(PassengersDO::getEnglishName, reqVO.getEnglishName())
                .eqIfPresent(PassengersDO::getGender, reqVO.getGender())
                .eqIfPresent(PassengersDO::getIdType, reqVO.getIdType())
                .likeIfPresent(PassengersDO::getIdNumber, reqVO.getIdNumber())
                .eqIfPresent(PassengersDO::getNationality, reqVO.getNationality())
                .betweenIfPresent(PassengersDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(PassengersDO::getId));
    }

    /**
     * 根据订单ID查询乘客列表
     */
    default List<PassengersDO> selectListByOrderId(Long orderId) {
        return selectList(PassengersDO::getOrderId, orderId);
    }

    /**
     * 根据证件信息查询乘客
     */
    default PassengersDO selectByIdNumber(String idType, String idNumber) {
        return selectOne(new LambdaQueryWrapperX<PassengersDO>()
                .eq(PassengersDO::getIdType, idType)
                .eq(PassengersDO::getIdNumber, idNumber));
    }

}