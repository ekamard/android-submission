package com.ekamard.mofyiv2.uiandvm.detailmovies;

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
import com.ekamard.mofyiv2.dataset.Movies;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Locale;

public class MoviesDetailsActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    public static final String MOVIES_ID = "123";
    ImageView imgPoster, imgBackDrop;
    private MoviesDetailsViewModel detailMoviesModel;
    private GenresAdaptor genresAdaptor;
    private ActorAdaptor actorAdaptor;
    private ProgressBar myProgressBar;
    private TextView tvTitle, tvRuntime, tvSynopsis, tvRating, tvStatus, tvdate, tvOriginLanguage;
    private FloatingActionButton btnPlay, btnLove;
    private int maxScrollingSize;
    private boolean isImageHidden;
    private String myLanguage;

    public MoviesDetailsActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_details);

        AppBarLayout myAppBar = findViewById(R.id.myAppBar);
        myAppBar.addOnOffsetChangedListener(this);

        Toolbar detailsMoviesToolbar = findViewById(R.id.detail_movie);
        detailsMoviesToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        detailsMoviesToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RatingBar votesHere = findViewById(R.id.star);
        votesHere.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(MoviesDetailsActivity.this, R.string.voting_greeting, Toast.LENGTH_SHORT).show();
            }
        });

        btnLove = findViewById(R.id.btn_loved);
        btnPlay = findViewById(R.id.btn_play_trailer);

        tvTitle = findViewById(R.id.tv_mv_title);
        tvRuntime = findViewById(R.id.tv_mv_runtime);
        tvSynopsis = findViewById(R.id.tv_mv_synopsis);
        tvRating = findViewById(R.id.tv_mv_rating);
        tvStatus = findViewById(R.id.tv_mv_status);
        tvdate = findViewById(R.id.tv_mv_date_released);
        tvOriginLanguage = findViewById(R.id.tv_mv_origin_language);
        imgPoster = findViewById(R.id.img_mv_upcoming);
        imgBackDrop = findViewById(R.id.img_backdrop_mv_upcoming);
        myProgressBar = findViewById(R.id.progresBar);

        myLanguage = Locale.getDefault().toString();
        if (myLanguage.equals("in_ID")) {
            myLanguage = "id";
        }

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

        detailMoviesModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MoviesDetailsViewModel.class);
        showMoviesDetails();
    }

    private void observeMoviesData() {
        detailMoviesModel.getMoviesData().observe(this, new Observer<Movies>() {
            @Override
            public void onChanged(Movies movies) {
                tvTitle.setText(movies.getTitle());
                tvRuntime.setText(movies.getRuntime());
                tvSynopsis.setText(movies.getSynopsis());
                tvRating.setText(movies.getRating());
                tvStatus.setText(movies.getStatus());
                tvdate.setText(movies.getReleaseInfo());
                tvOriginLanguage.setText(movies.getLanguage());
                Glide.with(getApplicationContext())
                        .load(movies.getImgPoster())
                        .into(imgPoster);
                Glide.with(getApplicationContext())
                        .load(movies.getImgBackdrop())
                        .into(imgBackDrop);

                showLoading(false);
            }
        });
    }

    private void observeConnection() {
        detailMoviesModel.getConnectionStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean state) {
                if (state) {
                    observeMoviesData();
                    observeGenresData();
                    observeCasterData();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.network_error_notification, Toast.LENGTH_SHORT).show();
                    showLoading(false);
                }
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

    public void showMoviesDetails() {
        String moviesId = getIntent().getStringExtra(MOVIES_ID);
        detailMoviesModel.setMoviesData(moviesId, myLanguage);
        detailMoviesModel.setGenresData(moviesId, myLanguage);
        detailMoviesModel.setActorData(moviesId);
        showLoading(true);
        observeConnection();
    }

    private void observeGenresData() {
        detailMoviesModel.getGenresData().observe(this, new Observer<ArrayList<Genres>>() {
            @Override
            public void onChanged(ArrayList<Genres> genres) {
                genresAdaptor.setListOfGenres(genres);
                showLoading(false);
            }
        });
    }

    private void observeCasterData() {
        detailMoviesModel.getActorData().observe(this, new Observer<ArrayList<Actor>>() {
            @Override
            public void onChanged(ArrayList<Actor> actors) {
                actorAdaptor.setActorData(actors);
                showLoading(false);
            }
        });
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
