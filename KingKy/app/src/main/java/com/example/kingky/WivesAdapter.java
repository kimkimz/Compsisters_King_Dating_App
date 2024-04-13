package com.example.kingky;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WivesAdapter extends RecyclerView.Adapter<WivesAdapter.ItemViewHolder> {
    private final List<Wives> wivesList;
    private final RecyclerViewInterface recyclerViewInterface;

    // Creating constructor for ItemAdapter
    private Wives wives;

    //Creating constructor for ItemAdapter
    public WivesAdapter(List<Wives> wivesList, RecyclerViewInterface recyclerViewInterface) {
        this.wivesList = wivesList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    // Method for creating ViewHolder
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.wife_view, parent, false);
        return new ItemViewHolder(view);
    }

    // Method for binding data to the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Wives item = wivesList.get(position);
        holder.nameView.setText(item.getName());
        holder.ageView.setText("Age:" + String.valueOf(item.getAge()));
        holder.imageView.setImageResource(item.getImage());

        // Update the visibility of the favorite button based on the item's status
//        if (FavouritesManager.getInstance().isItemInFavourites(item)) {
//            holder.favouriteButton.setVisibility(View.GONE);
//            holder.favouriteButtonFilled.setVisibility(View.VISIBLE);
//        } else {
//            holder.favouriteButton.setVisibility(View.VISIBLE);
//            holder.favouriteButtonFilled.setVisibility(View.GONE);
//        }
    }

    // Returning the number of items in the itemList
    @Override
    public int getItemCount() {
        return wivesList.size();
    }

    //Returning the ViewType for displaying each food in the list in the layout file
    @Override
    public int getItemViewType(final int position) {
        return R.layout.wife_view;
    }

    // Inner Class Declaration of RecyclerView.ViewHolder
    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameView, ageView;
        ImageButton favouriteButton, favouriteButtonFilled;

        // The constructor for setting up the ItemViewLayout
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.wives_images);
            nameView = itemView.findViewById(R.id.wives_names);
            ageView = itemView.findViewById(R.id.wives_ages);
            favouriteButton = itemView.findViewById(R.id.favouriteButton);
            favouriteButtonFilled = itemView.findViewById(R.id.favouriteButtonFilled);

            // onClickListener for entire Item
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onWifeClick(position);
                        }
                    }
                }
            });

            // onClickListener for Favorite Button
            favouriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onFavouriteClick(position);
                            favouriteButton.setVisibility(View.GONE);
                            favouriteButtonFilled.setVisibility(View.VISIBLE);
                            Toast.makeText(itemView.getContext(), "Item added to favourites", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

            // onClickListener for Filled Favorite Button
            favouriteButtonFilled.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onFavouriteClick(position);
                            favouriteButton.setVisibility(View.VISIBLE);
                            favouriteButtonFilled.setVisibility(View.GONE);
                            Toast.makeText(itemView.getContext(), "Item removed from favourites", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }
}