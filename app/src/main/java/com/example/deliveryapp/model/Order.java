package com.example.deliveryapp.model;

import androidx.annotation.NonNull;

// import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString, Root.class); */
public class Order {
    public String order_id;
    public String order_no;
    public String customer_name;
    public String latitude;
    public String longitude;
    public String address;
    public String delivery_cost;

    public Order(String order_id, String order_no, String customer_name, String latitude, String longitude, String address, String delivery_cost) {
        this.order_id = order_id;
        this.order_no = order_no;
        this.customer_name = customer_name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.delivery_cost = delivery_cost;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
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

    public String getDelivery_cost() {
        return delivery_cost;
    }

    public void setDelivery_cost(String delivery_cost) {
        this.delivery_cost = delivery_cost;
    }

    @NonNull
    @Override
    public String toString() {
        return "Response{" + "order_id='" + order_id + '\'' + ", order_no='" + order_no + '\'' + ", customer_name='" + customer_name + '\'' + ", latitude='" + latitude + '\'' + ", longitude='" + longitude + '\'' + ", address='" + address + '\'' + ", delivery_cost='" + delivery_cost + '\'' + '}';
    }
}

