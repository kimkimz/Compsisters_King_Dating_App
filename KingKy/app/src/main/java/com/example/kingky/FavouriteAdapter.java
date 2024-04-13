package com.example.kingky;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder> {
    private List<Wives> favouriteItems;
    private Context context;
    private FavouriteAdapterListener listener;

    public interface FavouriteAdapterListener {
        void onFavouriteClick(int position);
        void onFavouriteRemoveClick(int position);
        void onItemClick(int position);
    }

    public FavouriteAdapter(List<Wives> favouriteItems, Context context, FavouriteAdapterListener listener) {
        this.favouriteItems = favouriteItems;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wife_view, parent, false);
        return new FavouriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteViewHolder holder, int position) {
        Wives favouriteItem = favouriteItems.get(holder.getAdapterPosition());

        holder.image.setImageResource(favouriteItem.getImage());
        holder.name.setText(favouriteItem.getName());
        holder.age.setText("Age:" + String.valueOf(favouriteItem.getAge()));

        if (favouriteItems.get(position).isFavourite()) {
            holder.favouriteButton.setVisibility(View.GONE);
            holder.favouriteButtonFilled.setVisibility(View.VISIBLE);
        } else {
            holder.favouriteButton.setVisibility(View.VISIBLE);
            holder.favouriteButtonFilled.setVisibility(View.GONE);
        }

        holder.favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onFavouriteClick(holder.getAdapterPosition());
                }
            }
        });

        holder.favouriteButtonFilled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onFavouriteRemoveClick(holder.getAdapterPosition());
                }
            }
        });

        // onClickListener for itemView
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return favouriteItems.size();
    }

    public class FavouriteViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView age;
        ImageButton favouriteButton;
        ImageButton favouriteButtonFilled;

        public FavouriteViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.wives_images);
            name = itemView.findViewById(R.id.wives_names);
            age = itemView.findViewById(R.id.wives_ages);
            favouriteButton = itemView.findViewById(R.id.favouriteButton);
            favouriteButtonFilled = itemView.findViewById(R.id.favouriteButtonFilled);
        }
    }
}
