package com.example.bhanu.github.repos.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.bhanu.github.repos.data.GithubRepoRepository;
import com.example.bhanu.github.repos.datamodel.Filter;
import com.example.bhanu.github.repos.datamodel.Repo;
import com.example.bhanu.github.repos.datamodel.SearchResult;
import com.example.bhanu.github.repos.datamodel.UserVO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GithubViewModel extends AndroidViewModel {

    private final GithubRepoRepository githubRepos;
    private MutableLiveData<ArrayList<Repo>> searchResultRepos;
    private Call<SearchResult> repoSearchCall;
    private MutableLiveData<Filter> filterMutableLiveData;

    public MutableLiveData<Filter> getFilterMutableLiveData() {
        return filterMutableLiveData;
    }



    public MutableLiveData<ArrayList<Repo>> getSearchResultRepos() {
        if(searchResultRepos == null){
            searchResultRepos = new MutableLiveData<>();
        }
        return searchResultRepos;
    }





    public GithubViewModel(@NonNull Application application){
        super(application);
        githubRepos = new GithubRepoRepository(application);
        filterMutableLiveData = new MutableLiveData<>();
        Filter filter = new Filter();
        filter.setPrivacy(Filter.PRIVACY.BOTH);
        filterMutableLiveData.postValue(filter);
    }

    public void searchRepos(String keyword) {


        if(repoSearchCall != null){
            repoSearchCall.cancel();

        }

        repoSearchCall = githubRepos.searchRepos(keyword, new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {



                if(response.isSuccessful()){

                    ArrayList<Repo> repos = response.body().getItems();
                    if(!response.body().getItems().isEmpty()){

                        Collections.sort(repos, new Comparator<Repo>() {
                            @Override
                            public int compare(Repo o1, Repo o2) {
                                return o2.getWatchers_count()-o1.getWatchers_count();
                            }
                        });

                    }
                    try {
                        getSearchResultRepos().postValue(new ArrayList<Repo>(repos.subList(0, 10)));
                    }
                    catch (Exception e){
                        getSearchResultRepos().postValue(repos);
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                Log.e("error", t.getMessage());


            }
        });
    }


}
