package com.example.pbl6_android;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.pbl6_android.adapters.RewardAdapter;
import com.example.pbl6_android.models.MissionCheckModel;
import com.example.pbl6_android.retrofit.RetrofitInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MissionFragment extends Fragment {

    private RecyclerView rewardRecyclerView;
    private String BASE_URL = "http://10.0.2.2:5273/";
    private Retrofit retrofit;

    private MissionCheckModel result;
    private RetrofitInterface retrofitInterface;

    private void checkUserBeginnerMission(UUID userId)  {
        Call<MissionCheckModel> call = retrofitInterface.checkUserBeginnerMission(userId);
        call.enqueue(new Callback<MissionCheckModel>() {
            @Override
            public void onResponse(Call<MissionCheckModel> call, Response<MissionCheckModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                        result = response.body();
                    Map<String, String> missionStatuses = result.getResult();
                    updateMissionUI(missionStatuses);
                }
                else {
                    Log.e("API_ERROR", "Failed to fetch mission status ");
                }

                }

            @Override
            public void onFailure(Call<MissionCheckModel> call, Throwable t) {
                Log.e("API_ERROR", "Error" + t.getMessage(), t);

            }
        });
    };
    private void updateMissionUI(Map<String, String> missionStatuses) {
        // Get references to ImageViews
        ImageView check1 = getView().findViewById(R.id.check1);
        ImageView check2 = getView().findViewById(R.id.check2);
        ImageView check3 = getView().findViewById(R.id.check3);
        ImageView check4 = getView().findViewById(R.id.check4);
        ImageView check5 = getView().findViewById(R.id.check5);

        // Map of ImageViews
        List<ImageView> checkImages = new ArrayList<>();
        checkImages.add(check1);
        checkImages.add(check2);
        checkImages.add(check3);
        checkImages.add(check4);
        checkImages.add(check5);

        // Iterate through the missions
        int i = 0;
        for (Map.Entry<String, String> entry : missionStatuses.entrySet()) {
            ImageView checkImage = checkImages.get(i);

            // Check completion status
            if ("completed".equalsIgnoreCase(entry.getValue())) {
                checkImage.setImageResource(R.drawable.ic_check_green); // Green check
            } else {
                checkImage.setImageResource(R.drawable.ic_check_gray); // Gray check
            }
            i++;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mission, container, false);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        checkUserBeginnerMission(UUID.fromString("d4e56743-ff2c-41d3-957d-576e9f574c5d"));

        rewardRecyclerView = view.findViewById(R.id.reward_recycler_view);

        List<Integer> imageResIds = new ArrayList<>();
        imageResIds.add(R.drawable.basecap);
        imageResIds.add(R.drawable.dark_jacket);
        imageResIds.add(R.drawable.darkuniform);
        imageResIds.add(R.drawable.jacket);
        imageResIds.add(R.drawable.uniform_front);


        RewardAdapter adapter = new RewardAdapter(imageResIds);
        rewardRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        rewardRecyclerView.setAdapter(adapter);

        return view;
    }
}
