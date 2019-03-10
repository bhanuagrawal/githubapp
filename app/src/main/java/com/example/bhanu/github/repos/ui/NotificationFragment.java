package com.example.bhanu.github.repos.ui;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bhanu.github.R;
import com.example.bhanu.github.Utils;
import com.example.bhanu.github.repos.GithubViewModel;
import com.example.bhanu.github.repos.datamodel.EventVO;
import com.example.bhanu.github.repos.datamodel.UserVO;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NotificationFragment extends Fragment implements ItemAdater.ItemAdaterListner {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    GithubViewModel githubViewModel;
    private Observer<ArrayList<EventVO>> githubNotificationVOObserver;
    private Observer<UserVO> userProfileObserver;
    private ItemAdater itemApadter;


    @BindView(R.id.notificationRV)
    RecyclerView notificationsRV;

    private Observer<Boolean> tokenExpiryObserver;


    public NotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment notificationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
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
        githubNotificationVOObserver = (ArrayList<EventVO> events) ->{
            itemApadter.onDataChange(events);
        };


        tokenExpiryObserver = (Boolean tokenValid) -> {
            if(!tokenValid){
                mListener.openloginPage();
            }
        };

        userProfileObserver = (UserVO user) ->{
            githubViewModel.getNotifications().observe(getActivity(), githubNotificationVOObserver);
            githubViewModel.getNotifications();
        };




        itemApadter = new ItemAdater(getContext(), this, ItemAdater.NOTIFICATION);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        notificationsRV.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationsRV.setAdapter(itemApadter);
        if(Utils.getToken(getActivity()).isEmpty()){
            mListener.openloginPage();
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        githubViewModel.getValidToken().observe(getActivity(), tokenExpiryObserver);
        githubViewModel.getUserProfile().observe(getActivity(), userProfileObserver);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        if(githubViewModel.getNotifications().hasObservers()){
            githubViewModel.getNotifications().removeObserver(githubNotificationVOObserver);
        }
        githubViewModel.getValidToken().removeObserver(tokenExpiryObserver);
        githubViewModel.getUserProfile().removeObserver(userProfileObserver);

    }

    @Override
    public void onRepoSelected(int id) {

    }

    @Override
    public void onUserSelected(String username) {

    }

}
