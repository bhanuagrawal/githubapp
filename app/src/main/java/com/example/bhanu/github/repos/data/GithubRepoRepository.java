package com.example.bhanu.github.repos.data;

import android.app.Application;

import com.example.bhanu.github.Utils;
import com.example.bhanu.github.network.GithubService;
import com.example.bhanu.github.network.WebService;
import com.example.bhanu.github.repos.datamodel.EventVO;
import com.example.bhanu.github.repos.datamodel.Repo;
import com.example.bhanu.github.repos.datamodel.UserVO;

import java.util.ArrayList;


import retrofit2.Callback;


public class GithubRepoRepository {

    private final GithubService githubService;
    Application application;
    WebService webService;


    public GithubRepoRepository(Application application) {
        this.application = application;
        webService = new WebService(application);
        githubService = webService.getGithubClient().create(GithubService.class);
    }





    public void fetchRepos(String limit, String after, Callback<ArrayList<Repo>> callback) {

        githubService.fetchRepos("token " + Utils.getToken(application.getApplicationContext())).enqueue(callback);
    }

    public void fetNotifications(String username, Callback<ArrayList<EventVO>> callback) {
        githubService.fetchNotications("token " + Utils.getToken(application.getApplicationContext()), username).enqueue(callback);
    }

    public void getProfile(Callback<UserVO> callback){
        githubService.getProfile("token " + Utils.getToken(application.getApplicationContext())).enqueue(callback);
    }
}
