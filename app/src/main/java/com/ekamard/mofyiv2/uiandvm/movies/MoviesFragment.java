package com.ekamard.mofyiv2.uiandvm.movies;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ekamard.mofyiv2.R;
import com.ekamard.mofyiv2.controller.MoviesAdaptor;
import com.ekamard.mofyiv2.dataset.Movies;
import com.ekamard.mofyiv2.uiandvm.detailmovies.MoviesDetailsActivity;

import java.util.ArrayList;
import java.util.Locale;

public class MoviesFragment extends Fragment {

    private MoviesViewModel moviesViewModel;
    private ProgressBar myProgressBar;
    private MoviesAdaptor myMoviesAdapter;
    private View moviesFragment;
    private SwipeRefreshLayout myRefresh;
    private String myLanguage;


    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (moviesFragment == null) {

            moviesFragment = getLayoutInflater().inflate(R.layout.movies_fragment, container, false);

        }
        return moviesFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        moviesViewModel = new ViewModelProvider(requireActivity(), new ViewModelProvider.NewInstanceFactory()).get(MoviesViewModel.class);
        showUpcomingMovies();
        refreshData();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myProgressBar = view.findViewById(R.id.progresBar);
        myRefresh = view.findViewById(R.id.refresh_layout);
        myLanguage = Locale.getDefault().toString();

        RecyclerView moviesRecycler = view.findViewById(R.id.rv_upcoming_movies);
        moviesRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        moviesRecycler.setHasFixedSize(true);

        myMoviesAdapter = new MoviesAdaptor();
        myMoviesAdapter.notifyDataSetChanged();
        moviesRecycler.setAdapter(myMoviesAdapter);

        showDetails();

    }

    private void showLoading(Boolean state) {
        if (state) {
            myProgressBar.setVisibility(View.VISIBLE);
        } else {
            myProgressBar.setVisibility(View.GONE);
        }
    }

    private void showUpcomingMovies() {

        moviesViewModel.setMoviesData(myLanguage);
        showLoading(true);
        observeConnection();

    }

    private void refreshData() {
        myRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showUpcomingMovies();
                myRefresh.setRefreshing(false);
            }
        });
    }


    private void observeMoviesData() {
        moviesViewModel.getMoviesData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Movies>>() {
            @Override
            public void onChanged(ArrayList<Movies> movies) {
                myMoviesAdapter.setMoviesData(movies);
                showLoading(false);
            }
        });
    }


    private void observeConnection() {
        moviesViewModel.getConnectionStatus().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean state) {
                if (state) {
                    observeMoviesData();
                } else {
                    Toast.makeText(getContext(), R.string.network_error_notification, Toast.LENGTH_SHORT).show();
                    showLoading(false);
                }
            }
        });
    }

    private void showDetails() {
        myMoviesAdapter.setOnClick(new MoviesAdaptor.OnItemClickCallback() {
            @Override
            public void onItemCLicked(Movies data) {
                chooseMovies(data);
            }
        });
    }

    private void chooseMovies(Movies movies) {
        Intent toDetails = new Intent(getActivity(), MoviesDetailsActivity.class);
        toDetails.putExtra(MoviesDetailsActivity.MOVIES_ID, movies.getMoviesId());
        startActivity(toDetails);
    }

}
