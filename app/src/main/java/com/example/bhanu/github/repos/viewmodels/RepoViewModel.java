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

public class RepoViewModel extends AndroidViewModel {

    private MutableLiveData<Repo> repoDetailMutableLiveData;
    private MutableLiveData<ArrayList<UserVO>> contibutersLiveData;
    private final GithubRepoRepository githubRepos;
    private Call<ArrayList<UserVO>> repoContributersCall;


    public RepoViewModel(@NonNull Application application) {
        super(application);
        githubRepos = new GithubRepoRepository(application);
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
        if(repoContributersCall != null){
            repoContributersCall.cancel();
        }

        repoContributersCall = githubRepos.getRepoContributers(repo, new Callback<ArrayList<UserVO>>() {
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

    public MutableLiveData<Repo> getRepoDetailMutableLiveData() {
        if(repoDetailMutableLiveData == null){
            repoDetailMutableLiveData = new MutableLiveData<>();
        }
        return repoDetailMutableLiveData;
    }

    public MutableLiveData<ArrayList<UserVO>> getContibutersLiveData() {
        if(contibutersLiveData == null){
            contibutersLiveData = new MutableLiveData<>();
        }
        return contibutersLiveData;
    }
}
