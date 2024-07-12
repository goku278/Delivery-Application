package com.example.deliveryapp.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.deliveryapp.R;
import com.example.deliveryapp.adapter.DetailsAdapter;
import com.example.deliveryapp.listener.OnClickListener;
import com.example.deliveryapp.model.DeliveryDetails;
import com.example.deliveryapp.model.OrderDetails;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

/**
 *  We are creating a class with recycler view adapter
 */

public class OrdersListFragment extends Fragment implements OnClickListener {
    ArrayList<DeliveryDetails> deliveryDetails;
    ArrayList<OrderDetails> orderDetails;
    private TextView tvDeliveriesCompleted;

    private DetailsAdapter detailsAdapter;
    private ViewPager viewPager;
    private RecyclerView rvOrderDetails;
    private BottomNavigationView bottomNavigationView;
    private List<String> idList2;
    private LinearLayout llFilter;

    public OrdersListFragment() {

    }

    public OrdersListFragment(BottomNavigationView bottomNavigationView, LinearLayout llFilter) {
        this.bottomNavigationView = bottomNavigationView;
        this.llFilter = llFilter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.orders_list_fragment, container, false);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        deliveryDetails = Paper.book().read("deliveryDetails", deliveryDetails);
        orderDetails = new ArrayList<OrderDetails>();
        List<String> idList = new ArrayList<>();
        idList2 = new ArrayList<>();
        double partialAmount = 0d, totalAmount = 0d;
        if (deliveryDetails != null) {
            for (DeliveryDetails d : deliveryDetails) {
                idList.add(d.getOrderId());
                OrderDetails orderDetails1 = Paper.book().read(d.getOrderId(), new OrderDetails());
                if (orderDetails1 != null && orderDetails1.getOrderId() != null) {
                    orderDetails.add(orderDetails1);
                    idList2.add(orderDetails1.getOrderId());
                    partialAmount += Double.parseDouble(orderDetails1.getPaidOrderBill());
                }
                totalAmount += Double.parseDouble(d.getDeliveryCost());
            }
        }
        Log.d("OrderListFragment", "OrderDetails => " + orderDetails.size());

        // Displaying the highlights

        tvDeliveriesCompleted = view.findViewById(R.id.tvDeliveriesCompleted);

        tvDeliveriesCompleted.setText("Deliveries completed-> " + (orderDetails.size()) + "/" +  (deliveryDetails.size()) + "\n\n" +
                "Cash collected-> " + (partialAmount) + "/" + (totalAmount));

        rvOrderDetails = view.findViewById(R.id.rvOrderList);

        setAdapter(view);
    }

    private void setAdapter(View view) {
        detailsAdapter = new DetailsAdapter(deliveryDetails, this, true, idList2, llFilter, requireContext());
        rvOrderDetails.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 columns
        rvOrderDetails.setAdapter(detailsAdapter);
    }

    @Override
    public void onDeliveryItemClicked(DeliveryDetails deliveryDetails) {

    }

    @Override
    public void onFilterIconClicked(ArrayList<DeliveryDetails> filteredList) {
        // When user clicks the Filter icon in the Orders List Screen.

        detailsAdapter = new DetailsAdapter(filteredList, this, true, idList2, llFilter, requireContext());
        rvOrderDetails.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 columns
        rvOrderDetails.setAdapter(detailsAdapter);
        detailsAdapter.notifyDataSetChanged();
    }
}
