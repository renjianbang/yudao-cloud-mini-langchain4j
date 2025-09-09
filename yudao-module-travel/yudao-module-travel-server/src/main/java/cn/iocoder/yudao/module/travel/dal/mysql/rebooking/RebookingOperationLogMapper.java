package cn.iocoder.yudao.module.travel.dal.mysql.rebooking;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.travel.dal.dataobject.rebooking.RebookingOperationLogDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 改签操作日志 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface RebookingOperationLogMapper extends BaseMapperX<RebookingOperationLogDO> {

    default List<RebookingOperationLogDO> selectByRebookingId(Long rebookingId) {
        return selectList(RebookingOperationLogDO::getRebookingId, rebookingId);
    }

}