package com.janhavi.challenge.sales;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * SalesDataLoader is responsible for reading sales data from a CSV file
 * and converting each row into a {@link SalesRecord} object.
 *
 */
public class SalesDataLoader {

    /**
     * Date formatter for parsing order and ship dates.
     * Expected CSV format: M/d/yyyy (e.g., 10/18/2014)
     */
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("M/d/yyyy");

    /**
     * Reads a CSV file and converts all valid rows into a list of {@link SalesRecord}.
     *
     * @param csvPath path to the CSV file
     * @return list of parsed SalesRecord objects
     * @throws IOException if file reading fails
     */
    public List<SalesRecord> load(Path csvPath) throws IOException {
        // try-with-resources ensures the file stream is closed automatically
        try (Stream<String> lines = Files.lines(csvPath)) {
            return lines
                    .skip(1)
                    .filter(line -> !line.isBlank())
                    .map(this::parseLine)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Parses a single CSV row into a {@link SalesRecord} object.
     *
     * @param line raw CSV line
     * @return parsed SalesRecord object
     * @throws RuntimeException if parsing fails due to invalid data format
     */
    private SalesRecord parseLine(String line) {
        // Split CSV fields by comma
        String[] p = line.split(",");

        // Extract string fields
        String region = p[0].trim();
        String country = p[1].trim();
        String itemType = p[2].trim();
        String salesChannel = p[3].trim();
        String orderPriority = p[4].trim();

        // Parse numeric and date values
        long orderId = Long.parseLong(p[6].trim());
        LocalDate orderDate = LocalDate.parse(p[5].trim(), FORMATTER);
        LocalDate shipDate = LocalDate.parse(p[7].trim(), FORMATTER);

        int unitsSold = Integer.parseInt(p[8].trim());
        double unitPrice = Double.parseDouble(p[9].trim());
        double unitCost = Double.parseDouble(p[10].trim());
        double totalRevenue = Double.parseDouble(p[11].trim());
        double totalCost = Double.parseDouble(p[12].trim());
        double totalProfit = Double.parseDouble(p[13].trim());

        // Construct and return SalesRecord object
        return new SalesRecord(
                region, country, itemType, salesChannel, orderPriority,
                orderDate, orderId, shipDate,
                unitsSold, unitPrice, unitCost,
                totalRevenue, totalCost, totalProfit
        );
    }
}
