package com.example.deliveryapp.ui.fragment;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.view.View;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ApplicationProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.deliveryapp.MainActivity;
import com.example.deliveryapp.R;
import com.example.deliveryapp.adapter.DetailsAdapter;
import com.example.deliveryapp.model.DeliveryDetails;
import com.example.deliveryapp.model.Order;
import com.example.deliveryapp.model.ResponseList;
import com.example.deliveryapp.network.ApiService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeFragmentTest {

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testOnCreateView() {
        // Launch the fragment using FragmentScenario
        FragmentScenario<HomeFragment> scenario = FragmentScenario.launchInContainer(HomeFragment.class);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onFragment(fragment -> {
            // Verify the views are initialized
            ViewPager viewPager = fragment.getView().findViewById(R.id.view_pager);
            View dot2 = fragment.getView().findViewById(R.id.dot2);
            RecyclerView rvOrderDetails = fragment.getView().findViewById(R.id.rvDeliveryDetails);

            assertNotNull(viewPager);
            assertNotNull(dot2);
            assertNotNull(rvOrderDetails);

            // Verify fetchDeliveryDetails is called
            // This requires spying on the fragment
            HomeFragment spyFragment = spy(fragment);
            doNothing().when(spyFragment).fetchDeliveryDetails();

            spyFragment.onCreateView(null, null, null);
            verify(spyFragment, times(1)).fetchDeliveryDetails();
        });
    }
}