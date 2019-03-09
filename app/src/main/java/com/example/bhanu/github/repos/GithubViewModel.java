package com.example.bhanu.github.repos;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.bhanu.github.repos.data.GithubRepoRepository;
import com.example.bhanu.github.repos.datamodel.EventVO;
import com.example.bhanu.github.repos.datamodel.Repo;
import com.example.bhanu.github.repos.datamodel.SearchResult;
import com.example.bhanu.github.repos.datamodel.UserVO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GithubViewModel extends AndroidViewModel {

    private final GithubRepoRepository githubRepos;
    private MutableLiveData<ArrayList<Repo>> repos;
    private MutableLiveData<ArrayList<EventVO>> notifications;
    private MutableLiveData<Boolean> validToken;
    private MutableLiveData<UserVO> userProfile;
    private MutableLiveData<ArrayList<Repo>> searchResultRepos;
    private MutableLiveData<Repo> repoDetailMutableLiveData;

    public MutableLiveData<Repo> getRepoDetailMutableLiveData() {
        if(repoDetailMutableLiveData == null){
            repoDetailMutableLiveData = new MutableLiveData<>();
        }
        return repoDetailMutableLiveData;
    }

    public MutableLiveData<ArrayList<Repo>> getSearchResultRepos() {
        if(searchResultRepos == null){
            searchResultRepos = new MutableLiveData<>();
        }
        return searchResultRepos;
    }

    public MutableLiveData<UserVO> getUserProfile() {
        if(userProfile == null){
            userProfile = new MutableLiveData<>();
            githubRepos.getProfile(new Callback<UserVO>() {
                @Override
                public void onResponse(Call<UserVO> call, Response<UserVO> response) {
                    if(response.isSuccessful()){
                        userProfile.postValue(response.body());
                        validToken.postValue(true);
                    }
                    else if(response.code() == 401){
                        validToken.postValue(false);
                    }
                }

                @Override
                public void onFailure(Call<UserVO> call, Throwable t) {

                }
            });
        }
        return userProfile;
    }



    public MutableLiveData<ArrayList<EventVO>> getNotifications() {
        if(notifications == null){
            notifications = new MutableLiveData<>();
            githubRepos.fetNotifications(getUserProfile().getValue().getLogin(), new Callback<ArrayList<EventVO>>() {
                @Override
                public void onResponse(Call<ArrayList<EventVO>> call, Response<ArrayList<EventVO>> response) {
                    if(response.isSuccessful()){
                        notifications.postValue(response.body());
                        validToken.postValue(true);
                    }
                    else if(response.code() == 401){
                        validToken.postValue(false);
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<EventVO>> call, Throwable t) {

                }
            });
        }
        return notifications;
    }



    public MutableLiveData<Boolean> getValidToken() {

        if(validToken == null){
            validToken = new MutableLiveData<>();
            validToken.postValue(true);
        }
        return validToken;
    }


    public MutableLiveData<ArrayList<Repo>> getRepos() {
        if(repos == null){
            repos = new MutableLiveData<>();
        }
        return repos;
    }


    public void fetchRepofromServer(){

        if(repos == null){
            return;
        }

        githubRepos.fetchRepos("", "", new Callback<ArrayList<Repo>>() {
            @Override
            public void onResponse(Call<ArrayList<Repo>> call, Response<ArrayList<Repo>> response) {
                if(response.isSuccessful()){
                    repos.postValue(response.body());
                    validToken.postValue(true);
                }
                else if(response.code() == 401){
                    validToken.postValue(false);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Repo>> call, Throwable t) {
            }
        });

    }

    public void setRepos(MutableLiveData<ArrayList<Repo>> repos) {
        this.repos = repos;
    }

    public GithubViewModel(@NonNull Application application){
        super(application);
        githubRepos = new GithubRepoRepository(application);
    }

    public void searchRepos(String keyword) {
        githubRepos.searchRepos(keyword, new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {

                if(response.isSuccessful()){
                    try {
                        getSearchResultRepos().postValue(new ArrayList<Repo>(response.body().getItems().subList(0, 10)));
                    }
                    catch (Exception e){
                        getSearchResultRepos().postValue(response.body().getItems());
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                Log.e("error", t.getMessage());


            }
        });
    }

    public void getRepoDetail(int id) {
        githubRepos.getRepoDetail(id, new Callback<Repo>() {
            @Override
            public void onResponse(Call<Repo> call, Response<Repo> response) {
                getRepoDetailMutableLiveData().postValue(response.body());
            }

            @Override
            public void onFailure(Call<Repo> call, Throwable t) {

            }
        });
    }
}
