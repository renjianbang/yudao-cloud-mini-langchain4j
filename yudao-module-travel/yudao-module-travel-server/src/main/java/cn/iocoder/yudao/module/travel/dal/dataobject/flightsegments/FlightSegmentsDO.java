package cn.iocoder.yudao.module.travel.dal.dataobject.flightsegments;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 航班航段 DO
 *
 * @author 芋道源码
 */
@TableName("studio_flight_segments")
@KeySequence("studio_flight_segments_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightSegmentsDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    private Boolean deleted;
    /**
     * 关联的订单ID
     */
    private Long orderId;
    /**
     * 关联的乘客ID
     */
    private Long passengerId;
    /**
     * 航段类型 (1: 去程, 2: 返程)
     */
    private Integer segmentType;
    /**
     * 航司二字码 (例如: MU, CA)
     */
    private String airlineCode;
    /**
     * 航班号 (例如: MU583)
     */
    private String flightNo;
    /**
     * 出发机场三字码 (例如: PVG)
     */
    private String departureAirportCode;
    /**
     * 到达机场三字码 (例如: LAX)
     */
    private String arrivalAirportCode;
    /**
     * 计划出发时间 (当地时间)
     */
    private LocalDateTime departureTime;
    /**
     * 计划到达时间 (当地时间)
     */
    private LocalDateTime arrivalTime;
    /**
     * 舱位等级 (例如: ECONOMY, BUSINESS)
     */
    private String cabinClass;
    /**
     * 电子票号 (出票后回填)
     */
    private String ticketNo;
    /**
     * 航段状态 (10: 正常, 20: 已改签, 30: 已退票)
     */
    private Integer status;
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    /**
     * 最后更新时间
     */
    private LocalDateTime updatedAt;


}
