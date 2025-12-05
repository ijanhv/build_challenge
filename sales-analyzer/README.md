# Sales Data Analysis with Java Streams

A comprehensive Java application demonstrating functional programming and Stream API operations for analyzing sales data from CSV files. This project showcases data aggregation, grouping, filtering, and statistical analysis using lambda expressions and modern Java paradigms.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Architecture](#architecture)
- [Setup Instructions](#setup-instructions)
- [Usage](#usage)
- [Analysis Operations](#analysis-operations)
- [Sample Output](#sample-output)
- [Testing](#testing)
- [Technical Details](#technical-details)

## Overview

This application processes sales transaction data from CSV files and performs various analytical operations using Java Streams API. It demonstrates proficiency in:
- Functional programming paradigms
- Stream operations (map, filter, reduce, collect)
- Data aggregation and grouping with Collectors
- Lambda expressions and method references
- Statistical computations

## Features

- **CSV Data Import**: Robust parsing of sales data from CSV files
- **Multiple Analysis Methods**: 8+ different analytical queries on sales data
- **Functional Programming**: Pure functional approach using Streams API
- **Statistical Summaries**: Comprehensive revenue statistics (min, max, avg, sum)
- **Temporal Analysis**: Monthly revenue breakdown using YearMonth
- **Ranking Operations**: Top N profitable orders identification
- **Geographic Grouping**: Regional and country-based aggregations
- **Type Safety**: Immutable data models with strong typing

## Architecture

### Project Structure

```
com.janhavi.challenge.sales/
├── SalesRecord.java           # Immutable data model
├── SalesDataLoader.java       # CSV parsing and data loading
├── SalesAnalyzer.java         # Stream-based analysis operations
├── SalesAnalysisApp.java      # Main application entry point
└── test/
    └── SalesAnalyzerTest.java # Comprehensive unit tests
```

### Data Flow

```
CSV File → SalesDataLoader → List<SalesRecord> → SalesAnalyzer → Analysis Results
                ↓                      ↓                ↓
           Stream Pipeline      Functional Ops    Aggregated Data
```

## Setup Instructions

### Prerequisites

- **Java**: JDK 17 or higher (uses modern Java features)
- **Build Tool**: Maven or Gradle (optional)
- **IDE**: IntelliJ IDEA, Eclipse, or VS Code with Java extensions
- **Testing**: JUnit 5 (Jupiter)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/sales-analyzer.git
   cd sales-data-analysis
   ```

2. **Verify Java version**
   ```bash
   java -version
   # Should be 17 or higher
   ```

3. **Compile the project**
   ```bash
   # Using javac
   javac -d bin src/com/janhavi/challenge/sales/*.java
   
   # Or with Maven
   mvn clean compile
   ```

4. **Prepare sample data**
    - Place `sales-sample.csv` in the project root or a known location
    - Ensure CSV follows the expected format (see Data Format section)

##  Usage

### Running the Application

```bash
# Using compiled classes
java -cp bin com.janhavi.challenge.sales.SalesAnalysisApp sales-sample.csv

# Using Maven
mvn exec:java -Dexec.mainClass="com.janhavi.challenge.sales.SalesAnalysisApp" \
              -Dexec.args="sales-sample.csv"

# Using an executable JAR
java -jar sales-analysis.jar sales-sample.csv
```

### Command Line Arguments

| Argument | Description | Required |
|----------|-------------|----------|
| `<csv-path>` | Path to CSV file containing sales data | Yes |

### CSV Data Format

The input CSV must have the following columns:

| Region | Country | Item Type | Sales Channel | Order Priority | Order Date | Order ID | Ship Date | Units Sold | Unit Price | Unit Cost | Total Revenue | Total Cost | Total Profit |
|--------|---------|-----------|---------------|----------------|------------|----------|-----------|------------|------------|-----------|---------------|------------|--------------|
| Middle East and North Africa | Libya | Cosmetics | Offline | M | 10/18/2014 | 686800706 | 10/31/2014 | 8446 | 437.20 | 263.33 | 3692591.20 | 2224085.18 | 1468506.02 |


## Analysis Operations

### 1. Total Revenue
Calculates sum of all revenue across all transactions.

```java
public double totalRevenue() {
    return records.stream()
        .mapToDouble(SalesRecord::getTotalRevenue)
        .sum();
}
```

### 2. Total Profit
Computes aggregate profit from all sales.

```java
public double totalProfit() {
    return records.stream()
        .mapToDouble(SalesRecord::getTotalProfit)
        .sum();
}
```

### 3. Revenue by Region
Groups sales by geographic region and sums revenue.

```java
public Map<String, Double> revenueByRegion() {
    return records.stream()
        .collect(Collectors.groupingBy(
            SalesRecord::getRegion,
            Collectors.summingDouble(SalesRecord::getTotalRevenue)
        ));
}
```

### 4. Profit by Region
Aggregates profit data by region.

```java
public Map<String, Double> profitByRegion() {
        return records.stream()
                .collect(Collectors.groupingBy(
                        SalesRecord::getRegion,
                        Collectors.summingDouble(SalesRecord::getTotalProfit)
                ));
    }

```
### 5. Revenue by Item Type
Analyzes which product categories generate most revenue.

```java
    public Map<String, Double> revenueByItemType() {
        return records.stream()
                .collect(Collectors.groupingBy(
                        SalesRecord::getItemType,
                        Collectors.summingDouble(SalesRecord::getTotalRevenue)
                ));
    }
```


### 6. Units Sold by Country
Tracks sales volume per country.

```java
public Map<String, Integer> unitsSoldByCountry() {
    return records.stream()
        .collect(Collectors.groupingBy(
            SalesRecord::getCountry,
            Collectors.summingInt(SalesRecord::getUnitsSold)
        ));
}
```

### 7. Monthly Revenue
Time-series analysis of revenue by month and year.

```java
public Map<YearMonth, Double> monthlyRevenue() {
    return records.stream()
        .collect(Collectors.groupingBy(
            r -> YearMonth.from(r.getOrderDate()),
            Collectors.summingDouble(SalesRecord::getTotalRevenue)
        ));
}
```

### 8. Top N Profitable Orders
Identifies most profitable individual transactions.

```java
public List<SalesRecord> topNProfitableOrders(int n) {
    return records.stream()
        .sorted((a, b) -> Double.compare(b.getTotalProfit(), a.getTotalProfit()))
        .limit(n)
        .toList();
}
```

### 9. Revenue Statistics
Provides comprehensive statistical summary.

```java
public DoubleSummaryStatistics revenueStatistics() {
    return records.stream()
        .mapToDouble(SalesRecord::getTotalRevenue)
        .summaryStatistics();
}
```

## Sample Output

```
=== SALES ANALYSIS REPORT ===

Total Revenue: 1.32732184033E9
Total Profit: 3.9120261156E8

Revenue By Region:
  Australia and Oceania => 1.056895726E8
  Asia => 1.6767480949E8
  Europe => 3.5316746293E8
  Central America and the Caribbean => 1.4399761051E8
  Middle East and North Africa => 1.7510653574E8
  North America => 2.496159894E7
  Sub-Saharan Africa => 3.5672425012E8

Revenue By Region:
  Australia and Oceania => 1.056895726E8
  Asia => 1.6767480949E8
  Europe => 3.5316746293E8
  Central America and the Caribbean => 1.4399761051E8
  Middle East and North Africa => 1.7510653574E8
  North America => 2.496159894E7
  Sub-Saharan Africa => 3.5672425012E8

Profit By Region:
  Australia and Oceania => 3.187842073E7
  Asia => 5.07993991E7
  Europe => 1.0677196845E8
  Central America and the Caribbean => 4.133677821E7
  Middle East and North Africa => 5.105699334E7
  North America => 7708059.27
  Sub-Saharan Africa => 1.0165099246E8

Revenue By Item Type:
  Cosmetics => 3692591.20
  Vegetables => 464953.08
  Baby Food => 387259.76
  Cereal => 3228255.80
  Fruits => 91853.85

Units Sold By Country:
  Libya => 9963
  Canada => 3018
  Japan => 3322
  Chad => 9845
  Armenia => 9528
  Eritrea => 2844

Monthly Revenue:
  2014-10 => 4079850.96
  2011-11 => 464953.08
  2010-04 => 683335.40
  2011-08 => 91853.85
  2014-11 => 1959909.60
  2015-03 => 585010.80

Top 5 Profitable Orders:
403961122 | Belgium | Cosmetics | Profit: 1726181.36
653148210 | India | Cosmetics | Profit: 1725485.88
418593108 | Sweden | Cosmetics | Profit: 1714010.46
573025262 | Maldives | Cosmetics | Profit: 1697666.68
409873998 | Turkey | Cosmetics | Profit: 1682887.73

Revenue Statistics:
Count: 1000
Min: 2043.25
Max: 6617209.54
Average: 1327321.84033
Sum: 1.32732184033E9
```

## Testing

### Test Coverage

The `SalesAnalyzerTest` class provides comprehensive unit tests:

```java
@Test
void testTotalRevenue()           // Validates revenue summation
void testTotalProfit()            // Validates profit summation
void testRevenueByRegion()        // Tests grouping operations
void testUnitsSoldByCountry()     // Tests integer aggregation
void testMonthlyRevenue()         // Tests temporal grouping
void testTopProfitableOrder()     // Tests sorting and limiting
void testRevenueStatistics()      // Tests statistical calculations
```

### Running Tests

```bash
# With Maven
mvn test

# With Gradle
gradle test

# With IDE
# Right-click on SalesAnalyzerTest.java → Run Tests
```

### Test Data

Tests use a curated sample dataset with known values:
- 3 sales records
- Multiple regions (Middle East, North America, Asia)
- Different item types (Cosmetics, Vegetables, Cereal)
- Date range: 2010-2014

### Assertions

All tests use JUnit 5 assertions with appropriate delta values for floating-point comparisons:

```java
assertEquals(expected, actual, 0.01);  // 2 decimal precision
```

## Technical Details

### Functional Programming Concepts

**Stream Pipeline Pattern**
```
Source → Intermediate Ops → Terminal Op → Result
         (filter, map)      (collect)
```

**Key Stream Operations Used**
- `mapToDouble()` - Transform to primitive double stream
- `collect()` - Mutable reduction operation
- `groupingBy()` - Multi-level grouping collector
- `summingDouble()` - Downstream collector for summation
- `sorted()` - Ordering elements
- `limit()` - Truncating streams

### Lambda Expressions

```java
// Method reference
.mapToDouble(SalesRecord::getTotalRevenue)

// Lambda expression
.sorted((a, b) -> Double.compare(b.getTotalProfit(), a.getTotalProfit()))

// Composed lambda
r -> YearMonth.from(r.getOrderDate())
```

### Collectors API

Advanced collector usage:
- `Collectors.groupingBy()` - Primary grouping
- `Collectors.summingDouble()` - Numeric aggregation
- `Collectors.summingInt()` - Integer summation
- Custom downstream collectors

## Learning Objectives

This project demonstrates:

✅ **Functional Programming**: Pure functions, immutability, higher-order functions  
✅ **Stream Operations**: Intermediate and terminal operations  
✅ **Data Aggregation**: Grouping, summing, averaging, statistics  
✅ **Lambda Expressions**: Method references, lambda syntax  
✅ **Collectors API**: Built-in and custom collectors  
✅ **CSV Processing**: File I/O with streams  
✅ **Unit Testing**: JUnit 5, test-driven development  
✅ **Clean Code**: Separation of concerns, single responsibility

## Future Enhancements

- Support for multiple CSV files (batch processing)
- Export analysis results to JSON/Excel
- Advanced filtering (date ranges, price thresholds)
- Parallel stream processing for large datasets
- Custom collectors for complex aggregations
- Interactive CLI with user-selectable analyses
- Visualization with charts/graphs
- Performance benchmarking for different dataset sizes

## Best Practices Demonstrated

1. **Separation of Concerns**: Loader, Analyzer, Model in separate classes
2. **Immutability**: Immutable data models prevent bugs
3. **Fail-Fast**: Input validation and clear error messages
4. **DRY Principle**: Reusable stream operations
5. **Type Safety**: Strong typing with generics
6. **Testability**: Unit tests with high coverage
7. **Documentation**: Javadoc comments on public methods

---

**Author**: Janhavi  
**Repository**: [https://github.com/ijanhv/build_challenge](https://github.com/ijanhv/build_challenge)  
**Last Updated**: December 2025  
