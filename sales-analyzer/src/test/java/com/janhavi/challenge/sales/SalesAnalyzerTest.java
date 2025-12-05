package com.janhavi.challenge.sales;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SalesAnalyzerTest {

    private List<SalesRecord> sampleData;
    private SalesAnalyzer analyzer;

    @BeforeEach
    void setup() {
        sampleData = List.of(
                new SalesRecord(
                        "Middle East and North Africa", "Libya", "Cosmetics", "Offline", "M",
                        LocalDate.of(2014, 10, 18), 686800706L,
                        LocalDate.of(2014, 10, 31), 8446, 437.20, 263.33,
                        3692591.20, 2224085.18, 1468506.02
                ),
                new SalesRecord(
                        "North America", "Canada", "Vegetables", "Online", "M",
                        LocalDate.of(2011, 11, 7), 185941302L,
                        LocalDate.of(2011, 12, 8), 3018, 154.06, 90.93,
                        464953.08, 274426.74, 190526.34
                ),
                new SalesRecord(
                        "Asia", "Japan", "Cereal", "Offline", "C",
                        LocalDate.of(2010, 4, 10), 161442649L,
                        LocalDate.of(2010, 5, 12), 3322, 205.70, 117.11,
                        683335.40, 389039.42, 294295.98
                )
        );

        analyzer = new SalesAnalyzer(sampleData);
    }

    // TEST 1: Total Revenue
    @Test
    void testTotalRevenue() {
        double expected =
                3692591.20 +
                        464953.08 +
                        683335.40;

        assertEquals(expected, analyzer.totalRevenue(), 0.01);
    }

    // TEST 2: Total Profit
    @Test
    void testTotalProfit() {
        double expected =
                1468506.02 +
                        190526.34 +
                        294295.98;

        assertEquals(expected, analyzer.totalProfit(), 0.01);
    }

    // TEST 3: Revenue By Region
    @Test
    void testRevenueByRegion() {
        Map<String, Double> map = analyzer.revenueByRegion();

        assertEquals(3692591.20, map.get("Middle East and North Africa"), 0.01);
        assertEquals(464953.08, map.get("North America"), 0.01);
        assertEquals(683335.40, map.get("Asia"), 0.01);
    }

    // TEST 4: Units Sold By Country
    @Test
    void testUnitsSoldByCountry() {
        Map<String, Integer> map = analyzer.unitsSoldByCountry();

        assertEquals(8446, map.get("Libya"));
        assertEquals(3018, map.get("Canada"));
        assertEquals(3322, map.get("Japan"));
    }

    // TEST 5: Monthly Revenue
    @Test
    void testMonthlyRevenue() {
        Map<YearMonth, Double> map = analyzer.monthlyRevenue();

        assertEquals(3692591.20, map.get(YearMonth.of(2014, 10)), 0.01);
        assertEquals(464953.08, map.get(YearMonth.of(2011, 11)), 0.01);
        assertEquals(683335.40, map.get(YearMonth.of(2010, 4)), 0.01);
    }

    // TEST 6: Top Profitable Order
    @Test
    void testTopProfitableOrder() {
        List<SalesRecord> top = analyzer.topNProfitableOrders(1);

        assertEquals(1, top.size());
        assertEquals(686800706L, top.get(0).getOrderId());
        assertEquals(1468506.02, top.get(0).getTotalProfit(), 0.01);
    }

    // TEST 7: Revenue Statistics
    @Test
    void testRevenueStatistics() {
        var stats = analyzer.revenueStatistics();

        assertEquals(3, stats.getCount());
        assertEquals(464953.08, stats.getMin(), 0.01);
        assertEquals(3692591.20, stats.getMax(), 0.01);
        assertEquals(1613626.5599999998, stats.getAverage(), 0.01);
        assertEquals(4840879.68, stats.getSum(), 0.01);

    }
}
