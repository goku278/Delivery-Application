package com.example.deliveryapp;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.deliveryapp.listener.OnClickListener;
import com.example.deliveryapp.model.DeliveryDetails;
import com.example.deliveryapp.ui.fragment.DeliveryDetailsFragment;
import com.example.deliveryapp.ui.fragment.HomeFragment;
import com.example.deliveryapp.ui.fragment.OrdersListFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    private DeliveryDetails deliveryDetails;

    private BottomNavigationView bottomNavigationView;
    private LinearLayout llBack, llFilter, llMiddle, llNotification;
    private FrameLayout fragment_container;

    FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        if (!isLocationEnabled()) {
            Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        loadFragment(new HomeFragment(bottomNavigationView, this));

        setBottomNavigationClicks();

        setClickListeners();
    }

    private void setClickListeners() {
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationView.setSelectedItemId(R.id.home);
                llFilter.setVisibility(View.GONE);
                llMiddle.setVisibility(View.VISIBLE);
                llBack.setVisibility(View.GONE);
                llNotification.setVisibility(View.VISIBLE);
                loadFragment(new HomeFragment(bottomNavigationView, getApplicationContext()));
            }
        });

        llNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationView.setSelectedItemId(R.id.orders);
                llBack.setVisibility(View.VISIBLE);
                llMiddle.setVisibility(View.GONE);
                llFilter.setVisibility(View.VISIBLE);
                llNotification.setVisibility(View.GONE);
                loadFragment(new OrdersListFragment(bottomNavigationView, llFilter));
            }
        });
    }

    /**
     *
     * @return
     *
     * Here in this below method, we are checking if the location is turned
     * ON/OFF on our hand sets
     * So if OFF then it will re-direct us to the settings screen, to turn ON location services.
     */

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    /**
     *    This below method is intended to navigate from one bottom navigation menu to other bottom navigation menus
     *    I used fragments, instead of creating new Activities, to make the application light weight and fast responsive
     */

    private void setBottomNavigationClicks() {
        LinearLayout toolbar = findViewById(R.id.toolbar);

        llBack = toolbar.findViewById(R.id.llBack);
        llMiddle = toolbar.findViewById(R.id.llMiddle);
        llFilter = toolbar.findViewById(R.id.llFilter);
        llNotification = toolbar.findViewById(R.id.llNotification);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    llFilter.setVisibility(View.GONE);
                    llMiddle.setVisibility(View.VISIBLE);
                    llBack.setVisibility(View.GONE);
                    llNotification.setVisibility(View.VISIBLE);
                    loadFragment(new HomeFragment(bottomNavigationView, getApplicationContext()));
                    return true;
                }
                if (item.getItemId() == R.id.delivery) {
                    ArrayList<DeliveryDetails> deliveryDetailsList = Paper.book().read("deliveryDetails");
                    llBack.setVisibility(View.VISIBLE);
                    llMiddle.setVisibility(View.GONE);
                    llFilter.setVisibility(View.GONE);
                    llNotification.setVisibility(View.GONE);
//                    assert deliveryDetailsList != null;
                    if (deliveryDetailsList != null) {
                        loadFragment(new DeliveryDetailsFragment(deliveryDetailsList.get(0), bottomNavigationView));
                    }
                    return true;
                }
                if (item.getItemId() == R.id.orders) {
                    llBack.setVisibility(View.VISIBLE);
                    llMiddle.setVisibility(View.GONE);
                    llFilter.setVisibility(View.VISIBLE);
                    llNotification.setVisibility(View.GONE);
                    loadFragment(new OrdersListFragment(bottomNavigationView, llFilter));
                    return true;
                }
                return false;
            }
        });
    }

    /**
     *
     * @param deliveryDetails
     *
     *  This below method is a listener which has been triggered
     *  from the Details Adapter class, so when any list items are
     *  clicked then we can identify the source and can work further on the provided data
     */

    @Override
    public void onDeliveryItemClicked(DeliveryDetails deliveryDetails) {
        // Now visit the Delivery Details screen
        this.deliveryDetails = deliveryDetails;
        bottomNavigationView.setSelectedItemId(R.id.delivery);
        loadFragment(new DeliveryDetailsFragment(deliveryDetails, bottomNavigationView));
    }
}