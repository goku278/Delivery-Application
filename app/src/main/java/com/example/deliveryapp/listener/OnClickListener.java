package com.example.deliveryapp.listener;

import com.example.deliveryapp.model.DeliveryDetails;

import java.util.ArrayList;

/**
 *  Its an interface which provides click listeners
 */

public interface OnClickListener {
    public void onDeliveryItemClicked(
            DeliveryDetails deliveryDetails
    );

    public default void onFilterIconClicked(ArrayList<DeliveryDetails> filteredList) {}
}
