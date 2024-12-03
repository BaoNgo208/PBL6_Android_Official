package com.example.pbl6_android;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pbl6_android.adapters.OrderStatusAdapter;
import com.example.pbl6_android.adapters.SuggestedByCategoryAdapter;
import com.example.pbl6_android.models.Category;
import com.example.pbl6_android.models.OrderStatus;
import com.example.pbl6_android.models.PageState;
import com.example.pbl6_android.models.Product;
import com.example.pbl6_android.retrofit.RetrofitInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AboutFragment extends Fragment implements SuggestedByCategoryAdapter.OnSuggestedProductClickListener {


    RecyclerView orderStatusRec;
    List<OrderStatus> orderStatusList;
    OrderStatusAdapter orderStatusAdapter;
    SuggestedByCategoryAdapter adapter;

    RecyclerView suggestedProductRec;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    private String BASE_URL = "http://10.0.2.2:5273/";
    private static final int PAGE_SIZE = 4;

    List<Product> productList;
    private PageState pageState;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AboutFragment() {
        // Required empty public constructor
    }




    public static AboutFragment newInstance(String param1, String param2) {
        AboutFragment fragment = new AboutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private void fetchSuggestedProductByCategory(UUID userId,int page) {
        System.out.println("api gate check");

        Call<List<Product>> call = retrofitInterface.getProductSuggestedByCategory(userId,page,PAGE_SIZE);
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> fetchedProducts = response.body();
                    System.out.println("fetched list size:" + fetchedProducts.size());
                    productList.addAll(fetchedProducts);

                    adapter.notifyDataSetChanged();

                    if (fetchedProducts.size() < PAGE_SIZE) {
                        pageState.isLastPage = true;
                    }
                    pageState.isLoading = false;
                }
                else {
                    Log.e("Retrofit", "Fetch failed with code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("Retrofit", "Fetch failed: " + t.getMessage(), t);
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
        fetchSuggestedProductByCategory(UUID.fromString("d4e56743-ff2c-41d3-957d-576e9f574c5d"),1);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about, container, false);
        productList =new ArrayList<>();

        orderStatusRec = root.findViewById(R.id.order_status_rec);
        orderStatusList =new ArrayList<>();
        orderStatusList.add(new OrderStatus(R.drawable.icondangcho, "Đang chờ"));
        orderStatusList.add(new OrderStatus(R.drawable.icondathanhtoan1, "Đã thanh toán"));
        orderStatusList.add(new OrderStatus(R.drawable.icondahuy, "Đã hủy"));
        orderStatusList.add(new OrderStatus(R.drawable.icondanhgia1, "Đánh giá"));
        orderStatusAdapter = new OrderStatusAdapter(getActivity(),orderStatusList);
        orderStatusRec.setAdapter(orderStatusAdapter);
        orderStatusRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));


        suggestedProductRec = root.findViewById(R.id.recommended_product);
        adapter = new SuggestedByCategoryAdapter(getActivity(),productList,this);
        suggestedProductRec.setAdapter(adapter);
        suggestedProductRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));

        pageState = new PageState();
        pageState.currentPage = 1;  // Set initial page
        // Add scroll listener for pagination
        suggestedProductRec.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null && !pageState.isLoading && !pageState.isLastPage) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= PAGE_SIZE) {
                        pageState.isLoading = true;
                        pageState.currentPage++;
                        fetchSuggestedProductByCategory(UUID.fromString("d4e56743-ff2c-41d3-957d-576e9f574c5d"), pageState.currentPage);
                    }
                }
            }
        });

        return root;
    }

    @Override
    public void onSuggestedProductClick(Product product) {

    }
}