package com.example.pbl6_android;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.pbl6_android.models.Location;
import com.example.pbl6_android.models.Product;
import com.example.pbl6_android.retrofit.RetrofitInterface;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment  implements OnMapReadyCallback {

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    private String BASE_URL = "http://10.0.2.2:5273/";

    List<Product> productList = new ArrayList<>();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private GoogleMap googleMap;

    private Spinner categorySpinner;
    private List<Marker> currentMarkers = new ArrayList<>();

    private boolean isLoading = false;
    private boolean isFirstSelection = true; // Cờ để theo dõi lần chọn đầu tiên


    public MapFragment() {
        // Required empty public constructor
    }


    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private void fetchSearchedProducts(String searchQuery) {

        isLoading = true;

        Call<List<Product>> call = retrofitInterface.getAllProductsByCategory(searchQuery,true);
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> fetchedProducts = response.body();
                    productList.addAll(fetchedProducts);
                    if (isMapReady) { // Ensure the map is ready
                        addMarkersToMap();
                    }else {
                        // Đợi bản đồ sẵn sàng
                        Log.e("GoogleMap", "Google Map is not ready yet");
                    }

                }
                isLoading = false;

            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("API_ERROR", "Failed to fetch product: " + t.getMessage(), t);
            }
        });
    }
    private void addMarkersToMap() {
        if (productList.isEmpty()) {
            Log.d("MapFragment", "No products to display markers.");
            return; // Không thêm marker nếu danh sách sản phẩm trống
        }

//        for (Marker marker : currentMarkers) {
//            marker.remove();
//        }
//        currentMarkers.clear();
        for (Product product : productList) {
            if (product.getBrand() != null && product.getBrand().getLocations() != null) {
                for (Location location : product.getBrand().getLocations()) {
                    geocodeAddress(location.getName());
                }
            }
        }
    }

    private void clearMapAndData() {
        Handler handler = new Handler(Looper.getMainLooper());

        // Xóa tất cả marker khỏi bản đồ
        handler.post(() -> {
            for (Marker marker : currentMarkers) {
                if (marker != null) {
                    marker.remove();
                }
            }
            currentMarkers.clear(); // Dọn dẹp danh sách marker
        });

        // Xóa danh sách sản phẩm
        productList.clear();
        Log.d("ClearMapData", "Markers and products cleared");
    }


    private void geocodeAddress(String address) {
        ExecutorService executor = Executors.newFixedThreadPool(4);  // Tăng số luồng
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            Geocoder geocoder = new Geocoder(getContext());
            try {
                List<Address> addressList = geocoder.getFromLocationName(address, 1);
                if (addressList != null && !addressList.isEmpty()) {
                    Address location = addressList.get(0);
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    handler.post(() -> {
                        LatLng latLng = new LatLng(latitude, longitude);
                        Marker marker = googleMap.addMarker(new MarkerOptions().position(latLng).title(address));
                        currentMarkers.add(marker);
                    });
                } else {
                    Log.e("Geocoding", "Không tìm thấy địa chỉ: " + address);
                }
            } catch (IOException e) {
                Log.e("Geocoding", "Error getting location from address", e);
            }
        });
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        categorySpinner = view.findViewById(R.id.productTypeSpinner);

        List<String> categories = new ArrayList<>();
        categories.add("Đồ ăn vặt");
        categories.add("Món chính");
        categories.add("Giải khát");
        categories.add("Đồ ngọt");
        categories.add("Đồ mặn");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_item, categories);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Gán adapter vào Spinner
        categorySpinner.setAdapter(adapter);

//        int defaultPosition = categories.indexOf("Món chính"); // Tìm vị trí của "Món chính" trong danh sách
//        if (defaultPosition != -1) {
//        }
//            categorySpinner.setSelection(defaultPosition); // Đặt giá trị mặc định


        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (isFirstSelection) {
                    isFirstSelection = false; // Bỏ qua lần chọn đầu tiên
                    return;
                }


                String selectedCategory = parentView.getItemAtPosition(position).toString();

                clearMapAndData();
                fetchSearchedProducts(selectedCategory);
                Log.d("SpinnerItem", "Selected: " + selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
//                String defaultCategory = "Món chính"; // Hoặc danh mục mặc định của bạn
//                fetchSearchedProducts(defaultCategory);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this); // Gọi phương thức getMapAsync
        }
        return view;
    }
    private boolean isMapReady = false;

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        isMapReady = true;
        LatLng danang = new LatLng(16.0544, 108.2022);
        float zoomLevel = 12.0f;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(danang, zoomLevel));
//
//        if (!productList.isEmpty()) {
//            addMarkersToMap();
//        }
    }
}