package cn.iocoder.yudao.module.travel.service.fee;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.module.travel.controller.admin.ticket.vo.*;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 费用计算 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class FeeCalculateServiceImpl implements FeeCalculateService {

    // 基础票价配置（实际项目中应该从数据库或外部API获取）
    private static final Map<String, RoutePrice> ROUTE_PRICES = new HashMap<>();
    
    // 舱位等级价格倍率
    private static final Map<String, BigDecimal> CABIN_CLASS_MULTIPLIER = new HashMap<>();
    
    // 乘客类型价格倍率
    private static final Map<Integer, BigDecimal> PASSENGER_TYPE_MULTIPLIER = new HashMap<>();

    static {
        // 初始化航线价格（基础经济舱价格）
        ROUTE_PRICES.put("PVG-LAX", new RoutePrice(new BigDecimal("5800"), new BigDecimal("50"), new BigDecimal("300")));
        ROUTE_PRICES.put("LAX-PVG", new RoutePrice(new BigDecimal("5800"), new BigDecimal("50"), new BigDecimal("300")));
        ROUTE_PRICES.put("PEK-JFK", new RoutePrice(new BigDecimal("6200"), new BigDecimal("60"), new BigDecimal("350")));
        ROUTE_PRICES.put("JFK-PEK", new RoutePrice(new BigDecimal("6200"), new BigDecimal("60"), new BigDecimal("350")));
        ROUTE_PRICES.put("CAN-SIN", new RoutePrice(new BigDecimal("1800"), new BigDecimal("30"), new BigDecimal("150")));
        ROUTE_PRICES.put("SIN-CAN", new RoutePrice(new BigDecimal("1800"), new BigDecimal("30"), new BigDecimal("150")));
        ROUTE_PRICES.put("PVG-NRT", new RoutePrice(new BigDecimal("2200"), new BigDecimal("40"), new BigDecimal("180")));
        ROUTE_PRICES.put("NRT-PVG", new RoutePrice(new BigDecimal("2200"), new BigDecimal("40"), new BigDecimal("180")));
        ROUTE_PRICES.put("PEK-ICN", new RoutePrice(new BigDecimal("1500"), new BigDecimal("25"), new BigDecimal("120")));
        ROUTE_PRICES.put("ICN-PEK", new RoutePrice(new BigDecimal("1500"), new BigDecimal("25"), new BigDecimal("120")));

        // 舱位等级倍率
        CABIN_CLASS_MULTIPLIER.put("ECONOMY", new BigDecimal("1.0"));
        CABIN_CLASS_MULTIPLIER.put("PREMIUM_ECONOMY", new BigDecimal("1.5"));
        CABIN_CLASS_MULTIPLIER.put("BUSINESS", new BigDecimal("3.0"));
        CABIN_CLASS_MULTIPLIER.put("FIRST", new BigDecimal("5.0"));

        // 乘客类型倍率
        PASSENGER_TYPE_MULTIPLIER.put(1, new BigDecimal("1.0")); // 成人
        PASSENGER_TYPE_MULTIPLIER.put(2, new BigDecimal("0.75")); // 儿童
        PASSENGER_TYPE_MULTIPLIER.put(3, new BigDecimal("0.1")); // 婴儿
    }

    @Override
    public FeeCalculateRespVO calculateFees(FeeCalculateReqVO reqVO) {
        List<FeeCalculateRespVO.FeeDetail> feeDetails = new ArrayList<>();
        BigDecimal totalBasePrice = BigDecimal.ZERO;
        BigDecimal totalTaxes = BigDecimal.ZERO;
        BigDecimal totalOtherFees = BigDecimal.ZERO;

        // 为每个乘客的每个航段计算费用
        for (FeeCalculateReqVO.PassengerSimpleInfo passenger : reqVO.getPassengers()) {
            for (FeeCalculateReqVO.FlightSegmentSimpleInfo segment : reqVO.getFlightSegments()) {
                // 计算基础票价
                BigDecimal basePrice = calculateBasePrice(segment, passenger, reqVO.getCurrency());
                totalBasePrice = totalBasePrice.add(basePrice);

                // 添加基础票价明细
                feeDetails.add(buildFeeDetail("TICKET_PRICE", basePrice, reqVO.getCurrency(), 
                        String.format("机票基础价格 - %s", segment.getFlightNo())));
            }
        }

        // 计算税费
        BigDecimal airportTax = calculateAirportTax(reqVO, totalBasePrice);
        BigDecimal fuelSurcharge = calculateFuelSurcharge(reqVO, totalBasePrice);
        totalTaxes = airportTax.add(fuelSurcharge);

        feeDetails.add(buildFeeDetail("AIRPORT_TAX", airportTax, reqVO.getCurrency(), "机场建设费"));
        feeDetails.add(buildFeeDetail("FUEL_SURCHARGE", fuelSurcharge, reqVO.getCurrency(), "燃油附加费"));

        // 计算服务费
        BigDecimal serviceFee = calculateServiceFee(reqVO, totalBasePrice);
        totalOtherFees = totalOtherFees.add(serviceFee);

        feeDetails.add(buildFeeDetail("SERVICE_FEE", serviceFee, reqVO.getCurrency(), "服务费"));

        // 构建响应
        FeeCalculateRespVO response = new FeeCalculateRespVO();
        response.setFees(feeDetails);
        response.setCurrency(reqVO.getCurrency());
        response.setTotalAmount(totalBasePrice.add(totalTaxes).add(totalOtherFees));

        // 费用分解
        FeeCalculateRespVO.FeeBreakdown breakdown = new FeeCalculateRespVO.FeeBreakdown();
        breakdown.setBasePrice(totalBasePrice);
        breakdown.setTaxes(totalTaxes);
        breakdown.setFees(totalOtherFees);
        response.setBreakdown(breakdown);

        return response;
    }

    private BigDecimal calculateBasePrice(FeeCalculateReqVO.FlightSegmentSimpleInfo segment, 
                                        FeeCalculateReqVO.PassengerSimpleInfo passenger, String currency) {
        // 获取航线基础价格
        String routeKey = segment.getDepartureAirportCode() + "-" + segment.getArrivalAirportCode();
        RoutePrice routePrice = ROUTE_PRICES.get(routeKey);
        
        if (routePrice == null) {
            // 如果没有找到航线价格，使用默认价格
            routePrice = new RoutePrice(new BigDecimal("2000"), new BigDecimal("30"), new BigDecimal("100"));
        }

        BigDecimal basePrice = routePrice.getBasePrice();

        // 根据舱位等级调整价格
        BigDecimal cabinMultiplier = CABIN_CLASS_MULTIPLIER.getOrDefault(segment.getCabinClass(), new BigDecimal("1.0"));
        basePrice = basePrice.multiply(cabinMultiplier);

        // 根据乘客类型调整价格
        BigDecimal passengerMultiplier = PASSENGER_TYPE_MULTIPLIER.getOrDefault(passenger.getPassengerType(), new BigDecimal("1.0"));
        basePrice = basePrice.multiply(passengerMultiplier);

        // 币种转换（这里简化处理，实际项目中应该调用汇率API）
        if (!"CNY".equals(currency)) {
            BigDecimal exchangeRate = getExchangeRate("CNY", currency);
            basePrice = basePrice.multiply(exchangeRate);
        }

        return basePrice.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateAirportTax(FeeCalculateReqVO reqVO, BigDecimal totalBasePrice) {
        // 机场建设费通常是固定费用
        BigDecimal taxPerSegment = new BigDecimal("50");
        int totalSegments = reqVO.getPassengers().size() * reqVO.getFlightSegments().size();
        
        BigDecimal totalTax = taxPerSegment.multiply(new BigDecimal(totalSegments));
        
        // 币种转换
        if (!"CNY".equals(reqVO.getCurrency())) {
            BigDecimal exchangeRate = getExchangeRate("CNY", reqVO.getCurrency());
            totalTax = totalTax.multiply(exchangeRate);
        }
        
        return totalTax.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateFuelSurcharge(FeeCalculateReqVO reqVO, BigDecimal totalBasePrice) {
        // 燃油附加费通常按基础票价的一定比例计算
        BigDecimal fuelRate = new BigDecimal("0.08"); // 8%
        BigDecimal fuelSurcharge = totalBasePrice.multiply(fuelRate);
        
        return fuelSurcharge.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateServiceFee(FeeCalculateReqVO reqVO, BigDecimal totalBasePrice) {
        // 服务费通常是固定费用或按比例计算
        BigDecimal serviceFeeRate = new BigDecimal("0.02"); // 2%
        BigDecimal minServiceFee = new BigDecimal("20");
        
        BigDecimal serviceFee = totalBasePrice.multiply(serviceFeeRate);
        
        // 币种转换最小服务费
        if (!"CNY".equals(reqVO.getCurrency())) {
            BigDecimal exchangeRate = getExchangeRate("CNY", reqVO.getCurrency());
            minServiceFee = minServiceFee.multiply(exchangeRate);
        }
        
        // 取较大值
        if (serviceFee.compareTo(minServiceFee) < 0) {
            serviceFee = minServiceFee;
        }
        
        return serviceFee.setScale(2, RoundingMode.HALF_UP);
    }

    private FeeCalculateRespVO.FeeDetail buildFeeDetail(String feeType, BigDecimal amount, String currency, String description) {
        FeeCalculateRespVO.FeeDetail detail = new FeeCalculateRespVO.FeeDetail();
        detail.setFeeType(feeType);
        detail.setAmount(amount);
        detail.setCurrency(currency);
        detail.setDescription(description);
        return detail;
    }

    private BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {
        // 简化的汇率转换，实际项目中应该调用汇率API
        if ("CNY".equals(fromCurrency)) {
            switch (toCurrency) {
                case "USD": return new BigDecimal("0.14");
                case "EUR": return new BigDecimal("0.13");
                case "JPY": return new BigDecimal("20.0");
                case "KRW": return new BigDecimal("180.0");
                case "SGD": return new BigDecimal("0.19");
                default: return new BigDecimal("1.0");
            }
        }
        return new BigDecimal("1.0");
    }

    // 航线价格类
    private static class RoutePrice {
        private BigDecimal basePrice;
        private BigDecimal airportTax;
        private BigDecimal fuelSurcharge;

        public RoutePrice(BigDecimal basePrice, BigDecimal airportTax, BigDecimal fuelSurcharge) {
            this.basePrice = basePrice;
            this.airportTax = airportTax;
            this.fuelSurcharge = fuelSurcharge;
        }

        public BigDecimal getBasePrice() { return basePrice; }
        public BigDecimal getAirportTax() { return airportTax; }
        public BigDecimal getFuelSurcharge() { return fuelSurcharge; }
    }
}