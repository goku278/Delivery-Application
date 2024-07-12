package com.example.deliveryapp.ui.fragment;

import static com.example.deliveryapp.config.ApiConfig.BASE_URL;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.deliveryapp.R;
import com.example.deliveryapp.adapter.DetailsAdapter;
import com.example.deliveryapp.adapter.MyViewPagerAdapter;
import com.example.deliveryapp.listener.OnClickListener;
import com.example.deliveryapp.model.DeliveryDetails;
import com.example.deliveryapp.model.Order;
import com.example.deliveryapp.model.ResponseList;
import com.example.deliveryapp.network.ApiService;
import com.example.deliveryapp.network.RetrofitClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * This is a HomeFragment
 */

public class HomeFragment extends Fragment implements OnClickListener {

    private SpringDotsIndicator dot2;
    private MyViewPagerAdapter adapter;
    private DetailsAdapter detailsAdapter;
    private ViewPager viewPager;
    RecyclerView rvOrderDetails;
    List<Integer> viewPagerBackground;
    ArrayList<DeliveryDetails> deliveryDetailsList = new ArrayList<>();
    public BottomNavigationView bottomNavigationView;

    private double latitude, longitude;

    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final float DESIRED_DISTANCE_THRESHOLD = 50.0f;

    private Context context;

    public DeliveryDetails deliveryDetails;

    public HomeFragment() {

    }

    public HomeFragment(BottomNavigationView bottomNavigationView, Context context) {
        this.bottomNavigationView = bottomNavigationView;
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = view.findViewById(R.id.view_pager);
        dot2 = view.findViewById(R.id.dot2);
        rvOrderDetails = view.findViewById(R.id.rvDeliveryDetails);

        // At first we check here, if the data are already available in the local database,
        // if available then we load the data directly without calling the api

        deliveryDetailsList = Paper.book().read("deliveryDetails", deliveryDetailsList);
        if (deliveryDetailsList != null && !deliveryDetailsList.isEmpty()) {
            // Check if the first element is not null and has the required attributes
            if (deliveryDetailsList.get(0) != null && deliveryDetailsList.get(0).getOrderName() != null
                    && !deliveryDetailsList.get(0).getCustomerName().isEmpty()) {
                setUpViewPager();
                setOrderDetails();
            }
        } else {
            // call api
            fetchDeliveryDetails();
        }
        return view;
    }

    /**
     * This below method is fetching the data from the API
     */

    public void fetchDeliveryDetails() {
        Retrofit retrofit = RetrofitClient.getClient(BASE_URL);
        ApiService apiService = retrofit.create(ApiService.class);
        Call<ResponseList> call = apiService.getOrderList();
        call.enqueue(new Callback<ResponseList>() {
            @Override
            public void onResponse(@NonNull Call<ResponseList> call, @NonNull Response<ResponseList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResponseList responseList = response.body();
                    // Handle the parsed response here
                    for (Order order : responseList.orderlist) {
                        // Process each order
                        Log.d("Order", "Order ID: " + order.getOrder_id());
                        DeliveryDetails deliveryDetails = new DeliveryDetails(
                                order.order_id,
                                order.order_no,
                                order.customer_name,
                                order.latitude,
                                order.longitude,
                                order.address,
                                order.delivery_cost
                        );
                        deliveryDetailsList.add(deliveryDetails);
                    }

                    // Here we are caching teh data, using this PaperDb.

                    Paper.book().write("deliveryDetails", deliveryDetailsList);
                    setUpViewPager();
                    setOrderDetails();
                } else {
                    // Handle the case where the response is not successful
                    Log.e("Error", "Request failed with code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseList> call, @NonNull Throwable t) {
                // Handle the error case
                Log.e("Error", "Network error: " + t.getMessage());
            }
        });
    }

    /**
     * We are setting up the data fetched from the server ot the recycler view
     */

    public void setOrderDetails() {
        // Add more sample data as needed
        if (isAdded()) {
            detailsAdapter = new DetailsAdapter(deliveryDetailsList, this, false, null, null, requireContext());
            rvOrderDetails.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 columns
            rvOrderDetails.setAdapter(detailsAdapter);
        }
    }

    /**
     * This below method is setting up view pager, ie\. the image slider what we seen on the HomeFragment class.
     */

    public void setUpViewPager() {


        viewPagerBackground = new ArrayList<>();

        viewPagerBackground.add(R.drawable.cardview_blue_background);
        viewPagerBackground.add(R.drawable.cardview_green_background);
        viewPagerBackground.add(R.drawable.cardview_navyblue_background);
        viewPagerBackground.add(R.drawable.cardview_purple_background);
        viewPagerBackground.add(R.drawable.cardview_background);

        adapter = new MyViewPagerAdapter(getContext(), deliveryDetailsList, viewPagerBackground);
        viewPager.setAdapter(adapter);
        dot2.setViewPager(viewPager);
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    /**
     * @param deliveryDetails As soon as user clicks on teh delivery sections, then we are fetching the current device's location
     *                        And based on the fetched location, we will compare if the current device's location and the delivery address location
     *                        are within the 50 metre range, if within the 50 metres range then we navigate to the Delivery Details screen,
     *                        else we show a Toast message to the user.
     */


    @Override
    public void onDeliveryItemClicked(DeliveryDetails deliveryDetails) {
        latitude = Double.parseDouble(deliveryDetails.getLatitude());
        longitude = Double.parseDouble(deliveryDetails.getLongitude());
        this.deliveryDetails = deliveryDetails;
        getLocation(latitude, longitude, requireContext(), requireActivity(), deliveryDetails);
    }

    @Override
    public void onFilterIconClicked(ArrayList<DeliveryDetails> filteredList) {

    }

    /**
     * @param latitude
     * @param longitude
     * @param context
     * @param activity
     * @param deliveryDetails In this below method we are fetching the current location.
     */

    private void getLocation(double latitude, double longitude, Context context, Activity activity, DeliveryDetails deliveryDetails) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        // Request location permissions
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permissions are already granted, proceed with getting the location
            getCurrentLocation(latitude, longitude, deliveryDetails);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with getting the location
                getCurrentLocation(latitude, longitude, this.deliveryDetails);
            } else {
                // Permission denied, handle the case
            }
        }
    }

    private void getCurrentLocation(double latitude, double longitude, DeliveryDetails deliveryDetails) {
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
//        if (isLocation)
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            double currentLatitude = location.getLatitude();
                            double currentLongitude = location.getLongitude();
                            // Compare this with the delivery location
                            compareLocations(currentLatitude, currentLongitude, latitude, longitude, deliveryDetails);
                        }
                    }
                });
    }

    /**
     * @param currentLatitude
     * @param currentLongitude
     * @param deliveryLatitude
     * @param deliveryLongitude
     * @param deliveryDetails   We are comparing teh current location with the provided address location, and teh comparison factor is 50 metre
     */

    private void compareLocations(double currentLatitude, double currentLongitude, double deliveryLatitude, double deliveryLongitude, DeliveryDetails deliveryDetails) {
        // Your logic to compare locations and perform necessary actions
        float[] results = new float[1];
        Location.distanceBetween(currentLatitude, currentLongitude, deliveryLatitude, deliveryLongitude, results);
        float distanceInMeters = results[0];

        if (distanceInMeters < DESIRED_DISTANCE_THRESHOLD) {
            // The current location is within the desired distance of the delivery location
            // Perform your actions here
            // Now visit the Delivery Details screen
            bottomNavigationView.setSelectedItemId(R.id.delivery);
            loadFragment(new DeliveryDetailsFragment(deliveryDetails, bottomNavigationView));
        } else {
            // The current location is not within the desired distance of the delivery location
            // Perform your actions here
            Toast.makeText(requireContext(), "The delivery location is too far", Toast.LENGTH_SHORT).show();
        }
    }

}