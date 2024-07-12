package com.example.deliveryapp.model;

import androidx.annotation.NonNull;

import java.util.Arrays;

public class OrderDetails {
    private String orderId;
    private String orderNumber;
    private String orderBill;
    private String paidOrderBill;
    private byte[] orderPhoto;
    private String orderDamages;
    private String orderAddress;
    private String customerName;
    private String orderQuality;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderBill() {
        return orderBill;
    }

    public void setOrderBill(String orderBill) {
        this.orderBill = orderBill;
    }

    public byte[] getOrderPhoto() {
        return orderPhoto;
    }

    public void setOrderPhoto(byte[] orderPhoto) {
        this.orderPhoto = orderPhoto;
    }

    public String getOrderDamages() {
        return orderDamages;
    }

    public void setOrderDamages(String orderDamages) {
        this.orderDamages = orderDamages;
    }

    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPaidOrderBill() {
        return paidOrderBill;
    }

    public void setPaidOrderBill(String paidOrderBill) {
        this.paidOrderBill = paidOrderBill;
    }

    public String getOrderQuality() {
        return orderQuality;
    }

    public void setOrderQuality(String orderQuality) {
        this.orderQuality = orderQuality;
    }

    @NonNull
    @Override
    public String toString() {
        return "OrderDetails{" +
                "orderId='" + orderId + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", orderBill='" + orderBill + '\'' +
                ", paidOrderBill='" + paidOrderBill + '\'' +
                ", orderPhoto=" + Arrays.toString(orderPhoto) +
                ", orderDamages='" + orderDamages + '\'' +
                ", orderAddress='" + orderAddress + '\'' +
                ", customerName='" + customerName + '\'' +
                ", orderQuality='" + orderQuality + '\'' +
                '}';
    }
}