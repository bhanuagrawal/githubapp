package com.example.bhanu.github.repos;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.bhanu.github.repos.data.GithubRepoRepository;
import com.example.bhanu.github.repos.datamodel.Repo;
import com.example.bhanu.github.repos.datamodel.SearchResult;
import com.example.bhanu.github.repos.datamodel.UserVO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GithubViewModel extends AndroidViewModel {

    private final GithubRepoRepository githubRepos;
    private MutableLiveData<UserVO> userPublicProfile;
    private MutableLiveData<ArrayList<Repo>> searchResultRepos;
    private MutableLiveData<Repo> repoDetailMutableLiveData;
    private MutableLiveData<ArrayList<UserVO>> contibutersLiveData;
    private MutableLiveData<ArrayList<Repo>> userReposLiveData;

    public MutableLiveData<ArrayList<Repo>> getUserReposLiveData() {
        if(userReposLiveData == null){
            userReposLiveData = new MutableLiveData<>();
        }
        return userReposLiveData;
    }

    public MutableLiveData<ArrayList<UserVO>> getContibutersLiveData() {
        if(contibutersLiveData == null){
            contibutersLiveData = new MutableLiveData<>();
        }
        return contibutersLiveData;
    }

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

    public MutableLiveData<UserVO> getUserPublicProfile() {
        if(userPublicProfile == null){
            userPublicProfile = new MutableLiveData<>();
        }
        return userPublicProfile;
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
                Log.e("error", t.getMessage());

            }
        });
    }

    public void getRepoContributers(Repo repo) {
        githubRepos.getRepoContributers(repo, new Callback<ArrayList<UserVO>>() {
            @Override
            public void onResponse(Call<ArrayList<UserVO>> call, Response<ArrayList<UserVO>> response) {
                getContibutersLiveData().postValue(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<UserVO>> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
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
