package com.example.bhanu.github;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;


import com.example.bhanu.github.network.GithubAuthenticationService;
import com.example.bhanu.github.network.model.Token;
import com.example.bhanu.github.network.WebService;
import com.example.bhanu.github.repos.GithubViewModel;
import com.example.bhanu.github.repos.ui.NotificationFragment;
import com.example.bhanu.github.repos.ui.ReposFragment;

import androidx.navigation.fragment.NavHostFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener,
        ReposFragment.OnFragmentInteractionListener, NotificationFragment.OnFragmentInteractionListener {

    private static final String CODE = "code";
    private static final String ERROR_CODE = "error";
    private static final String STATE = "state" ;
    private WebService webService;
    private GithubAuthenticationService githubAuthenticationService;
    private NavHostFragment navHostFragment;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webService = new WebService(getApplication());
        githubAuthenticationService = webService.getGithubAuthenticationClient().create(GithubAuthenticationService.class);
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.my_nav_host_fragment);
        dialog = new ProgressDialog(this);

        Uri data = getIntent().getData();
        if (data != null && !TextUtils.isEmpty(data.getScheme())) {
            if (Constants.REDIRECT_URI_ROOT.equals(data.getScheme())) {
                String code = data.getQueryParameter(CODE);
                String error = data.getQueryParameter(ERROR_CODE);
                String state = data.getQueryParameter(STATE);

                if(!Utils.getState(this).equals(state)) {
                    finish();
                }

                if (!TextUtils.isEmpty(code)) {
                    getTokenFormUrl(code, state);
                }
                if(!TextUtils.isEmpty(error)) {
                    finish();
                }
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.notification:
                navHostFragment.getNavController().navigate(R.id.notificationFragment);

        }
        return(super.onOptionsItemSelected(item));
    }

    private void getTokenFormUrl(String code, String state) {
        dialog.show();
        githubAuthenticationService.savePost(Constants.GITHUB_CLIENT_ID,
                Constants.GITHUB_CLIENT_SECRET,
                code,
                Constants.REDIRECT_URL_CALLBACK,
                state
        ).enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                dialog.dismiss();
                Utils.saveToken(getApplicationContext(), response.body());
                ViewModelProviders.of(MainActivity.this).get(GithubViewModel.class).getValidToken().postValue(true);
                navHostFragment.getNavController().navigate(R.id.action_loginFragment2_to_reposFragment);

            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                t.getCause();
                dialog.dismiss();
            }
        });

    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void openloginPage() {
        navHostFragment.getNavController().navigate(R.id.loginFragment2);
    }

    @Override
    public void openRepoPage() {
        navHostFragment.getNavController().navigate(R.id.reposFragment);

    }
}
