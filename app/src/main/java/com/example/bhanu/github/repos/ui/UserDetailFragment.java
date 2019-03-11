package com.example.bhanu.github.repos.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bhanu.github.R;
import com.example.bhanu.github.repos.viewmodels.UserViewModel;
import com.example.bhanu.github.repos.datamodel.Repo;
import com.example.bhanu.github.repos.datamodel.UserVO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserDetailFragment extends Fragment implements ItemAdater.ItemAdaterListner {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private String username;
    private Observer<UserVO> userProfileObserver;
    private ItemAdater itemApadter;


    @BindView(R.id.imageView4)
    ImageView avatar;

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.recyclerview)
    RecyclerView reposRV;
    private Observer<ArrayList<Repo>> githubRepoObserver;
    private UserViewModel userViewModel;

    public UserDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserDetailFragment newInstance(String param1, String param2) {
        UserDetailFragment fragment = new UserDetailFragment();
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
            username = UserDetailFragmentArgs.fromBundle(getArguments()).getUsername();

        }



        userViewModel =
                ViewModelProviders.of(this).get(UserViewModel.class);


        userViewModel.fetchUserDetails(username);
        userProfileObserver = (UserVO user) ->{
            bindView(user);
            userViewModel.getUserRepos(user);
        };


        githubRepoObserver = (ArrayList<Repo> repos) ->{
            itemApadter.onDataChange(repos);
        };

        itemApadter = new ItemAdater(getContext(), this, ItemAdater.REPOS);

    }

    private void bindView(UserVO user) {
        name.setText(user.getName());
        Glide.with(getContext())
                .load(user.getAvatar_url())
                .into(avatar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        reposRV.setLayoutManager(new LinearLayoutManager(getContext()));
        reposRV.setAdapter(itemApadter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userViewModel.getUserPublicProfile().observe(this, userProfileObserver);
        userViewModel.getUserReposLiveData().observe(this, githubRepoObserver);

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
        userViewModel.getUserPublicProfile().removeObserver(userProfileObserver);
        userViewModel.getUserReposLiveData().removeObserver(githubRepoObserver);
    }

    @Override
    public void onRepoSelected(int id) {
        UserDetailFragmentDirections.ActionUserDetailFragmentToRepoDetailFragment action = UserDetailFragmentDirections.actionUserDetailFragmentToRepoDetailFragment();
        action.setRepoId(id);
        mListener.openPage(action);
    }

    @Override
    public void onUserSelected(String username) {

    }

}
