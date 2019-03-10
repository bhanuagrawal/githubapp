package com.example.bhanu.github;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import com.example.bhanu.github.repos.ui.OnFragmentInteractionListener;
import com.example.bhanu.github.repos.ui.RepoDetailFragmentDirections;

import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener{
    private NavHostFragment navHostFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.my_nav_host_fragment);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return(super.onOptionsItemSelected(item));
    }



    @Override
    public void openUserDetailPage(String username) {

        RepoDetailFragmentDirections.ActionRepoDetailFragmentToUserDetailFragment action = RepoDetailFragmentDirections.actionRepoDetailFragmentToUserDetailFragment(username);
        navHostFragment.getNavController().navigate(action);
    }

    @Override
    public void openPage(NavDirections action) {
        navHostFragment.getNavController().navigate(action);
    }
}
