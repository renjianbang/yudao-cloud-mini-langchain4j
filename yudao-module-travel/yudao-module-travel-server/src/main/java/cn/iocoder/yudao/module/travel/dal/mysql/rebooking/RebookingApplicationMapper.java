package cn.iocoder.yudao.module.travel.dal.mysql.rebooking;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.travel.controller.admin.rebooking.vo.RebookingApplicationPageReqVO;
import cn.iocoder.yudao.module.travel.dal.dataobject.rebooking.RebookingApplicationDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 改签申请 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface RebookingApplicationMapper extends BaseMapperX<RebookingApplicationDO> {

    default PageResult<RebookingApplicationDO> selectPage(RebookingApplicationPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<RebookingApplicationDO>()
                .eqIfPresent(RebookingApplicationDO::getOrderId, reqVO.getOrderId())
                .eqIfPresent(RebookingApplicationDO::getPassengerId, reqVO.getPassengerId())
                .eqIfPresent(RebookingApplicationDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(RebookingApplicationDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(RebookingApplicationDO::getId));
    }

}