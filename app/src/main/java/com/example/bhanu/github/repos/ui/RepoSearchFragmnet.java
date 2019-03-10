package com.example.bhanu.github.repos.ui;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.bhanu.github.R;
import com.example.bhanu.github.repos.GithubViewModel;
import com.example.bhanu.github.repos.datamodel.Filter;
import com.example.bhanu.github.repos.datamodel.Repo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RepoSearchFragmnet extends Fragment implements ItemAdater.ItemAdaterListner {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.editText3)
    EditText searchbar;

    @BindView(R.id.searchResult)
    RecyclerView reposRV;

    @BindView(R.id.filterIV)
    ImageView filterIV;

    private GithubViewModel githubViewModel;
    private Observer<ArrayList<Repo>> githubRepoObserver;
    private ItemAdater itemApadter;
    private Observer<Filter> filterObserver;

    public RepoSearchFragmnet() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RepoSearchFragmnet.
     */
    // TODO: Rename and change types and number of parameters
    public static RepoSearchFragmnet newInstance(String param1, String param2) {
        RepoSearchFragmnet fragment = new RepoSearchFragmnet();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        githubViewModel =
                ViewModelProviders.of(getActivity()).get(GithubViewModel.class);


        githubRepoObserver = (ArrayList<Repo> repos) ->{
            itemApadter.onDataChange(repos);
        };

        itemApadter = new ItemAdater(getContext(), this, ItemAdater.REPOS);
    }

    private void setSearchKey(Filter filtet) {
        if(filtet.getPrivacy() == Filter.PRIVACY.BOTH){
            githubViewModel.searchRepos(searchbar.getText().toString());
        }
        else if(filtet.getPrivacy() == Filter.PRIVACY.PUBLIC){
            githubViewModel.searchRepos(searchbar.getText().toString() + " is:public" );

        }
        else if(filtet.getPrivacy() == Filter.PRIVACY.PRIVATE){
            githubViewModel.searchRepos(searchbar.getText().toString() + " is:private" );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_repo_search_fragmnet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        searchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setSearchKey(githubViewModel.getFilterMutableLiveData().getValue());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        reposRV.setLayoutManager(new LinearLayoutManager(getContext()));
        reposRV.setAdapter(itemApadter);

        filterIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.openPage(RepoSearchFragmnetDirections.actionRepoSearchFragmnetToSearchFilterFragment());
            }
        });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        githubViewModel.getSearchResultRepos().observe(this, githubRepoObserver);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        githubViewModel.getSearchResultRepos().removeObserver(githubRepoObserver);

    }

    @Override
    public void onRepoSelected(int id) {
        RepoSearchFragmnetDirections.ActionRepoSearchFragmnetToRepoDetailFragment action = RepoSearchFragmnetDirections.actionRepoSearchFragmnetToRepoDetailFragment();
        action.setRepoId(id);
        mListener.openPage(action);
    }

    @Override
    public void onUserSelected(String username) {

    }
}
