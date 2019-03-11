package com.example.bhanu.github.repos.ui;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.bhanu.github.R;
import com.example.bhanu.github.repos.viewmodels.GithubViewModel;
import com.example.bhanu.github.repos.datamodel.Filter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFilterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFilterFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private GithubViewModel githubViewModel;
    private Observer<Filter> filterObserver;


    @BindView(R.id.pblic)
    CheckBox publicCB;

    @BindView(R.id.prvate)
    CheckBox privateCB;

    @BindView(R.id.button3)
    Button doneBtn;
    private CompoundButton.OnCheckedChangeListener privacyChangeListner;

    public SearchFilterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFilterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFilterFragment newInstance(String param1, String param2) {
        SearchFilterFragment fragment = new SearchFilterFragment();
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

        filterObserver = (Filter filtet) ->{
            if(filtet.getPrivacy() == Filter.PRIVACY.BOTH){
                privateCB.setChecked(true);
                publicCB.setChecked(true);
            }
            else if(filtet.getPrivacy() == Filter.PRIVACY.PUBLIC){
                privateCB.setChecked(false);
                publicCB.setChecked(true);
            }
            else if(filtet.getPrivacy() == Filter.PRIVACY.PRIVATE){
                privateCB.setChecked(true);
                publicCB.setChecked(false);
            }
        };

        privacyChangeListner = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Filter f = githubViewModel.getFilterMutableLiveData().getValue();
                if(!publicCB.isChecked() && privateCB.isChecked()){
                    f.setPrivacy(Filter.PRIVACY.PRIVATE);
                }
                else if(publicCB.isChecked() && !privateCB.isChecked()){
                    f.setPrivacy(Filter.PRIVACY.PUBLIC);
                }
                else {
                    f.setPrivacy(Filter.PRIVACY.BOTH);
                }
                githubViewModel.getFilterMutableLiveData().postValue(f);
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank2, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        publicCB.setOnCheckedChangeListener(privacyChangeListner);
        privateCB.setOnCheckedChangeListener(privacyChangeListner);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchFilterFragment.this.getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        githubViewModel.getFilterMutableLiveData().observe(this, filterObserver);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        githubViewModel.getFilterMutableLiveData().removeObserver(filterObserver);
    }
}
