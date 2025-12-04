package com.janhavi.challenge.sales;

import java.time.YearMonth;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SalesAnalyzer {

    private final List<SalesRecord> records;

    public SalesAnalyzer(List<SalesRecord> records) {
        this.records = records;
    }

    // Total Revenue
    public double totalRevenue() {
        return records.stream()
                .mapToDouble(SalesRecord::getTotalRevenue)
                .sum();
    }

    // Total Profit
    public double totalProfit() {
        return records.stream()
                .mapToDouble(SalesRecord::getTotalProfit)
                .sum();
    }

    // Revenue By Region
    public Map<String, Double> revenueByRegion() {
        return records.stream()
                .collect(Collectors.groupingBy(
                        SalesRecord::getRegion,
                        Collectors.summingDouble(SalesRecord::getTotalRevenue)
                ));
    }

    // Profit By Region
    public Map<String, Double> profitByRegion() {
        return records.stream()
                .collect(Collectors.groupingBy(
                        SalesRecord::getRegion,
                        Collectors.summingDouble(SalesRecord::getTotalProfit)
                ));
    }

    // Revenue By Item Type
    public Map<String, Double> revenueByItemType() {
        return records.stream()
                .collect(Collectors.groupingBy(
                        SalesRecord::getItemType,
                        Collectors.summingDouble(SalesRecord::getTotalRevenue)
                ));
    }

    // Units Sold By Country
    public Map<String, Integer> unitsSoldByCountry() {
        return records.stream()
                .collect(Collectors.groupingBy(
                        SalesRecord::getCountry,
                        Collectors.summingInt(SalesRecord::getUnitsSold)
                ));
    }

    // Monthly Revenue
    public Map<YearMonth, Double> monthlyRevenue() {
        return records.stream()
                .collect(Collectors.groupingBy(
                        r -> YearMonth.from(r.getOrderDate()),
                        Collectors.summingDouble(SalesRecord::getTotalRevenue)
                ));
    }

    // Top N Profitable Orders
    public List<SalesRecord> topNProfitableOrders(int n) {
        return records.stream()
                .sorted((a, b) -> Double.compare(b.getTotalProfit(), a.getTotalProfit()))
                .limit(n)
                .toList();
    }

    // Revenue Statistics
    public DoubleSummaryStatistics revenueStatistics() {
        return records.stream()
                .mapToDouble(SalesRecord::getTotalRevenue)
                .summaryStatistics();
    }
}
