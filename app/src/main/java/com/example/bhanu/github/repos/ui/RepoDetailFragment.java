package com.example.bhanu.github.repos.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bhanu.github.R;
import com.example.bhanu.github.repos.viewmodels.RepoViewModel;
import com.example.bhanu.github.repos.datamodel.Repo;
import com.example.bhanu.github.repos.datamodel.UserVO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RepoDetailFragment extends Fragment implements ItemAdater.ItemAdaterListner {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    @BindView(R.id.imageView3)
    ImageView avatar;

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.description)
    TextView description;

    @BindView(R.id.link)
    TextView link;

    @BindView(R.id.recyclerview)
    RecyclerView contributersRV;

    private OnFragmentInteractionListener mListener;
    private RepoViewModel repoViewModel;
    private Observer<Repo> githubRepoObserver;
    private ItemAdater itemApadter;
    private Observer<ArrayList<UserVO>> contributersObsever;
    private int repoId;

    public RepoDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RepoDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RepoDetailFragment newInstance(String param1, String param2) {
        RepoDetailFragment fragment = new RepoDetailFragment();
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
            repoId = RepoDetailFragmentArgs.fromBundle(getArguments()).getRepoId();

        }

        repoViewModel =
                ViewModelProviders.of(getActivity()).get(RepoViewModel.class);

        repoViewModel.getRepoDetail(repoId);
        githubRepoObserver = (Repo repo) ->{
            bindView(repo);
            repoViewModel.getRepoContributers(repo);
        };


        contributersObsever = (ArrayList<UserVO> users) -> {
            itemApadter.onDataChange(users);

        };

        itemApadter = new ItemAdater(getContext(), this, ItemAdater.USERS);


    }

    private void bindView(Repo repo) {
        name.setText(repo.getName());
        description.setText(repo.getDescription());
        link.setText(repo.getHtml_url());
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(repo.getHtml_url()));
                startActivity(browserIntent);
            }
        });
        Glide.with(getContext())
                .load(repo.getOwner().getAvatar_url())
                .into(avatar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_repo_detail, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        contributersRV.setLayoutManager(new GridLayoutManager(getContext(), 3));
        contributersRV.setAdapter(itemApadter);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        repoViewModel.getRepoDetailMutableLiveData().observe(this, githubRepoObserver);
        repoViewModel.getContibutersLiveData().observe(this, contributersObsever);
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
        repoViewModel.getRepoDetailMutableLiveData().removeObserver(githubRepoObserver);
        repoViewModel.getContibutersLiveData().removeObserver(contributersObsever);
    }

    @Override
    public void onRepoSelected(int id) {


    }

    @Override
    public void onUserSelected(String username) {
        mListener.openUserDetailPage(username);

    }

}
