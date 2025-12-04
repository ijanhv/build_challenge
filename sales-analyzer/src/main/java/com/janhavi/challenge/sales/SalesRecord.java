package com.janhavi.challenge.sales;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Immutable model representing one row of sales data.
 */
public class SalesRecord {

    private final String region;
    private final String country;
    private final String itemType;
    private final String salesChannel;
    private final String orderPriority;
    private final LocalDate orderDate;
    private final long orderId;
    private final LocalDate shipDate;

    private final int unitsSold;
    private final double unitPrice;
    private final double unitCost;
    private final double totalRevenue;
    private final double totalCost;
    private final double totalProfit;

    public SalesRecord(String region, String country, String itemType,
                       String salesChannel, String orderPriority,
                       LocalDate orderDate, long orderId, LocalDate shipDate,
                       int unitsSold, double unitPrice, double unitCost,
                       double totalRevenue, double totalCost, double totalProfit) {

        this.region = region;
        this.country = country;
        this.itemType = itemType;
        this.salesChannel = salesChannel;
        this.orderPriority = orderPriority;
        this.orderDate = orderDate;
        this.orderId = orderId;
        this.shipDate = shipDate;
        this.unitsSold = unitsSold;
        this.unitPrice = unitPrice;
        this.unitCost = unitCost;
        this.totalRevenue = totalRevenue;
        this.totalCost = totalCost;
        this.totalProfit = totalProfit;
    }

    public String getRegion() { return region; }
    public String getCountry() { return country; }
    public String getItemType() { return itemType; }
    public String getSalesChannel() { return salesChannel; }
    public String getOrderPriority() { return orderPriority; }
    public LocalDate getOrderDate() { return orderDate; }
    public long getOrderId() { return orderId; }
    public LocalDate getShipDate() { return shipDate; }
    public int getUnitsSold() { return unitsSold; }
    public double getUnitPrice() { return unitPrice; }
    public double getUnitCost() { return unitCost; }
    public double getTotalRevenue() { return totalRevenue; }
    public double getTotalCost() { return totalCost; }
    public double getTotalProfit() { return totalProfit; }
}

