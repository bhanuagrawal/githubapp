package com.example.bhanu.github.repos.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.bhanu.github.repos.data.GithubRepoRepository;
import com.example.bhanu.github.repos.datamodel.Repo;
import com.example.bhanu.github.repos.datamodel.UserVO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends AndroidViewModel {


    private final GithubRepoRepository githubRepos;
    private MutableLiveData<UserVO> userPublicProfile;
    private MutableLiveData<ArrayList<Repo>> userReposLiveData;


    public UserViewModel(@NonNull Application application) {
        super(application);
        githubRepos = new GithubRepoRepository(application);
    }

    public void fetchUserDetails(String username) {
        githubRepos.getPublicProfile(username, new Callback<UserVO>() {
            @Override
            public void onResponse(Call<UserVO> call, Response<UserVO> response) {
                getUserPublicProfile().postValue(response.body());
            }

            @Override
            public void onFailure(Call<UserVO> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }

    public MutableLiveData<UserVO> getUserPublicProfile() {
        if(userPublicProfile == null){
            userPublicProfile = new MutableLiveData<>();
        }
        return userPublicProfile;
    }

    public MutableLiveData<ArrayList<Repo>> getUserReposLiveData() {
        if(userReposLiveData == null){
            userReposLiveData = new MutableLiveData<>();
        }
        return userReposLiveData;
    }

    public void getUserRepos(UserVO user) {
        githubRepos.getUserRepos(user.getRepos_url(), new Callback<ArrayList<Repo>>() {
            @Override
            public void onResponse(Call<ArrayList<Repo>> call, Response<ArrayList<Repo>> response) {
                getUserReposLiveData().postValue(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Repo>> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }
}
