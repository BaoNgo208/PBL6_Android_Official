package com.example.pbl6_android;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pbl6_android.adapters.OrderStatusAdapter;
import com.example.pbl6_android.models.Category;
import com.example.pbl6_android.models.OrderStatus;
import com.example.pbl6_android.retrofit.RetrofitInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AboutFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AboutFragment() {
        // Required empty public constructor
    }



    RecyclerView orderStatusRec;
    List<OrderStatus> orderStatusList;
    OrderStatusAdapter orderStatusAdapter;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    private String BASE_URL = "http://10.0.2.2:5273/";

    public static AboutFragment newInstance(String param1, String param2) {
        AboutFragment fragment = new AboutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            retrofitInterface = retrofit.create(RetrofitInterface.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about, container, false);

        orderStatusRec = root.findViewById(R.id.order_status_rec);
        orderStatusList =new ArrayList<>();
        orderStatusList.add(new OrderStatus(R.drawable.dahuy, "Đã thanh toán"));
        orderStatusList.add(new OrderStatus(R.drawable.dahuy, "Đã hủy"));
        orderStatusList.add(new OrderStatus(R.drawable.dahuy, "Đang chờ"));
        orderStatusList.add(new OrderStatus(R.drawable.dahuy, "Đã nhận"));
        orderStatusAdapter = new OrderStatusAdapter(getActivity(),orderStatusList);
        orderStatusRec.setAdapter(orderStatusAdapter);
        orderStatusRec.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));


        return root;
    }
}