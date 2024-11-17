package com.example.pbl6_android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pbl6_android.Activity.Product.DetailActivity;
import com.example.pbl6_android.Activity.Product.SearchedByCategoryActivity;
import com.example.pbl6_android.Activity.Product.SearchedProductActivity;
import com.example.pbl6_android.adapters.HomeHorAdapter;
import com.example.pbl6_android.adapters.NewProductAdapter;
import com.example.pbl6_android.adapters.RecommendedProductAdapter;
import com.example.pbl6_android.models.Category;
import com.example.pbl6_android.models.PageState;
import com.example.pbl6_android.models.Product;
import com.example.pbl6_android.Activity.Promote.PromoteActivity;
import com.example.pbl6_android.retrofit.RetrofitInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment implements RecommendedProductAdapter.OnProductClickListener , HomeHorAdapter.OnCategoryClickListener {

    private PageState pageState;
    private PageState newPageState;

    RecyclerView homeHorizontalRec;
    RecyclerView recommendedProduct;
    RecyclerView newProduct;
    List<Category> categoryList;
    HomeHorAdapter homeHorAdapter;
    RecommendedProductAdapter recommendedProductAdapter;
    NewProductAdapter newProductAdapter;
    List<Product> productList;
    List<Product> newProductList;

    TextView viewMoreNewProducts;
    TextView viewMoreRecommendedProducts;

    //Retrofit fetch api backend
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    private String BASE_URL = "http://10.0.2.2:5273/";

    private static final int PAGE_SIZE = 4;
    public HomeFragment() {
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString("param1");
            String mParam2 = getArguments().getString("param2");
        }
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        productList =new ArrayList<>();
        fetchRecommendedProducts(1);
        fetchNewProducts(1);
    }

    private void loadMoreItems(List<Product> list, RecyclerView.Adapter<?> adapter, PageState state) {
        state.isLoading = true;
        state.currentPage++;



        new Handler().postDelayed(() -> {
            if (state.currentPage <= state.totalPage) {
                list.add(new Product("Sản phẩm mới " + (list.size() + 1), 32.000, "pizza"));
                list.add(new Product("Sản phẩm mới " + (list.size() + 2), 32.000, "pizza"));
                list.add(new Product("Sản phẩm mới " + (list.size() + 3), 32.000, "pizza"));
                list.add(new Product("Sản phẩm mới " + (list.size() + 4), 32.000, "pizza"));
                adapter.notifyDataSetChanged();
            } else {
                state.isLastPage = true;
            }
            state.isLoading = false;
        }, 2000);
    }
    private void fetchRecommendedProducts(int page) {
        Call<List<Product>> call = retrofitInterface.getAllProduct(page, PAGE_SIZE);
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> fetchedProducts = response.body();

                    productList.addAll(fetchedProducts);
                    recommendedProductAdapter.notifyDataSetChanged();

                    if (fetchedProducts.size() < PAGE_SIZE) {
                        pageState.isLastPage = true;
                    }
                    pageState.isLoading = false;
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                pageState.isLoading = false;
            }
        });

    }
    private void fetchNewProducts(int page) {
        Call<List<Product>> call = retrofitInterface.getProductNew(page, PAGE_SIZE);
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> fetchedProducts = response.body();

                    productList.addAll(fetchedProducts);
                    newProductAdapter.notifyDataSetChanged();
                    if (fetchedProducts.size() < PAGE_SIZE) {
                        pageState.isLastPage = true;
                    }
                    pageState.isLoading = false;
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                pageState.isLoading = false;
            }
        });

    }
    private void loadMoreRecommendedItems(PageState state) {
        state.isLoading = true;
        state.currentPage++;
        fetchRecommendedProducts(state.currentPage);
    }


    private void setupViewMore(TextView viewMoreButton, List<Product> list, RecyclerView.Adapter<?> adapter, PageState state) {
        viewMoreButton.setOnClickListener(v -> {
            if (!state.isLoading && !state.isLastPage) {
//                loadMoreItems(list, adapter, state);
                loadMoreRecommendedItems(state);
            }
        });
    }

    private void setupViewMore2(TextView viewMoreButton, PageState state) {
        viewMoreButton.setOnClickListener(v -> {
            if (!state.isLoading && !state.isLastPage) {
                loadMoreRecommendedItems(state); // Fetch more products on click
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        homeHorizontalRec = root.findViewById(R.id.home_hor_rec);
        categoryList = new ArrayList<>();
        categoryList.add(new Category(R.drawable.pizza, "Đồ ăn vặt"));
        categoryList.add(new Category(R.drawable.hamburger, "Món chính"));
        categoryList.add(new Category(R.drawable.sandwich, "Giải khát"));
        categoryList.add(new Category(R.drawable.ice_cream, "Đồ ngọt"));
        categoryList.add(new Category(R.drawable.potatoes, "Đồ mặn"));
        homeHorAdapter = new HomeHorAdapter(getActivity(), categoryList,this);
        homeHorizontalRec.setAdapter(homeHorAdapter);
        homeHorizontalRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));

        recommendedProduct = root.findViewById(R.id.recommended_product);

        recommendedProductAdapter = new RecommendedProductAdapter(getActivity(), productList,this);
        recommendedProduct.setAdapter(recommendedProductAdapter);
        recommendedProduct.setLayoutManager(new GridLayoutManager(getActivity(), 2));
//        pageState = new PageState();

        pageState = new PageState();
        pageState.currentPage = 1;  // Set initial page

        viewMoreRecommendedProducts = root.findViewById(R.id.view_more_recommended_products);
        setupViewMore2(viewMoreRecommendedProducts, pageState);

        newProduct = root.findViewById(R.id.new_product);
        newProductList = new ArrayList<>();

        newProductAdapter = new NewProductAdapter(getActivity(), productList);
        newProduct.setAdapter(newProductAdapter);
        newProduct.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        newPageState = new PageState();
        newPageState.currentPage=1;
        newProduct.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!pageState.isLoading && !pageState.isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= PAGE_SIZE) {
                        loadMoreTrendingItems(pageState);
                    }
                }
            }
        });
        viewMoreNewProducts = root.findViewById(R.id.view_more_new_products);
        setupViewMore(viewMoreNewProducts, newProductList, newProductAdapter, newPageState);


        ImageView promote = root.findViewById(R.id.discountIcon);
        promote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), PromoteActivity.class);
                startActivity(i);
            }
        });

        ImageView cartIcon= root.findViewById(R.id.cartIcon);
        cartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),CartActivity.class);
                startActivity(i);
            }
        });

        EditText editText = root.findViewById(R.id.search_bar);

        editText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() <= (editText.getCompoundDrawables()[0].getBounds().width() + editText.getPaddingLeft())) {
                    String searchQuery = editText.getText().toString().trim();

                    Intent intent = new Intent(root.getContext(), SearchedProductActivity.class);
                    intent.putExtra("product", searchQuery);
                    startActivity(intent);
                    v.performClick();
                    return true;
                }
            }
            return false;
        });

        return root;
    }
//    private void fetchNewProducts(int page) {
//        Call<List<Product>> call = retrofitInterface.getProductNew(page, PAGE_SIZE);
//        call.enqueue(new Callback<List<Product>>() {
//            @Override
//            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    List<Product> fetchedProducts = response.body();
//
//                    productList.addAll(fetchedProducts);
//                    newProductAdapter.notifyDataSetChanged();
//
//                    if (fetchedProducts.size() < PAGE_SIZE) {
//                        pageState.isLastPage = true;
//                    }
//                    pageState.isLoading = false;
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Product>> call, Throwable t) {
//                pageState.isLoading = false;
//            }
//        });
//    }



    @Override
    public void onProductClick(Product product) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("product",product);

        startActivity(intent);
    }

    @Override
    public void onCategoryClick(Category category) {
        Intent intent = new Intent(getActivity(), SearchedByCategoryActivity.class);
        intent.putExtra("categoryName",category.getName());
        System.out.println("category name:" + category.getName());
        startActivity(intent);
    }
    private void loadMoreTrendingItems(PageState state) {
        state.isLoading = true;
        state.currentPage++;
        fetchRecommendedProducts(state.currentPage);
    }
}
