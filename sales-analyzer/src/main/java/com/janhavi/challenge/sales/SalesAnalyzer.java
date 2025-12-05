package com.janhavi.challenge.sales;

import java.time.YearMonth;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * SalesAnalyzer performs analytical computations on a collection
 * of {@link SalesRecord} objects using Java Streams.
 *
 */
public class SalesAnalyzer {

    private final List<SalesRecord> records;

    /**
     * Constructs a SalesAnalyzer with the given list of sales records.
     *
     * @param records list of sales records to be analyzed
     */
    public SalesAnalyzer(List<SalesRecord> records) {
        this.records = records;
    }

    /**
     * Calculates total revenue across all sales records.
     *
     * @return sum of total revenue for all records
     */
    public double totalRevenue() {
        return records.stream()
                .mapToDouble(SalesRecord::getTotalRevenue)
                .sum();
    }

    /**
     * Calculates total profit across all sales records.
     *
     * @return sum of total profit for all records
     */
    public double totalProfit() {
        return records.stream()
                .mapToDouble(SalesRecord::getTotalProfit)
                .sum();
    }

    /**
     * Groups sales by region and calculates total revenue per region.
     *
     * @return map of region -> total revenue
     */
    public Map<String, Double> revenueByRegion() {
        return records.stream()
                .collect(Collectors.groupingBy(
                        SalesRecord::getRegion,
                        Collectors.summingDouble(SalesRecord::getTotalRevenue)
                ));
    }


    /**
     * Groups sales by region and calculates total profit per region.
     *
     * @return map of region -> total profit
     */
    public Map<String, Double> profitByRegion() {
        return records.stream()
                .collect(Collectors.groupingBy(
                        SalesRecord::getRegion,
                        Collectors.summingDouble(SalesRecord::getTotalProfit)
                ));
    }

    /**
     * Groups sales by item type and calculates total revenue per item type.
     *
     * @return map of item type -> total revenue
     */
    public Map<String, Double> revenueByItemType() {
        return records.stream()
                .collect(Collectors.groupingBy(
                        SalesRecord::getItemType,
                        Collectors.summingDouble(SalesRecord::getTotalRevenue)
                ));
    }

    /**
     * Groups sales by country and calculates total units sold per country.
     *
     * @return map of country -> total units sold
     */
    public Map<String, Integer> unitsSoldByCountry() {
        return records.stream()
                .collect(Collectors.groupingBy(
                        SalesRecord::getCountry,
                        Collectors.summingInt(SalesRecord::getUnitsSold)
                ));
    }

    /**
     * Aggregates revenue by month using {@link YearMonth}
     * derived from the order date.
     *
     * @return map of YearMonth -> total monthly revenue
     */
    public Map<YearMonth, Double> monthlyRevenue() {
        return records.stream()
                .collect(Collectors.groupingBy(
                        r -> YearMonth.from(r.getOrderDate()),
                        Collectors.summingDouble(SalesRecord::getTotalRevenue)
                ));
    }

    /**
     * Returns the top N most profitable orders sorted in descending order of profit.
     *
     * @param n number of top records to return
     * @return list of top N profitable sales records
     */
    public List<SalesRecord> topNProfitableOrders(int n) {
        return records.stream()
                .sorted((a, b) -> Double.compare(b.getTotalProfit(), a.getTotalProfit()))
                .limit(n)
                .toList();
    }

    /**
     * Computes statistical summary for total revenue values.
     * Provides count, sum, min, max, and average.
     *
     * @return {@link DoubleSummaryStatistics} for revenue
     */
    public DoubleSummaryStatistics revenueStatistics() {
        return records.stream()
                .mapToDouble(SalesRecord::getTotalRevenue)
                .summaryStatistics();
    }
}
