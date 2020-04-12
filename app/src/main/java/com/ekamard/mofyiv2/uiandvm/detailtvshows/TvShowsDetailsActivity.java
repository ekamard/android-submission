package com.ekamard.mofyiv2.uiandvm.detailtvshows;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ekamard.mofyiv2.R;
import com.ekamard.mofyiv2.controller.ActorAdaptor;
import com.ekamard.mofyiv2.controller.GenresAdaptor;
import com.ekamard.mofyiv2.dataset.Actor;
import com.ekamard.mofyiv2.dataset.Genres;
import com.ekamard.mofyiv2.dataset.TvShows;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Locale;

public class TvShowsDetailsActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    public static final String TVSHOW_ID = "123";
    ImageView imgPoster, imgBackDrop;
    private TvShowsDetailsViewModel tvShowsDetailsViewModel;
    private GenresAdaptor genresAdaptor;
    private ActorAdaptor actorAdaptor;
    private ProgressBar myProgressBar;
    private TextView tvTitle, tvSynopsis, tvRating, tvStatus, tvdate, tvOriginLanguage, tvSeason, tvEpisode, tvNextEpisode;
    private FloatingActionButton btnPlay, btnLove;
    private int maxScrollingSize;
    private boolean isImageHidden;
    private String myLanguage;

    public TvShowsDetailsActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_shows_details);

        AppBarLayout myAppBar = findViewById(R.id.myAppBar);
        myAppBar.addOnOffsetChangedListener(this);

        Toolbar detailTvShowToolbar = findViewById(R.id.detail_tvshow);
        detailTvShowToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        detailTvShowToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RatingBar votesHere = findViewById(R.id.star);
        votesHere.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(TvShowsDetailsActivity.this, R.string.voting_greeting, Toast.LENGTH_SHORT).show();
            }
        });

        btnLove = findViewById(R.id.btn_loved);
        btnPlay = findViewById(R.id.btn_play_trailer);

        myLanguage = Locale.getDefault().toString();
        if (myLanguage.equals("in_ID")) {
            myLanguage = "id";
        }

        tvTitle = findViewById(R.id.tv_tv_title);
        tvSynopsis = findViewById(R.id.tv_tv_synopsis);
        tvRating = findViewById(R.id.tv_tv_rating);
        tvStatus = findViewById(R.id.tv_tv_status);
        tvdate = findViewById(R.id.tv_tv_date_released);
        tvOriginLanguage = findViewById(R.id.tv_tv_origin_language);
        tvSeason = findViewById(R.id.tv_tv_total_season);
        tvEpisode = findViewById(R.id.tv_tv_total_episode);
        tvNextEpisode = findViewById(R.id.tv_tv_next_released);
        imgPoster = findViewById(R.id.img_tv_upcoming);
        imgBackDrop = findViewById(R.id.img_backdrop_tv_upcoming);
        myProgressBar = findViewById(R.id.progresBar);

        RecyclerView genresRecycler = findViewById(R.id.rv_genres);
        genresRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        genresRecycler.setHasFixedSize(true);
        genresAdaptor = new GenresAdaptor();
        genresAdaptor.notifyDataSetChanged();
        genresRecycler.setAdapter(genresAdaptor);

        RecyclerView casterRecyler = findViewById(R.id.rv_actor);
        casterRecyler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        casterRecyler.setHasFixedSize(true);
        actorAdaptor = new ActorAdaptor();
        actorAdaptor.notifyDataSetChanged();
        casterRecyler.setAdapter(actorAdaptor);

        tvShowsDetailsViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(TvShowsDetailsViewModel.class);
        showTvShowsDetail();
    }

    private void showTvShowsDetail() {
        String tvShowsId = getIntent().getStringExtra(TVSHOW_ID);
        tvShowsDetailsViewModel.setTvShowsData(tvShowsId, myLanguage);
        tvShowsDetailsViewModel.setActorData(tvShowsId);
        tvShowsDetailsViewModel.setGenresData(tvShowsId, myLanguage);
        showLoading(true);
        observeConnection();
    }

    private void observeConnection() {
        tvShowsDetailsViewModel.getConnectionStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean state) {
                if (state) {
                    observeTvShowData();
                    observeGenresData();
                    observeCasterData();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.network_error_notification, Toast.LENGTH_SHORT).show();
                    showLoading(false);
                }
            }
        });
    }

    private void observeTvShowData() {
        tvShowsDetailsViewModel.getTvShowsData().observe(this, new Observer<TvShows>() {
            @Override
            public void onChanged(TvShows tvShows) {
                tvTitle.setText(tvShows.getTitle());
                tvSynopsis.setText(tvShows.getSynopsis());
                tvRating.setText(tvShows.getRating());
                tvStatus.setText(tvShows.getStatus());
                tvdate.setText(tvShows.getReleaseInfo());
                tvOriginLanguage.setText(tvShows.getLanguage());
                tvSeason.setText(tvShows.getSeasons());
                tvEpisode.setText(tvShows.getEpisode());
                tvNextEpisode.setText(tvShows.getNextEpisode());

                Glide.with(getApplicationContext())
                        .load(tvShows.getImgPoster())
                        .into(imgPoster);
                Glide.with(getApplicationContext())
                        .load(tvShows.getImgBackdrop())
                        .into(imgBackDrop);
                showLoading(false);
            }
        });
    }

    private void observeGenresData() {
        tvShowsDetailsViewModel.getGenresData().observe(this, new Observer<ArrayList<Genres>>() {
            @Override
            public void onChanged(ArrayList<Genres> genres) {
                genresAdaptor.setListOfGenres(genres);
                showLoading(false);
            }
        });
    }

    private void observeCasterData() {
        tvShowsDetailsViewModel.getActorData().observe(this, new Observer<ArrayList<Actor>>() {
            @Override
            public void onChanged(ArrayList<Actor> actors) {
                actorAdaptor.setActorData(actors);
                showLoading(false);
            }
        });
    }

    private void showLoading(Boolean state) {
        if (state) {
            myProgressBar.setVisibility(View.VISIBLE);
        } else {
            myProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (maxScrollingSize == 0)
            maxScrollingSize = appBarLayout.getTotalScrollRange();
        int currentScrollingSize = (Math.abs(verticalOffset)) * 100 / maxScrollingSize;

        int CHANCE_SHOW_IMAGE = 30;
        if (currentScrollingSize >= CHANCE_SHOW_IMAGE) {
            if (!isImageHidden) {
                isImageHidden = true;
                ViewCompat.animate(btnLove).scaleY(0).scaleX(0).start();
                ViewCompat.animate(btnPlay).scaleY(0).scaleX(0).start();
            }
        }
        if (currentScrollingSize < CHANCE_SHOW_IMAGE) {
            if (isImageHidden) {
                isImageHidden = false;
                ViewCompat.animate(btnLove).scaleX(1).scaleY(1).start();
                ViewCompat.animate(btnPlay).scaleX(1).scaleY(1).start();
            }
        }
    }
}


