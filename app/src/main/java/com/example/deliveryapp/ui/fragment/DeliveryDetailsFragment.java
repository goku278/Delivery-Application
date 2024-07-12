package com.example.deliveryapp.ui.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.deliveryapp.R;
import com.example.deliveryapp.config.BitmapConfig;
import com.example.deliveryapp.model.DeliveryDetails;
import com.example.deliveryapp.model.OrderDetails;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

/**
 *  This is a DeliveryDetailsFragment class
 */

public class DeliveryDetailsFragment extends Fragment {

    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int REQUEST_PERMISSION_CAMERA = 102;

    private ImageView imageView, ivGoodQuality2, ivBadQuality2;
    private ArrayList<DeliveryDetails> deliveryDetailsList;
    private TextView orderId, orderNumber, orderAmount, address;
    private EditText etAmount;
    private String orderQuality;
    private String orderDamage;
    private DeliveryDetails deliveryDetails2;
    private Button btSubmit;
    private BottomNavigationView bottomNavigationView;
    private LinearLayout llGood2, llDamaged2;
    private Spinner customSpinner;
    private String selectedItem;
    private BitmapConfig bitmapConfig;
    private byte[] bitmapImage;

    public DeliveryDetailsFragment() {

    }

    public DeliveryDetailsFragment(DeliveryDetails deliveryDetails2, BottomNavigationView bottomNavigationView) {
        this.deliveryDetailsList = Paper.book().read("deliveryDetails", deliveryDetailsList);
        this.deliveryDetails2 = deliveryDetails2;
        this.bottomNavigationView = bottomNavigationView;
        bitmapConfig = new BitmapConfig();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.delivery_details_fragment_layout, container, false);

        setSpinner(view);
        setViews(view);
        setClicks(view);
        return view;
    }

    private void setViews(View view) {
        orderId = view.findViewById(R.id.tvOrderId);
        orderNumber = view.findViewById(R.id.tvOrderNumber);
        address = view.findViewById(R.id.tvAddress);
        orderAmount = view.findViewById(R.id.tvOrderBill);

        etAmount = view.findViewById(R.id.tvAmount);

        llGood2 = view.findViewById(R.id.llGood);
        llDamaged2 = view.findViewById(R.id.llDamaged);

        ivGoodQuality2 = view.findViewById(R.id.ivGoodQuality);
        ivBadQuality2 = view.findViewById(R.id.ivBadQuality);

        btSubmit = view.findViewById(R.id.btSubmit);

        orderId.setText(this.deliveryDetails2.getOrderId());
        orderNumber.setText(this.deliveryDetails2.getOrderName());
        address.setText(this.deliveryDetails2.getAddress());
        address.setText(this.deliveryDetails2.getAddress());
        orderAmount.setText(this.deliveryDetails2.getDeliveryCost());
    }

    /**
     *
     * @param view
     *
     * Initiated onClick Listeners
     */

    private void setClicks(View view) {
        imageView = view.findViewById(R.id.ivTakePicture);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(),
                            new String[]{android.Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA);
                } else {
                    // Permission already granted; proceed with camera operation
                    dispatchTakePictureIntent();
                }
            }
        });

        llGood2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivGoodQuality2.setVisibility(View.VISIBLE);
                ivBadQuality2.setVisibility(View.GONE);
                customSpinner.setVisibility(View.GONE);
            }
        });

        llDamaged2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivGoodQuality2.setVisibility(View.GONE);
                ivBadQuality2.setVisibility(View.VISIBLE);
                customSpinner.setVisibility(View.VISIBLE);
            }
        });

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amt = etAmount.getText().toString();
                if (customSpinner.getVisibility() == View.VISIBLE) {
                    if (selectedItem == null && selectedItem.isEmpty()) {
                        Toast.makeText(requireContext(), "Please select damage type", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (amt == null || amt.isEmpty()) {
                        Toast.makeText(requireContext(), "Please input amount", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (amt != null && !amt.isEmpty()) {
                        if (Double.parseDouble(amt) > Double.parseDouble(deliveryDetails2.getDeliveryCost())) {
                            Toast.makeText(requireContext(), "Amount cannot be more than the product bill", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    if (bitmapImage == null || bitmapImage.length == 0) {
                        Toast.makeText(requireContext(), "Please capture product photo", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (ivGoodQuality2.getVisibility() == View.GONE && ivBadQuality2.getVisibility() == View.GONE) {
                        Toast.makeText(requireContext(), "Please select Order Quality", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else if (customSpinner.getVisibility() != View.VISIBLE) {
                    if (amt == null || amt.isEmpty()) {
                        Toast.makeText(requireContext(), "Please input amount", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (amt != null && !amt.isEmpty()) {
                        if (Double.parseDouble(amt) > Double.parseDouble(deliveryDetails2.getDeliveryCost())) {
                            Toast.makeText(requireContext(), "Amount cannot be more than the product bill", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    if (Double.parseDouble(amt) < Double.parseDouble(deliveryDetails2.getDeliveryCost())) {
                        Toast.makeText(requireContext(), "Amount cannot be less than the product bill", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (bitmapImage == null || bitmapImage.length == 0) {
                        Toast.makeText(requireContext(), "Please capture product photo", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (ivGoodQuality2.getVisibility() == View.GONE && ivBadQuality2.getVisibility() == View.GONE) {
                        Toast.makeText(requireContext(), "Please select Order Quality", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                OrderDetails orderDetails = new OrderDetails();
                orderDetails.setOrderAddress(deliveryDetails2.getAddress());
                orderDetails.setOrderBill(deliveryDetails2.getDeliveryCost());
                orderDetails.setOrderId(deliveryDetails2.getOrderId());
                orderDetails.setOrderDamages(selectedItem);
                orderDetails.setOrderNumber(deliveryDetails2.getOrderName());
                orderDetails.setOrderPhoto(bitmapImage);
                orderDetails.setOrderBill(deliveryDetails2.getDeliveryCost());
                orderDetails.setPaidOrderBill(amt);
                orderDetails.setCustomerName(deliveryDetails2.getCustomerName());
                if (ivBadQuality2.getVisibility() == View.VISIBLE) orderDetails.setOrderQuality("GOOD");
                else orderDetails.setOrderQuality("DAMAGED");

                Paper.book().write(deliveryDetails2.getOrderId(), orderDetails);

                Toast.makeText(requireContext(), "Successfully saved in the database", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void setSpinner(View view) {
        customSpinner = view.findViewById(R.id.custom_spinner);
        customSpinner.setVisibility(View.GONE);
        if (customSpinner != null) {
            List<String> spinnerItems = new ArrayList<>();
            spinnerItems.add("Select Damage Type");
            spinnerItems.add("Broken");
            spinnerItems.add("Seal Opened");
            spinnerItems.add("Distorted");
            spinnerItems.add("Missing");
            spinnerItems.add("Item Replaced");

            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, spinnerItems);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            customSpinner.setAdapter(adapter);

            customSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // Get the selected item
                    selectedItem = (String) parent.getItemAtPosition(position);
                    // Do something with the selected item
                    Toast.makeText(requireContext(), "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Handle case where no item is selected, if needed
                }
            });

        }
    }

    // Launch camera intent
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    // Handle camera intent result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            assert extras != null;
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
            assert imageBitmap != null;
            bitmapImage = bitmapConfig.bitmapToByteArray(imageBitmap);

        } else {
            Toast.makeText(requireContext(), "Failed to capture image", Toast.LENGTH_SHORT).show();
        }
    }

    // Handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}