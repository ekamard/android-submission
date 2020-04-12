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
import com.ekamard.mofyiv2.dataset.Actor;

import java.util.ArrayList;

public class ActorAdaptor extends RecyclerView.Adapter<ActorAdaptor.ActorViewHolder> {

    private ArrayList<Actor> listActor = new ArrayList<>();

    public void setActorData(ArrayList<Actor> itemActor) {
        listActor.clear();
        listActor.addAll(itemActor);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ActorViewHolder onCreateViewHolder(@NonNull ViewGroup actor, int pos) {
        View actorsView = LayoutInflater.from(actor.getContext()).inflate(R.layout.item_actors_popular, actor, false);
        return new ActorViewHolder(actorsView);
    }

    @Override
    public void onBindViewHolder(@NonNull ActorViewHolder holder, int pos) {
        holder.bind(listActor.get(pos));
        Actor ourActor = listActor.get(pos);

        Glide.with(holder.itemView.getContext())
                .load(ourActor.getProfileImage())
                .apply(new RequestOptions().override(400, 400))
                .into(holder.profileImg);
    }


    @Override
    public int getItemCount() {
        return listActor.size();
    }

    static class ActorViewHolder extends RecyclerView.ViewHolder {

        ImageView profileImg;
        TextView name;

        ActorViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImg = itemView.findViewById(R.id.img_actor);
            name = itemView.findViewById(R.id.tv_actor_name);

        }

        void bind(Actor actor) {
            name.setText(actor.getName());
        }
    }
}
