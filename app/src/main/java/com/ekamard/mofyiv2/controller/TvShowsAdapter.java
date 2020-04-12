package com.ekamard.mofyiv2.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ekamard.mofyiv2.R;
import com.ekamard.mofyiv2.dataset.TvShows;

import java.util.ArrayList;

public class TvShowsAdapter extends RecyclerView.Adapter<TvShowsAdapter.ViewHolderTvShows> {

    private ArrayList<TvShows> listTvShows = new ArrayList<>();
    private OnItemClickCallback onClick;

    public void setOnClick(OnItemClickCallback onClick) {
        this.onClick = onClick;
    }

    public void setTvShowData(ArrayList<TvShows> itemTvShows) {
        listTvShows.clear();
        listTvShows.addAll(itemTvShows);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolderTvShows onCreateViewHolder(@NonNull ViewGroup parent, int pos) {
        View tvShowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_upcoming_tvshow, parent, false);
        return new ViewHolderTvShows(tvShowView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderTvShows holder, final int pos) {
        holder.bind(listTvShows.get(pos));

        TvShows myTvShows = listTvShows.get(pos);
        Glide.with(holder.itemView.getContext())
                .load(myTvShows.getImgPoster())
                .into(holder.tvPhoto);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onItemClicked(listTvShows.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listTvShows.size();
    }

    public interface OnItemClickCallback {
        void onItemClicked(TvShows data);
    }

    static class ViewHolderTvShows extends RecyclerView.ViewHolder {

        TextView tvTitle, tvRating;
        ImageView tvPhoto;

        ViewHolderTvShows(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_tv_title);
            tvRating = itemView.findViewById(R.id.tv_tv_rating);
            tvPhoto = itemView.findViewById(R.id.img_tv_upcoming);

        }

        void bind(TvShows tvShows) {
            tvTitle.setText(tvShows.getTitle());
            tvRating.setText(tvShows.getRating());
        }
    }
}
