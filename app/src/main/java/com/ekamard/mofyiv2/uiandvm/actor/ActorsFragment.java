package com.ekamard.mofyiv2.uiandvm.actor;

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
import com.ekamard.mofyiv2.controller.ActorAdaptor;
import com.ekamard.mofyiv2.dataset.Actor;

import java.util.ArrayList;
import java.util.Objects;

public class ActorsFragment extends Fragment {

    private ActorsViewModel actorsViewModel;
    private SwipeRefreshLayout myRefresh;
    private View actorsFragment;
    private ActorAdaptor actorAdapter;
    private ProgressBar myProgressBar;

    public ActorsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (actorsFragment == null) {
            actorsFragment = inflater.inflate(R.layout.actors_fragment2, container, false);
        }
        return actorsFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actorsViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(ActorsViewModel.class);
        showPopularActor();
        refeshData();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myProgressBar = view.findViewById(R.id.progresBar);
        myRefresh = view.findViewById(R.id.refresh_layout);

        RecyclerView actorRecycler = view.findViewById(R.id.rv_actor);
        actorRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        actorRecycler.setHasFixedSize(true);

        actorAdapter = new ActorAdaptor();
        actorAdapter.notifyDataSetChanged();
        actorRecycler.setAdapter(actorAdapter);

    }

    private void showLoading(Boolean state) {
        if (state) {
            myProgressBar.setVisibility(View.VISIBLE);
        } else {
            myProgressBar.setVisibility(View.GONE);
        }
    }

    private void showPopularActor() {
        actorsViewModel.setActorData();
        showLoading(true);
        observeConnection();
    }

    private void refeshData() {
        myRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showPopularActor();
                myRefresh.setRefreshing(false);
            }
        });
    }

    private void observeActorData() {
        actorsViewModel.getActorData().observe(Objects.requireNonNull(getActivity()), new Observer<ArrayList<Actor>>() {
            @Override
            public void onChanged(ArrayList<Actor> actors) {
                if (actors != null) {
                    actorAdapter.setActorData(actors);
                    showLoading(false);
                }
            }
        });
    }

    private void observeConnection() {
        actorsViewModel.getConnectionStatus().observe(Objects.requireNonNull(getActivity()), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean state) {
                if (state) {
                    observeActorData();
                } else {
                    Toast.makeText(getContext(), R.string.network_error_notification, Toast.LENGTH_SHORT).show();
                    showLoading(false);
                }
            }
        });
    }

}
