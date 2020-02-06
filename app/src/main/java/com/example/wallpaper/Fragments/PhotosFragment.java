package com.example.wallpaper.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.wallpaper.Adaptors.PhotosAdaptor;
import com.example.wallpaper.Models.Photo;
import com.example.wallpaper.R;
import com.example.wallpaper.Webservices.ApiInterface;
import com.example.wallpaper.Webservices.ServiceGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotosFragment extends Fragment {

    private final String TAG = PhotosFragment.class.getSimpleName();
    @BindView(R.id.fragment_photos_progressBar)
    ProgressBar progressBar;
    @BindView(R.id.fragment_photos_recycleview)
    RecyclerView recyclerView;

    private PhotosAdaptor photosAdaptor;
    private List<Photo> photos = new ArrayList<>();

    private Unbinder unbinder;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photos, container, false);
        //recycler view
        unbinder = ButterKnife.bind(this, view);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        //linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);

        photosAdaptor = new PhotosAdaptor(getActivity(), photos);
        recyclerView.setAdapter(photosAdaptor);

        showProgressBar(true);
        getPhotos();


        return view;

    }

    private void getPhotos(){
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<List<Photo>> call = apiInterface.getPhotos();
        call.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                if(response.isSuccessful()){

                    photos.addAll(response.body());
                    photosAdaptor.notifyDataSetChanged();
                }else{

                    Log.e(TAG, "fail " + response.message());

                }
                showProgressBar(false);
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {

                if (t instanceof IOException) {
                    Log.e(TAG, "this is actual network failure " + t.getMessage());
                    // logging probably not necessary
                }
                else {
                    Log.e(TAG, "this is a conversion issue " + t.getMessage());
                    // todo log to some central bug tracking service
                }
                showProgressBar(false);

            }
        });

    }


    private void showProgressBar(boolean isShow){
        if(isShow){
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
