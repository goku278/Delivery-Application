package com.example.deliveryapp.model;

import androidx.annotation.NonNull;

/**
 *  This is a model class to fetch the response data coming via the server
 */

public class DeliveryDetails {
    private String orderId;
    private String orderName;
    private String customerName;
    private String latitude;
    private String longitude;
    private String address;
    private String deliveryCost;

    public DeliveryDetails(String orderName, String customerName, String address) {
        this.orderName = orderName;
        this.customerName = customerName;
        this.address = address;
    }

    public DeliveryDetails(String orderId, String orderName, String customerName, String latitude, String longitude, String address, String deliveryCost) {
        this.orderId = orderId;
        this.orderName = orderName;
        this.customerName = customerName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.deliveryCost = deliveryCost;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(String deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    @NonNull
    @Override
    public String toString() {
        return "DeliveryDetails{" +
                "orderId='" + orderId + '\'' +
                ", orderName='" + orderName + '\'' +
                ", customerName='" + customerName + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", address='" + address + '\'' +
                ", deliveryCost='" + deliveryCost + '\'' +
                '}';
    }
}
