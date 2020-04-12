package com.ekamard.mofyiv2.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ekamard.mofyiv2.R;
import com.ekamard.mofyiv2.dataset.Genres;

import java.util.ArrayList;

public class GenresAdaptor extends RecyclerView.Adapter<GenresAdaptor.GenresViewHolder> {

    private ArrayList<Genres> listOfGenres = new ArrayList<>();

    public void setListOfGenres(ArrayList<Genres> itemGenre) {
        listOfGenres.clear();
        listOfGenres.addAll(itemGenre);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GenresViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View genresView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genres, parent, false);
        return new GenresViewHolder(genresView);
    }

    @Override
    public void onBindViewHolder(@NonNull GenresViewHolder holder, int position) {
        holder.bind(listOfGenres.get(position));
    }

    @Override
    public int getItemCount() {
        return listOfGenres.size();
    }

    static class GenresViewHolder extends RecyclerView.ViewHolder {

        TextView tvGenres;

        GenresViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGenres = itemView.findViewById(R.id.genre_name);
        }

        void bind(Genres genres) {
            tvGenres.setText(genres.getName());
        }
    }
}
