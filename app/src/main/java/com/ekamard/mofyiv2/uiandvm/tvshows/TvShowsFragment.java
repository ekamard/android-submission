package com.ekamard.mofyiv2.uiandvm.tvshows;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.ekamard.mofyiv2.R;
import com.ekamard.mofyiv2.controller.TvShowsAdapter;
import com.ekamard.mofyiv2.controller.ViewPagerAdapter;
import com.ekamard.mofyiv2.dataset.TvShows;
import com.ekamard.mofyiv2.uiandvm.detailtvshows.TvShowsDetailsActivity;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class TvShowsFragment extends Fragment {

    private TvShowsViewModel tvShowsViewModel;
    private View tvShowFragment;
    private ProgressBar myProgressBar;
    private TvShowsAdapter tvShowAdapter;
    private SwipeRefreshLayout myRefresh;
    private String myLanguage;

    public TvShowsFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (tvShowFragment == null) {
            tvShowFragment = getLayoutInflater().inflate(R.layout.tv_shows_fragment, container, false);



        }
        return tvShowFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myProgressBar = view.findViewById(R.id.progresBar);

        myLanguage = Locale.getDefault().toString();

        ViewPagerAdapter myViewPagerAdapter = new ViewPagerAdapter(getContext());
        ViewPager myViewPager = view.findViewById(R.id.vp_toprated_tvshow);
        myViewPager.setAdapter(myViewPagerAdapter);

        RecyclerView tvShowRecycler = view.findViewById(R.id.rv_upcoming_tvShows);
        tvShowRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        tvShowRecycler.setHasFixedSize(true);

        tvShowAdapter = new TvShowsAdapter();
        tvShowAdapter.notifyDataSetChanged();

        tvShowRecycler.setAdapter(tvShowAdapter);

        myRefresh = view.findViewById(R.id.refresh_layout);

        tvShowsViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(TvShowsViewModel.class);
        showUpcomingTvShow();
        refreshData();

        showDetails();
    }

    private void showLoading(Boolean state) {
        if (state) {
            myProgressBar.setVisibility(View.VISIBLE);
        } else {
            myProgressBar.setVisibility(View.GONE);
        }
    }

    private void showUpcomingTvShow() {
        tvShowsViewModel.setUpComingTvShow(myLanguage);
        showLoading(true);
        observeConnection();
    }

    private void refreshData() {
        myRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showUpcomingTvShow();
                myRefresh.setRefreshing(false);
            }
        });
    }

    private void observeTvShowData() {
        tvShowsViewModel.getTvShowData().observe(Objects.requireNonNull(getActivity()), new Observer<ArrayList<TvShows>>() {
            @Override
            public void onChanged(ArrayList<TvShows> tvShows) {
                if (tvShows != null) {
                    tvShowAdapter.setTvShowData(tvShows);
                    showLoading(false);
                }
            }
        });
    }

    private void observeConnection() {
        tvShowsViewModel.getConnectionStatus().observe(Objects.requireNonNull(getActivity()), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean state) {
                if (state) {
                    observeTvShowData();
                } else {
                    Toast.makeText(getContext(), R.string.network_error_notification, Toast.LENGTH_SHORT).show();
                    showLoading(false);
                }
            }
        });
    }

    private void chooseTvShows(TvShows tvShows) {
        Intent toDetailsTvShow = new Intent(getActivity(), TvShowsDetailsActivity.class);
        toDetailsTvShow.putExtra(TvShowsDetailsActivity.TVSHOW_ID, tvShows.getTvShowsId());
        startActivity(toDetailsTvShow);
    }

    private void showDetails() {
        tvShowAdapter.setOnClick(new TvShowsAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(TvShows data) {
                chooseTvShows(data);
            }
        });
    }
}
