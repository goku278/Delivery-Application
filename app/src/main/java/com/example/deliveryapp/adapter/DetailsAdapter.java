package com.example.deliveryapp.adapter;

// MyRecyclerViewAdapter.java

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deliveryapp.R;
import com.example.deliveryapp.listener.OnClickListener;
import com.example.deliveryapp.model.DeliveryDetails;

import java.util.ArrayList;
import java.util.List;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.ViewHolder> {

    private final ArrayList<DeliveryDetails> deliveryDetailsList;
    private OnClickListener onClickListener;
    private Boolean isFromOrderListFragment;
    private List<String> idList2;
    private LinearLayout llFilter;
    private Context context;

    /**
     *
     * @param deliveryDetailsList
     * @param onClickListener
     * @param isFromOrderListFragment
     * @param idList2
     * @param llFilter
     * @param context
     *
     * This below method is a parameterized constructor
     */

    public DetailsAdapter(ArrayList<DeliveryDetails> deliveryDetailsList, OnClickListener onClickListener, Boolean isFromOrderListFragment, List<String> idList2, LinearLayout llFilter, Context context) {
        this.deliveryDetailsList = deliveryDetailsList;
        this.onClickListener = onClickListener;
        this.isFromOrderListFragment = isFromOrderListFragment;
        this.idList2 = idList2;
        this.llFilter = llFilter;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_details_layout2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvAddress2.setText(deliveryDetailsList.get(position).getAddress());
        holder.llParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onDeliveryItemClicked(deliveryDetailsList.get(holder.getAdapterPosition()));
            }
        });

        // We are using this same Details adapter from Details Fragment and as well as OrdersListFragment screen

        if (isFromOrderListFragment) {
            if (position >= 0 && position < deliveryDetailsList.size()) {
                DeliveryDetails deliveryDetail = deliveryDetailsList.get(position);
                if (idList2.contains(deliveryDetail.getOrderId())) {
                    holder.llParent.setBackgroundResource(R.drawable.card_background22);
                    Log.d("DetailsAdapter", "deliveryDetails => " + deliveryDetail);
                }
            }

            // We are filtering the list as per the orders delivery cost exceeding rs. 400/-

            if (this.llFilter != null) {
                this.llFilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Filtered the Orders List whose order cost are more than 400", Toast.LENGTH_SHORT).show();
//                        // Filter by delivery_cost > 400.0
                        ArrayList<DeliveryDetails> filteredList = filterByDeliveryCostGreaterThan(deliveryDetailsList, 400.0);
                        onClickListener.onFilterIconClicked(filteredList);
                    }
                });
            }
        }

    }

    // Filter method to filter the data list

    public static ArrayList<DeliveryDetails> filterByDeliveryCostGreaterThan(ArrayList<DeliveryDetails> list, double minDeliveryCost) {
        ArrayList<DeliveryDetails> filteredList = new ArrayList<>();
        for (DeliveryDetails details : list) {
            if (Double.parseDouble(details.getDeliveryCost()) > minDeliveryCost) {
                filteredList.add(details);
            }
        }
        return filteredList;
    }

    @Override
    public int getItemCount() {
        return deliveryDetailsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAddress2;
        LinearLayout llParent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAddress2 = itemView.findViewById(R.id.tvAddress2);
            llParent = itemView.findViewById(R.id.llParent);
        }
    }
}