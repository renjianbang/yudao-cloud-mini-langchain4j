package cn.iocoder.yudao.module.travel.dal.dataobject.passengers;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 乘客 DO
 *
 * @author 芋道源码
 */
@TableName("studio_passengers")
@KeySequence("studio_passengers_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PassengersDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    /**
     * 关联订单ID
     */
    private Long orderId;
    /**
     * 乘客类型 (1: 成人, 2: 儿童, 3: 婴儿)
     */
    private Integer passengerType;
    /**
     * 中文姓名
     */
    private String chineseName;
    /**
     * 英文姓名
     */
    private String englishName;
    /**
     * 性别 (MALE: 男, FEMALE: 女)
     */
    private String gender;
    /**
     * 出生日期
     */
    private LocalDate birthday;
    /**
     * 证件类型 (ID_CARD: 身份证, PASSPORT: 护照, OTHER: 其他)
     */
    private String idType;
    /**
     * 证件号码
     */
    private String idNumber;
    /**
     * 证件有效期
     */
    private LocalDate idExpiryDate;
    /**
     * 国籍
     */
    private String nationality;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 常旅客号
     */
    private String frequentFlyerNo;
    /**
     * 餐食偏好
     */
    private String mealPreference;
    /**
     * 座位偏好
     */
    private String seatPreference;
    /**
     * 特殊服务需求
     */
    private String specialService;
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    /**
     * 最后更新时间
     */
    private LocalDateTime updatedAt;

}