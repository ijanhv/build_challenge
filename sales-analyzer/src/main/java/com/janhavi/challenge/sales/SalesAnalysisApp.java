package com.janhavi.challenge.sales;

import java.nio.file.Path;
import java.time.YearMonth;
import java.util.Map;

public class SalesAnalysisApp {

    public static void main(String[] args) throws Exception {

        if (args.length == 0) {
            System.err.println("Usage: java SalesAnalysisApp <path-to-sales-sample.csv>");
            System.exit(1);
        }

        Path csvPath = Path.of(args[0]);
        SalesDataLoader loader = new SalesDataLoader();

        var records = loader.load(csvPath);
        SalesAnalyzer analyzer = new SalesAnalyzer(records);

        System.out.println("\n=== SALES ANALYSIS REPORT ===");

        System.out.println("\nTotal Revenue: " + analyzer.totalRevenue());
        System.out.println("Total Profit: " + analyzer.totalProfit());

        System.out.println("\nRevenue By Region:");
        analyzer.revenueByRegion().forEach(
                (k, v) -> System.out.println("  " + k + " => " + v)
        );

        System.out.println("\nProfit By Region:");
        analyzer.profitByRegion().forEach(
                (k, v) -> System.out.println("  " + k + " => " + v)
        );

        System.out.println("\nRevenue By Item Type:");
        analyzer.revenueByItemType().forEach(
                (k, v) -> System.out.println("  " + k + " => " + v)
        );

        System.out.println("\nUnits Sold By Country:");
        analyzer.unitsSoldByCountry().forEach(
                (k, v) -> System.out.println("  " + k + " => " + v)
        );

        System.out.println("\nMonthly Revenue:");
        for (Map.Entry<YearMonth, Double> e : analyzer.monthlyRevenue().entrySet()) {
            System.out.println("  " + e.getKey() + " => " + e.getValue());
        }

        System.out.println("\nTop 5 Profitable Orders:");
        analyzer.topNProfitableOrders(5)
                .forEach(r -> System.out.println(
                        r.getOrderId() + " | " +
                                r.getCountry() + " | " +
                                r.getItemType() + " | Profit: " +
                                r.getTotalProfit()
                ));

        var stats = analyzer.revenueStatistics();
        System.out.println("\nRevenue Statistics:");
        System.out.println("Count: " + stats.getCount());
        System.out.println("Min: " + stats.getMin());
        System.out.println("Max: " + stats.getMax());
        System.out.println("Average: " + stats.getAverage());
        System.out.println("Sum: " + stats.getSum());
    }
}
