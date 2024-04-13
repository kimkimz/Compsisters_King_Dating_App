package com.example.kingky;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.ChangeClipBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.TransitionSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class BrowseActivity extends AppCompatActivity implements RecyclerViewInterface{
    boolean isLandscape = false;
    WivesAdapter adapter;
    private String selectedAgeFilter = null;
    private RecyclerView wivesRecyclerView;
    private ArrayList<Wives> wivesList;
    private ArrayList<Wives> filterWives;

    boolean toTwentyClick = false;
    boolean toTwentyFiveClick = false;
    boolean toThirtyClick = false;
    boolean thirtyPlusClick = false;

    // Declaring buttons and colors
    private Button toTwenty, toTwentyFive, toThirty, thirtyPlus;
    private ImageButton favouriteButton, favouriteButtonFilled;
    private ArrayList<Button> buttonList = new ArrayList<>();

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        searchView = findViewById(R.id.search_view);
        searchView.clearFocus();

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // Open the SearchActivity when the SearchView is clicked and gains focus
                if (hasFocus) {
                    Intent intent = new Intent(BrowseActivity.this, SearchActivity.class);
                    startActivity(intent);
                }
            }
        });

        // Set up an OnQueryTextListener for the SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Open the SearchActivity with the search query
                openSearchActivity(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle text change if needed
                return false;
            }
        });

        // Set up an OnClickListener for the SearchView to start the transition
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearchActivityTransition();
            }
        });

        // Retrieve selected filter from intent extras
        String selectedFilter = getIntent().getStringExtra("selectedAgeFilter");

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.browse);

        // Perform item selected listener
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                } else if (item.getItemId() == R.id.browse) {
                    return true;
                } else if (item.getItemId() == R.id.calendar) {
                    startActivity(new Intent(getApplicationContext(), CalendarActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                } else if (item.getItemId() == R.id.favourites) {
                    startActivity(new Intent(getApplicationContext(), FavouritesActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;
            }
        });

        // Initialise buttons and colors
        iniWidget();
        // Set up the data
        setUpData();
        // Set up the RecyclerView
        setUpRecyclerView();
        // Apply filter based on the selected filter
        filterList(selectedFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpData();
        setUpRecyclerView();
        searchView.clearFocus();
    }

    private void openSearchActivity(String query) {
        Intent intent = new Intent(BrowseActivity.this, SearchActivity.class);
        intent.putExtra("searchQuery", query);

        // Start the SearchActivity with shared element transition
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(BrowseActivity.this, searchView, ViewCompat.getTransitionName(searchView));
        startActivity(intent, options.toBundle());
    }

    private void startSearchActivityTransition() {
        // Define the shared element transition
        TransitionSet transitionSet = new TransitionSet();
        transitionSet.addTransition(new ChangeBounds());
        transitionSet.addTransition(new ChangeTransform());
        transitionSet.addTransition(new ChangeClipBounds());
        transitionSet.addTransition(new ChangeImageTransform());
        transitionSet.setDuration(300);

        // Set the shared element enter transition for the SearchActivity
        getWindow().setSharedElementEnterTransition(transitionSet);

        // Start the SearchActivity with shared element transition
        Intent intent = new Intent(BrowseActivity.this, SearchActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(BrowseActivity.this, searchView, ViewCompat.getTransitionName(searchView));
        startActivity(intent, options.toBundle());
    }

    private void setUpData() {
        this.wivesList = WifeProvider.generateWives();

        // Apply filter if selectedFilter is not null
        if (selectedAgeFilter != null) {
            ArrayList<Wives> filteredWivesList = new ArrayList<>();
            for (Wives wives : this.wivesList) {
                if (wives.getCategory().contains(selectedAgeFilter)) {
                    filteredWivesList.add(wives);
                }
            }
            this.wivesList = filteredWivesList;
        }
        // Update button status based on the selectedFilter
        updateButtonStatus();
    }

    private void updateButtonStatus() {
        // Clear button status
        for (Button button : buttonList) {
            button.setSelected(false);
        }

        // Set the selected button based on the selectedFilter
        if (selectedAgeFilter != null) {
            if (selectedAgeFilter.equals("toTwenty")) {
                toTwenty.setSelected(true);
            } else if (selectedAgeFilter.equals("toTwentyFive")) {
                toTwentyFive.setSelected(true);
            } else if (selectedAgeFilter.equals("toThirty")) {
                toThirty.setSelected(true);
            } else if (selectedAgeFilter.equals("thirtyPlus")) {
                thirtyPlus.setSelected(true);
            }
        }
    }

    private void setUpRecyclerView(){
        wivesRecyclerView = (RecyclerView)findViewById(R.id.browse_recycler_view);
        adapter = new WivesAdapter(this.wivesList, this);
        wivesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        wivesRecyclerView.setAdapter(adapter);
    }


    //initialise buttons and Colors
    @SuppressLint("ResourceType")
    private void iniWidget(){
        toTwenty = (Button) findViewById(R.id.toTwenty);
        toTwentyFive = (Button) findViewById(R.id.toTwentyFive);
        toThirty = (Button) findViewById(R.id.toThirty);
        thirtyPlus = (Button) findViewById(R.id.thirtyPlus);
        favouriteButton = (ImageButton)findViewById(R.id.favouriteButton);
        favouriteButtonFilled = (ImageButton)findViewById(R.id.favouriteButtonFilled);

        // Add buttons to buttonList for easy manipulation
        this.buttonList.add(toTwenty);
        this.buttonList.add(toTwentyFive);
        this.buttonList.add(toThirty);
        this.buttonList.add(thirtyPlus);

        // Customize button color
        for(Button button : buttonList){
            button.setBackgroundTintList(getColorStateList(R.drawable.button_bg_selector));
            button.setTextColor(getColorStateList(R.drawable.button_text_selector));
        }
    }

    // Giving the filter a status
    private void filterList(String status){
        selectedAgeFilter = status;

        ArrayList<Wives> filterWives = new ArrayList<Wives>();
        if(selectedAgeFilter != null){
            for(Wives wives : WifeProvider.generateWives()){
                if (wives.getCategory().contains(status)){
                    filterWives.add(wives);
                }
            }
            this.filterWives = filterWives;
        }

        WivesAdapter adapter;
        RecyclerView wivesRecyclerView;
        if(selectedAgeFilter != null){
            adapter = new WivesAdapter(filterWives, this);
            wivesRecyclerView = findViewById(R.id.browse_recycler_view);
        }else{
            adapter = new WivesAdapter(WifeProvider.generateWives(),this);
            wivesRecyclerView = findViewById(R.id.browse_recycler_view);
        }
        wivesRecyclerView.setAdapter(adapter);
    }

    //Buttons Status
    public void toTwentyTapped(View view) {
        if(!toTwentyClick){
            filterList("toTwenty");
            //Clear status of other buttons
            toTwentyFiveClick = false;
            toThirtyClick = false;
            thirtyPlusClick = false;
            for(Button button : buttonList){
                button.setSelected(false);
            }

            //Keep the entrees button pressed
            toTwenty.setSelected(true);
            toTwentyClick = true;

        }else{
            //Clear button status
            toTwentyClick = false;
            toTwenty.setSelected(false);
            filterList(null);
        }
    }

    public void toTwentyFiveTapped(View view) {
        if(!toTwentyFiveClick){
            filterList("toTwentyFive");
            toTwentyClick = false;
            toThirtyClick = false;
            thirtyPlusClick = false;
            for(Button button : buttonList){
                button.setSelected(false);
            }

            //Keep the button pressed
            toTwentyFive.setSelected(true);
            toTwentyFiveClick = true;

        }else{
            toTwentyFiveClick = false;
            toTwentyFive.setSelected(false);
            filterList(null);
        }

    }

    public void toThirtyTapped(View view) {
        if(!toThirtyClick){
            filterList("toThirty");
            toTwentyClick = false;
            toTwentyFiveClick = false;
            thirtyPlusClick = false;
            for(Button button : buttonList){
                button.setSelected(false);
            }

            //Keep the button pressed
            toThirty.setSelected(true);
            toThirtyClick = true;

        }else{
            toThirtyClick = false;
            toThirty.setSelected(false);
            filterList(null);
        }
    }

    public void thirtyPlusTapped(View view) {
        if(!thirtyPlusClick){
            filterList("thirtyPlus");
            toTwentyClick = false;
            toTwentyFiveClick = false;
            toThirtyClick = false;
            for(Button button : buttonList){
                button.setSelected(false);
            }

            //Keep the button pressed
            thirtyPlus.setSelected(true);
            thirtyPlusClick = true;

        }else{
            thirtyPlusClick = false;
            thirtyPlus.setSelected(false);
            filterList(null);
        }
    }

    @Override
    public void onWifeClick(int position) {

    }

    @Override
    public void onRecentSearchClick(int position) {

    }

    @Override
    public void onRecentSearchRemoveClick(int position) {

    }

//    @Override
//    public void onItemClick(int position) {
//        Intent intent = new Intent(BrowseActivity.this, DetailActivity.class);
//
//        if(selectedAgeFilter != null){
//            Wives selectedItem = this.filterWives.get(position);
//            intent.putExtra("Name", selectedItem.getName());
//            intent.putExtra("Category", selectedItem.getCategory());
//            intent.putExtra("Age", selectedItem.getAge());
//            intent.putExtra("Description", selectedItem.getDescription());
//            intent.putExtra("currentItem", selectedItem);
//            intent.putExtra("Image", selectedItem.getImage());
//
//        }else{
//            Wives selectedItem = this.wivesList.get(position);
//            intent.putExtra("Name", selectedItem.getName());
//            intent.putExtra("Category", selectedItem.getCategory());
//            intent.putExtra("Age", selectedItem.getAge());
//            intent.putExtra("Description", selectedItem.getDescription());
//            intent.putExtra("currentItem", selectedItem);
//            intent.putExtra("Image", selectedItem.getImage());
//        }
//        startActivity(intent);
//    }

    @Override
    public void onFavouriteClick(int position) {
        Wives clickedItem;
        if (selectedAgeFilter != null) {
            clickedItem = filterWives.get(position);
        } else {
            clickedItem = wivesList.get(position);
        }

        if (FavouritesManager.getInstance().isItemInFavourites(clickedItem)) {
            FavouritesManager.getInstance().removeFromFavourites(clickedItem);
        } else {
            FavouritesManager.getInstance().addToFavourites(clickedItem);
        }
    }
}