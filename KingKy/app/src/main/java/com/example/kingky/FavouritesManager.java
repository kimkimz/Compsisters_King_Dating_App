package com.example.kingky;

import java.util.ArrayList;

public class FavouritesManager {
    private static FavouritesManager instance;
    private ArrayList<Wives> favouritesList;

    private FavouritesManager() {
        favouritesList = new ArrayList<>();
    }

    public static FavouritesManager getInstance() {
        if (instance == null) {
            instance = new FavouritesManager();
        }
        return instance;
    }

    public ArrayList<Wives> getFavouritesList() {
        return favouritesList;
    }

    public void addToFavourites(Wives wives) {
        boolean itemExists = false;
        for (Wives favItem : favouritesList) {
            if (favItem.getName().equals(wives.getName())) {
                itemExists = true;
                break;
            }
        }
        if (!itemExists) {
            favouritesList.add(wives);
        }
    }

    public void removeFromFavourites(Wives wives) {
        favouritesList.remove(wives);
    }

    public boolean isItemInFavourites(Wives wives) {
        return favouritesList.contains(wives);
    }

    public void clearFavourites() {
        favouritesList.clear();
    }
}

