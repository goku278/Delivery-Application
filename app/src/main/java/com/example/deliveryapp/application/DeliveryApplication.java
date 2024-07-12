package com.example.deliveryapp.application;

import android.app.Application;
import io.paperdb.Paper;

/**
 *  This is the entry point of our delivery app application,
 *  Here below we initiated PaperDb
 */

public class DeliveryApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Paper.init(this);
    }
}
