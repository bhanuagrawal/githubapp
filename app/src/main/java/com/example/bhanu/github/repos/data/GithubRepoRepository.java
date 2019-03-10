package com.example.bhanu.github.repos.data;

import android.app.Application;

import com.example.bhanu.github.network.GithubService;
import com.example.bhanu.github.network.WebService;
import com.example.bhanu.github.repos.datamodel.Repo;
import com.example.bhanu.github.repos.datamodel.SearchResult;
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


    public void searchRepos(String keyword, Callback<SearchResult> callback){
        githubService.searcbRepos(keyword).enqueue(callback);
    }

    public void getRepoDetail(int id, Callback<Repo> callback){
        githubService.getRepoDetail(id).enqueue(callback);
    }

    public void getRepoContributers(Repo repo, Callback<ArrayList<UserVO>> callback) {
        githubService.getRepoContributers(repo.getContributors_url()).enqueue(callback);
    }

    public void getPublicProfile(String username, Callback<UserVO> callback) {
        githubService.getPublicProfile(username).enqueue(callback);
    }

    public void getUserRepos(String url, Callback<ArrayList<Repo>> callback) {
        githubService.getUserRepos(url).enqueue(callback);
    }
}
