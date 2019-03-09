package com.example.bhanu.github.network;

import com.example.bhanu.github.repos.datamodel.EventVO;
import com.example.bhanu.github.repos.datamodel.Repo;
import com.example.bhanu.github.repos.datamodel.SearchResult;
import com.example.bhanu.github.repos.datamodel.UserVO;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GithubService {

    @Headers({"Accept: application/json"})
    @GET("/user/repos")
    Call<ArrayList<Repo>> fetchRepos(@Header("Authorization") String token);


    @Headers({"Accept: application/json"})
    @GET("/users/{username}/events")
    Call<ArrayList<EventVO>> fetchNotications(@Header("Authorization") String token, @Path(value = "username", encoded = true)String username);



    @Headers({"Accept: application/json"})
    @GET("/user")
    Call<UserVO> getProfile(@Header("Authorization") String token);

    @Headers({"Accept: application/json"})
    @GET("/search/repositories")
    Call<SearchResult> searcbRepos(@Query("q") String keyword);


    @Headers({"Accept: application/json"})
    @GET("/repositories/{id}")
    Call<Repo> getRepoDetail(@Path(value = "id", encoded = true)int repoId);



}
