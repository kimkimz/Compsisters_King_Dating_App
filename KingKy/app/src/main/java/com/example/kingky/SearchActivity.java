package com.example.kingky;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;

public class SearchActivity extends AppCompatActivity implements RecyclerViewInterface, RecentSearchAdapter.RecentSearchAdapterListener {

    boolean isLandscape = false;
    private RecyclerView itemRecyclerView;
    private ArrayList<Wives> wivesList;

    private SearchView searchView;
    private ArrayList<Wives> searchResults = new ArrayList<>();

    private TextView searchResultsText;

    private LinearLayout noSearchResult;

    // List to store search history
    private LinkedList<String> searchHistory = new LinkedList<>();
    private static final int MAX_SEARCH_HISTORY = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Find the SearchView by its ID
        searchView = findViewById(R.id.searchView);

        // Get the search query from the intent
        Intent intent = getIntent();
        if (intent != null) {
            String searchQuery = intent.getStringExtra("searchQuery");
            if (searchQuery != null) {
                // Set the search query in the SearchView
                searchView.setQuery(searchQuery, false);
                performSearch(searchQuery);
            }
        }

        // Request focus and show the keyboard on the SearchView
        searchView.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(searchView, InputMethodManager.SHOW_IMPLICIT);

        // Set the OnQueryTextListener on the SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                addToSearchHistory(query); // Add the search query to the search history
                return false; // Return false to let the SearchView perform the default action (e.g., show suggestions or handle submit button)
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    // Show the recent search suggestions
                    updateSearchSuggestions();
                    clearSearchResults(); // Clear the search results and hide the RecyclerView
                } else {
                    performSearch(newText); // Call performSearch() when the query text changes
                    hideSearchSuggestions();
                }
                return true;
            }
        });

        Button clearRecentSearchButton = findViewById(R.id.clearRecentSearchButton);
        clearRecentSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearRecentSearchHistory();
            }
        });

        // Initialise
        setUpItemData();
        setUpItemRecyclerView();
    }

    private void clearRecentSearchHistory() {
        searchHistory.clear();
        updateSearchSuggestions();
    }

    private void hideSearchSuggestions() {
        RecyclerView recentSearchRecyclerView = findViewById(R.id.recentSearchRecyclerView);
        TextView recentSearchResultsText = findViewById(R.id.recentSearchResultsText);
        Button clearRecentSearchButton = findViewById(R.id.clearRecentSearchButton);
        recentSearchRecyclerView.setVisibility(View.GONE);
        recentSearchResultsText.setVisibility(View.GONE);
        clearRecentSearchButton.setVisibility(View.GONE);
    }

    // Method to clear search results and hide the RecyclerView
    private void clearSearchResults() {
        searchResultsText = findViewById(R.id.searchResultsText);
        noSearchResult = findViewById(R.id.noSearchResult);
        ArrayList<Wives> emptyList = new ArrayList<>();
        WivesAdapter adapter = new WivesAdapter(emptyList, this);
        itemRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        itemRecyclerView.setVisibility(View.GONE);
        searchResultsText.setVisibility(View.GONE);
        noSearchResult.setVisibility(View.GONE);
    }

    private void performSearch(String query) {

        searchResults.clear();
        searchResultsText = findViewById(R.id.searchResultsText);
        noSearchResult = findViewById(R.id.noSearchResult);

        for (int i = 0; i < wivesList.size(); i++) {
            Wives wives = wivesList.get(i);
            if (wives.getName().toLowerCase().contains(query.toLowerCase())) {
                searchResults.add(wives);
            }
        }

        // Update the RecyclerView adapter with the search results
        WivesAdapter adapter = new WivesAdapter(searchResults, this);
        itemRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // Show/hide the RecyclerView based on the search results
        if (searchResults.isEmpty()) {
            itemRecyclerView.setVisibility(View.GONE); // Hide the RecyclerView
            searchResultsText.setVisibility(View.GONE);
            if (!query.isEmpty()) {
                noSearchResult.setVisibility(View.VISIBLE);
            }
        } else {
            itemRecyclerView.setVisibility(View.VISIBLE); // Show the RecyclerView
            searchResultsText.setVisibility(View.VISIBLE);
            noSearchResult.setVisibility(View.GONE);
        }
    }

    private void setUpItemData(){
        this.wivesList = WifeProvider.generateWives();
    }

    private void setUpItemRecyclerView(){
        itemRecyclerView = (RecyclerView)findViewById(R.id.itemRecyclerView);
        WivesAdapter adapter = new WivesAdapter(this.wivesList, this);
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemRecyclerView.setAdapter(adapter);
    }

//    @Override
//    public void onItemClick(int position) {
//        Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
//
//        // Get the clicked item from the search results
//        Item clickedItem = searchResults.get(position);
//
//        // Pass the necessary information to the detail activity
//        intent.putExtra("Name", clickedItem.getName());
//        intent.putExtra("Price", String.format("$%s", clickedItem.getPrice()));
//        intent.putExtra("Description", clickedItem.getDescription());
//        intent.putExtra("currentItem", clickedItem);
//
//        // putting imageList
//        ArrayList<Integer> imageList = new ArrayList<Integer>();
//        for(int i = 0; i<3; i++){
//            imageList.add(this.searchResults.get(position).getImageList()[i]);
//        }
//        intent.putIntegerArrayListExtra("ImageList", imageList);
//
//        startActivity(intent);
//    }

    @Override
    public void onFavouriteClick(int position) {
        // Get the item added to cart
        Wives clickedItem = searchResults.get(position);

        if (FavouritesManager.getInstance().isItemInFavourites(clickedItem)) {
            FavouritesManager.getInstance().removeFromFavourites(clickedItem);
        } else {
            FavouritesManager.getInstance().addToFavourites(clickedItem);
        }
    }

    // Method to add a search query to the search history
    private void addToSearchHistory(String query) {
        if (TextUtils.isEmpty(query)) {
            return;
        }

        // Check if the query already exists in the search history
        if (searchHistory.contains(query)) {
            return; // Don't add duplicate queries
        }

        // Remove the oldest query if the maximum search history limit is reached
        if (searchHistory.size() == MAX_SEARCH_HISTORY) {
            searchHistory.removeFirst();
        }

        // Add the new query to the end of the search history
        searchHistory.addFirst(query);

        // Update the recent search query suggestions if needed
        updateSearchSuggestions();
    }

    @Override
    public void onWifeClick(int position) {

    }

    @Override
    public void onRecentSearchClick(int position) {
        String recentSearch = searchHistory.get(position);
        searchView.setQuery(recentSearch, true); // Set the query and submit it for searching
    }

    @Override
    public void onRecentSearchRemoveClick(int position) {
        // Handle the click event when the removeRecentSearchButton is clicked
        if (position >= 0 && position < searchHistory.size()) {
            searchHistory.remove(position);
            updateSearchSuggestions();
        }
    }

    // Method to update the recent search query suggestions
    private void updateSearchSuggestions() {
        // Get the reference to the RecyclerView for recent search queries
        RecyclerView recentSearchRecyclerView = findViewById(R.id.recentSearchRecyclerView);

        // Create an instance of the adapter for the RecyclerView
        RecentSearchAdapter adapter = new RecentSearchAdapter(searchHistory, this);

        // Set the adapter to the RecyclerView
        recentSearchRecyclerView.setAdapter(adapter);

        // Get the search query from the SearchView
        String query = searchView.getQuery().toString().trim();

        // Show/hide the recent search query suggestions based on the searchHistory list and the search query
        TextView recentSearchResultsText = findViewById(R.id.recentSearchResultsText);
        Button clearRecentSearchButton = findViewById(R.id.clearRecentSearchButton);

        if (searchHistory.isEmpty() || !TextUtils.isEmpty(query)) {
            recentSearchRecyclerView.setVisibility(View.GONE);
            recentSearchResultsText.setVisibility(View.GONE);
            clearRecentSearchButton.setVisibility(View.GONE);
        } else {
            recentSearchRecyclerView.setVisibility(View.VISIBLE);
            recentSearchResultsText.setVisibility(View.VISIBLE);
            clearRecentSearchButton.setVisibility(View.VISIBLE);
        }
    }
}
