package cn.iocoder.yudao.module.travel.service.base;

import cn.iocoder.yudao.module.travel.controller.admin.ticket.vo.*;
import java.util.List;

/**
 * 基础数据 Service 接口
 *
 * @author 芋道源码
 */
public interface BaseDataService {

    /**
     * 获取所有可用机场信息
     *
     * @return 机场信息列表
     */
    List<AirportInfoVO> getAirports();

    /**
     * 获取所有可用航空公司信息
     *
     * @return 航空公司信息列表
     */
    List<AirlineInfoVO> getAirlines();

}