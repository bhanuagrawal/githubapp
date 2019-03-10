package com.example.bhanu.github.network;

import com.example.bhanu.github.repos.datamodel.Repo;
import com.example.bhanu.github.repos.datamodel.SearchResult;
import com.example.bhanu.github.repos.datamodel.UserVO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface GithubService {

    @Headers({"Accept: application/json"})
    @GET("/search/repositories")
    Call<SearchResult> searcbRepos(@Query("q") String keyword);


    @Headers({"Accept: application/json"})
    @GET("/repositories/{id}")
    Call<Repo> getRepoDetail(@Path(value = "id", encoded = true)int repoId);

    @Headers({"Accept: application/json"})
    @GET
    Call<ArrayList<UserVO>> getRepoContributers(@Url String html_url);

    @Headers({"Accept: application/json"})
    @GET("/users/{username}")
    Call<UserVO> getPublicProfile(@Path(value = "username", encoded = true)String username);

    @Headers({"Accept: application/json"})
    @GET
    Call<ArrayList<Repo>> getUserRepos(@Url String url);
}
