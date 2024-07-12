package com.example.deliveryapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.deliveryapp.R;
import com.example.deliveryapp.model.DeliveryDetails;

import java.util.List;

/**
 *  This below class is a View Pager Adapter class which extend the pagerAdapter parent class
 */

public class MyViewPagerAdapter extends PagerAdapter {

    private Context context;
    private List<DeliveryDetails> deliveryDetailsList;
    private List<Integer> viewPagerBackground;

    public MyViewPagerAdapter(
            Context context,
            List<DeliveryDetails> deliveryDetailsList,
            List<Integer> viewPagerBackground
    ) {
        this.context = context;
        this.deliveryDetailsList = deliveryDetailsList;
        this.viewPagerBackground = viewPagerBackground;
    }

    @Override
    public int getCount() {
        return deliveryDetailsList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
        );
        View view = layoutInflater.inflate(R.layout.view_pager_layout, container, false);

        TextView tvOrderNumber = view.findViewById(R.id.tvOrderName);
        TextView tvCustomerName = view.findViewById(R.id.tvCustomerName);
        TextView tvAddress = view.findViewById(R.id.tvAddress);
        LinearLayout llParent = view.findViewById(R.id.llParent);

        int validPosition = position % viewPagerBackground.size();
        llParent.setBackgroundResource(viewPagerBackground.get(validPosition));
        tvOrderNumber.setText(deliveryDetailsList.get(position).getOrderName());
        tvCustomerName.setText(deliveryDetailsList.get(position).getCustomerName());
        tvAddress.setText(deliveryDetailsList.get(position).getAddress());

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
