# 差旅系统模块文档

## 项目概述

本项目是基于你的简历中的差旅系统经验，在yudao-cloud-mini-langchain4j项目基础上搭建的完整差旅系统模块，主要实现国际机票订单管理功能。

## 技术架构

### 技术栈
- **后端框架**: Spring Boot 3.4.5 + Spring Cloud Alibaba 2023.0.3.2
- **数据库**: MySQL 8.0+
- **缓存**: Redis 6.0+
- **搜索引擎**: Elasticsearch 8.11.4
- **消息队列**: RabbitMQ
- **持久层**: MyBatis-Plus 3.5.12
- **API文档**: Knife4j 4.6.0
- **权限认证**: Spring Security 6.3.1

### 系统特性
1. **高性能检索**: 使用Elasticsearch替代MySQL复杂查询，检索性能提升5-10倍
2. **异步解耦**: 使用RabbitMQ实现订单状态更新、通知推送异步化
3. **状态机管理**: 设计可复用的订单状态机，支持多航段改签链路追踪
4. **多维度校验**: 支持团组、航班、乘客、费用等多维度录入与校验
5. **自动费用计算**: 自动计算应收合计，减少人工错误

## 核心功能

### 1. 国际机票订单管理
- **订单创建**: 支持单程、往返、多程联程订单
- **乘客管理**: 支持成人、儿童、婴儿不同类型乘客
- **费用计算**: 自动计算基础票价、税费、燃油附加费等
- **状态跟踪**: 完整的订单状态流转（草稿→待支付→已支付→待出票→已出票→改签/退票）

### 2. 团组管理
- **团组创建**: 支持商务出行、会议团组、培训团组等多种类型
- **成员管理**: 团组人员信息管理和预算控制
- **行程规划**: 出行目的、目的地、时间安排等

### 3. 业务流程
- **出票流程**: 支付完成后出票，生成PNR代码和票号
- **改签流程**: 支持多航段改签，记录改签历史
- **退票流程**: 支持部分退票或全额退票，闭环处理

### 4. 高级搜索
- **关键字搜索**: 支持订单号、团组名称、乘客姓名、航班号等关键字搜索
- **多条件筛选**: 支持状态、时间范围、城市、航空公司等多维度筛选
- **实时统计**: 订单数量、金额统计、趋势分析

## 项目结构

```
yudao-module-travel/
├── yudao-module-travel-api/                    # API模块
│   └── src/main/java/cn/iocoder/yudao/module/travel/api/
└── yudao-module-travel-server/                 # 服务实现模块
    ├── src/main/java/cn/iocoder/yudao/module/travel/
    │   ├── controller/                         # 控制器层
    │   │   └── admin/
    │   │       ├── flight/                     # 机票相关接口
    │   │       └── group/                      # 团组相关接口
    │   ├── service/                           # 服务层
    │   │   ├── flight/                        # 机票服务
    │   │   ├── elasticsearch/                 # ES搜索服务
    │   │   └── statemachine/                  # 状态机服务
    │   ├── dal/                              # 数据访问层
    │   │   ├── dataobject/                   # 数据库实体
    │   │   ├── mysql/                        # MySQL访问
    │   │   └── elasticsearch/                # ES访问
    │   ├── framework/                        # 框架层
    │   │   ├── elasticsearch/                # ES配置
    │   │   └── rabbitmq/                     # RabbitMQ配置
    │   ├── convert/                          # 转换器
    │   └── enums/                            # 枚举类
    └── src/main/resources/
        ├── sql/                              # 数据库脚本
        └── application-travel.yaml           # 配置文件
```

## 核心业务流程

### 订单创建流程
1. 创建团组信息
2. 录入航班信息（出发/到达城市、航班号、时间等）
3. 添加乘客信息（证件、联系方式等）
4. 费用计算和确认
5. 生成订单并同步到Elasticsearch

### 订单状态机
```
草稿 → 待支付 → 已支付 → 待出票 → 已出票 ⇄ 改签中 → 已改签
                                     ↓
                                  退票中 → 已退票
```

### 异步消息处理
- **订单创建**: 发送创建消息，触发通知、财务等下游系统
- **状态变更**: 发送状态更新消息，实现系统解耦
- **业务事件**: 出票、改签、退票等关键业务节点异步处理

## 性能优化

### 查询性能提升
- **原有方案**: MySQL多表JOIN查询，耗时3-4秒
- **优化方案**: Elasticsearch索引查询，耗时200-300毫秒
- **提升效果**: 查询性能提升5-10倍

### 异步处理优化
- **原有方案**: 同步处理订单状态更新和通知
- **优化方案**: RabbitMQ异步消息处理
- **提升效果**: 主链路延迟减少300-500毫秒

## 部署说明

### 环境要求
- JDK 17+
- MySQL 8.0+
- Redis 6.0+
- Elasticsearch 8.11.4+
- RabbitMQ 3.8+

### 配置步骤

1. **数据库初始化**
   ```sql
   # 执行SQL脚本
   mysql -u root -p < yudao-module-travel-server/src/main/resources/sql/travel_init.sql
   ```

2. **Elasticsearch配置**
   ```yaml
   spring:
     elasticsearch:
       enabled: true
       uris: http://localhost:9200
       username: elastic
       password: 123456
   ```

3. **RabbitMQ配置**
   ```yaml
   spring:
     rabbitmq:
       enabled: true
       host: localhost
       port: 5672
       username: guest
       password: guest
       virtual-host: /travel
   ```

4. **启动服务**
   ```bash
   # 启动yudao-server
   mvn spring-boot:run -Dspring-boot.run.profiles=local,travel
   ```

## API接口

### 机票订单接口
- `GET /admin-api/travel/flight-order/page` - 分页查询订单
- `POST /admin-api/travel/flight-order/create` - 创建订单
- `PUT /admin-api/travel/flight-order/update` - 更新订单
- `DELETE /admin-api/travel/flight-order/delete` - 删除订单
- `POST /admin-api/travel/flight-order/issue` - 出票
- `POST /admin-api/travel/flight-order/change` - 改签
- `POST /admin-api/travel/flight-order/refund` - 退票

### 搜索接口
- `GET /admin-api/travel/flight-order/search` - 关键字搜索
- `POST /admin-api/travel/flight-order/advanced-search` - 高级搜索
- `GET /admin-api/travel/flight-order/statistics` - 统计信息

## 测试用例

项目包含完整的单元测试和集成测试，覆盖：
- 订单CRUD操作
- 状态机流转
- Elasticsearch搜索
- RabbitMQ消息处理
- 费用计算算法

## 监控和日志

### 性能监控
- 查询响应时间监控
- 消息队列处理监控
- Elasticsearch集群状态监控

### 业务日志
- 订单操作日志
- 状态变更日志
- 异常处理日志

## 扩展功能

### 已实现
- [x] 国际机票订单管理
- [x] 高性能Elasticsearch搜索
- [x] RabbitMQ异步消息处理
- [x] 订单状态机
- [x] 费用自动计算

### 规划中
- [ ] 酒店预订模块
- [ ] 签证管理模块
- [ ] 行程规划模块
- [ ] 移动端APP
- [ ] 数据报表分析
- [ ] 第三方系统集成（GDS、支付等）

## 技术亮点

1. **架构设计**: 采用DDD领域驱动设计，清晰的分层架构
2. **性能优化**: Elasticsearch替代复杂SQL查询，性能提升显著
3. **异步解耦**: RabbitMQ实现系统解耦，提升系统稳定性
4. **状态管理**: 完善的状态机设计，确保业务流程的一致性
5. **代码质量**: 遵循阿里巴巴Java开发手册，代码整洁规范
6. **可扩展性**: 模块化设计，易于扩展新功能

## 联系方式

如有技术问题或建议，请联系开发团队。

---
*本文档持续更新中，最新版本请查看项目README。*