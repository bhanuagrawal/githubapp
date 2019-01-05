package com.example.bhanu.github.network;

import com.example.bhanu.github.network.model.Token;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GithubAuthenticationService {


    @Headers({"Accept: application/json"})
    @POST("/login/oauth/access_token")
    @FormUrlEncoded
    Call<Token> savePost(@Field("client_id") String clientId,
                         @Field("client_secret") String clientSecret,
                         @Field("code") String code,
                         @Field("redirect_uri") String redirectUri,
                         @Field("state") String state);





}
