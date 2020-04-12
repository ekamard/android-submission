package com.ekamard.mofyiv2.uiandvm.home;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ekamard.mofyiv2.R;
import com.ekamard.mofyiv2.controller.MoviesAdaptor;
import com.ekamard.mofyiv2.controller.TvShowsAdapter;
import com.ekamard.mofyiv2.dataset.Movies;
import com.ekamard.mofyiv2.dataset.TvShows;
import com.ekamard.mofyiv2.uiandvm.detailmovies.MoviesDetailsActivity;
import com.ekamard.mofyiv2.uiandvm.detailtvshows.TvShowsDetailsActivity;

import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private HomeViewModel myViewModel;
    private MoviesAdaptor myMoviesAdapter;
    private TvShowsAdapter myTvShowAdapter;
    private ProgressBar myProgressBar, myProgressBar2;
    private View homeFragmentView;
    private SwipeRefreshLayout myRefreshLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (homeFragmentView == null) {

            homeFragmentView = inflater.inflate(R.layout.home_fragment, container, false);

        }
        return homeFragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView myMoviesRecycle = view.findViewById(R.id.rv_upcoming_movies);
        myMoviesRecycle.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        myMoviesRecycle.setHasFixedSize(true);
        myMoviesAdapter = new MoviesAdaptor();
        myMoviesAdapter.notifyDataSetChanged();
        myMoviesRecycle.setAdapter(myMoviesAdapter);

        RecyclerView myTvShowRecycle = view.findViewById(R.id.rv_upcoming_tvShows);
        myTvShowRecycle.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        myTvShowRecycle.setHasFixedSize(true);
        myTvShowAdapter = new TvShowsAdapter();
        myTvShowAdapter.notifyDataSetChanged();
        myTvShowRecycle.setAdapter(myTvShowAdapter);

        myProgressBar = view.findViewById(R.id.progresBar);
        myProgressBar2 = view.findViewById(R.id.progresBar2);
        myRefreshLayout = view.findViewById(R.id.refresh_layout);

        myViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(HomeViewModel.class);
        showUpcomingMovies();
        showUpcomingTvShow();
        refreshData();
        showDetails();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void showLoading(Boolean state) {
        if (state) {
            myProgressBar.setVisibility(View.VISIBLE);
            myProgressBar2.setVisibility(View.VISIBLE);
        } else {
            myProgressBar.setVisibility(View.GONE);
            myProgressBar2.setVisibility(View.GONE);
        }
    }

    private void showUpcomingMovies() {
        myViewModel.setUpcomingMovies();
        showLoading(true);
        observeConnection();
    }

    private void showUpcomingTvShow() {
        myViewModel.setUpcomingTvShow();
        showLoading(true);
        observeConnection();
    }

    private void refreshData() {
        myRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showUpcomingMovies();
                showUpcomingTvShow();
                myRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void observeMoviesData() {
        myViewModel.getMovies().observe(Objects.requireNonNull(getActivity()), new Observer<ArrayList<Movies>>() {
            @Override
            public void onChanged(ArrayList<Movies> movies) {
                if (movies != null) {
                    myMoviesAdapter.setMoviesData(movies);
                    showLoading(false);
                }
            }
        });
    }

    private void observeTvShowData() {
        myViewModel.getTvshow().observe(Objects.requireNonNull(getActivity()), new Observer<ArrayList<TvShows>>() {
            @Override
            public void onChanged(ArrayList<TvShows> tvShows) {
                if (tvShows != null) {
                    myTvShowAdapter.setTvShowData(tvShows);
                    showLoading(false);
                }
            }
        });
    }

    private void observeConnection() {
        myViewModel.getConnectionStatus().observe(Objects.requireNonNull(getActivity()), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean state) {
                if (state) {
                    observeMoviesData();
                    observeTvShowData();
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

        myTvShowAdapter.setOnClick(new TvShowsAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(TvShows data) {
                chooesTvShows(data);
            }
        });
    }

    private void chooseMovies(Movies movies) {
        Intent toDetailsMovies = new Intent(getActivity(), MoviesDetailsActivity.class);
        toDetailsMovies.putExtra(MoviesDetailsActivity.MOVIES_ID, movies.getMoviesId());
        startActivity(toDetailsMovies);
    }

    private void chooesTvShows(TvShows tvShows) {
        Intent toDetailsTvShows = new Intent(getActivity(), TvShowsDetailsActivity.class);
        toDetailsTvShows.putExtra(TvShowsDetailsActivity.TVSHOW_ID, tvShows.getTvShowsId());
        startActivity(toDetailsTvShows);
    }
}
