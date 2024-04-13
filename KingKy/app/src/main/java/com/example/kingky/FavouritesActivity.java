package com.example.kingky;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class FavouritesActivity extends AppCompatActivity implements RecyclerViewInterface, FavouriteAdapter.FavouriteAdapterListener {

    boolean isLandscape = false;
    private ArrayList<Wives> favouriteList = new ArrayList<>();
    private RecyclerView favouriteRecyclerView;
    private LinearLayout emptyFavourite;

    private Button clearFavouritesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        this.favouriteList = FavouritesManager.getInstance().getFavouritesList();

        clearFavouritesButton = findViewById(R.id.clearFavouritesButton);
        clearFavouritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClearFavourites();
            }
        });

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.favourites);

        // Perform item selected listener
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                } else if (item.getItemId() == R.id.browse) {
                    startActivity(new Intent(getApplicationContext(), BrowseActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                } else if (item.getItemId() == R.id.calendar) {
                    startActivity(new Intent(getApplicationContext(), CalendarActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                } else if (item.getItemId() == R.id.favourites) {
                    return true;
                }
                return false;
            }
        });

        setUpVisibility();
        setUpRecyclerView();
    }

    private void setUpVisibility() {
        emptyFavourite = findViewById(R.id.EmptyFavourites);
        clearFavouritesButton = findViewById(R.id.clearFavouritesButton);

        emptyFavourite.setVisibility(View.VISIBLE);
        clearFavouritesButton.setVisibility(View.GONE);

        if (favouriteList.size() != 0) {
            emptyFavourite.setVisibility(View.GONE);
            clearFavouritesButton.setVisibility(View.VISIBLE);
        }
    }

    private void setUpRecyclerView() {
        favouriteRecyclerView = findViewById(R.id.FavouritesRecyclerView);

        // Update the isFavourite field of each item in the favouriteList
        for (int i = 0; i < favouriteList.size(); i++) {
            Wives wives = favouriteList.get(i);
            if (isItemInFavouriteList(wives)) {
                wives.setFavourite(true);
            } else {
                wives.setFavourite(false);
            }
        }

        FavouriteAdapter adapter = new FavouriteAdapter(this.favouriteList, this, this);
        favouriteRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        favouriteRecyclerView.setAdapter(adapter);
    }

    private boolean isItemInFavouriteList(Wives item) {
        // Implement the logic to check if the item is in the favourite list
        for (Wives favouriteItem : favouriteList) {
            if (favouriteItem.getName().equals(item.getName())) {
                return true;
            }
        }
        return false;
    }

//    @Override
//    public void onItemClick(int position) {
//        // Handle item click if needed
//        Intent intent = new Intent(FavouritesActivity.this, DetailActivity.class);
//
//        intent.putExtra("Name", this.favouriteList.get(position).getName());
//        intent.putExtra("Category", this.favouriteList.get(position).getCategory());
//        intent.putExtra("Price", String.format("$" + this.favouriteList.get(position).getPrice()));
//        intent.putExtra("Description", this.favouriteList.get(position).getDescription());
//        intent.putExtra("currentItem", this.favouriteList.get(position));
//        // putting imageList
//        ArrayList<Integer> imageList = new ArrayList<Integer>();
//        for(int i = 0; i<3; i++){
//            imageList.add(this.favouriteList.get(position).getImageList()[i]);
//        }
//        intent.putIntegerArrayListExtra("ImageList", imageList);
//        startActivity(intent);
//    }

    @Override
    public void onWifeClick(int position) {

    }

    @Override
    public void onFavouriteClick(int position) {
    }

    @Override
    public void onRecentSearchClick(int position) {
    }

    @Override
    public void onRecentSearchRemoveClick(int position) {
    }

    @Override
    public void onFavouriteRemoveClick(int position) {
        Wives clickedItem = favouriteList.get(position);
        FavouritesManager.getInstance().removeFromFavourites(clickedItem);
        favouriteList.remove(clickedItem);
        favouriteRecyclerView.getAdapter().notifyItemRemoved(position);
        setUpVisibility();
    }

    @Override
    public void onItemClick(int position) {

    }

    private void onClearFavourites() {
        FavouritesManager.getInstance().clearFavourites();
        favouriteList.clear();
        favouriteRecyclerView.getAdapter().notifyDataSetChanged();
        setUpVisibility();
    }
}