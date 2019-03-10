package com.example.bhanu.github.network;

import android.app.Application;

import com.example.bhanu.github.Constants;
import com.example.bhanu.github.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class WebService {

    Application application;

    private static Retrofit retrofit = null;


    public Retrofit getGithubClient() {
        if (retrofit==null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
  /*          OkHttpClient.Builder defaultHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Interceptor.Chain chain) throws IOException {
                            //getAccessToken is your own accessToken(retrieve it by saving in shared preference or any other option )
                            if(Utils.getToken(application.getApplicationContext()).isEmpty()){
                                //PrintLog.error("retrofit 2","Authorization header is already present or token is empty....");
                                return chain.proceed(chain.request());
                            }
                            Request authorisedRequest = chain.request().newBuilder()
                                    .addHeader("Authorization", "token " + Utils.getToken(application.getApplicationContext())).build();
                            //PrintLog.error("retrofit 2","Authorization header is added to the url....");
                            return chain.proceed(authorisedRequest);
                        }});
*/
            OkHttpClient.Builder defaultHttpClient= new OkHttpClient.Builder();
            defaultHttpClient.addInterceptor(logging);
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL_GITHUN_APIS)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(defaultHttpClient.build())
                    .build();
        }
        return retrofit;
    }

    public WebService(Application application) {
        this.application = application;
    }
}
