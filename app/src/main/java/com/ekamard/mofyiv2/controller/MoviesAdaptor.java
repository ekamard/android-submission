package com.ekamard.mofyiv2.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ekamard.mofyiv2.R;
import com.ekamard.mofyiv2.dataset.Movies;

import java.util.ArrayList;

public class MoviesAdaptor extends RecyclerView.Adapter<MoviesAdaptor.MoviesViewHolder> {

    private ArrayList<Movies> moviesList = new ArrayList<>();
    private OnItemClickCallback onClick;

    public void setOnClick(OnItemClickCallback onClick) {
        this.onClick = onClick;
    }

    public void setMoviesData(ArrayList<Movies> moviesItems) {
        moviesList.clear();
        moviesList.addAll(moviesItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MoviesAdaptor.MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int pos) {
        View moviesView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_upcoming_movies, parent, false);
        return new MoviesViewHolder(moviesView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MoviesAdaptor.MoviesViewHolder holder, int position) {
        holder.bind(moviesList.get(position));

        Movies myMovies = moviesList.get(position);
        Glide.with(holder.itemView.getContext())
                .load(myMovies.getImgPoster())
                .apply(new RequestOptions().override(200, 400))
                .into(holder.moviesPhotos);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onItemCLicked(moviesList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public interface OnItemClickCallback {
        void onItemCLicked(Movies data);
    }

    static class MoviesViewHolder extends RecyclerView.ViewHolder {

        TextView moviesTitle, moviesRating;
        ImageView moviesPhotos, moviesBackdrop;

        MoviesViewHolder(@NonNull View itemView) {
            super(itemView);
            moviesTitle = itemView.findViewById(R.id.tv_mv_title);
            moviesRating = itemView.findViewById(R.id.tv_mv_rating);
            moviesPhotos = itemView.findViewById(R.id.img_mv_upcoming);
            moviesBackdrop = itemView.findViewById(R.id.img_backdrop_mv_upcoming);
        }

        void bind(Movies movies) {
            moviesTitle.setText(movies.getTitle());
            moviesRating.setText(movies.getRating());
        }
    }

}
