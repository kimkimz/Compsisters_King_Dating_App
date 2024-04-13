package com.example.kingky;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecentSearchAdapter extends RecyclerView.Adapter<RecentSearchAdapter.RecentSearchViewHolder> {
    private List<String> recentSearches;
    private RecentSearchAdapterListener listener;

    public interface RecentSearchAdapterListener {
        void onRecentSearchRemoveClick(int position);
        void onRecentSearchClick(int position);
    }

    public RecentSearchAdapter(List<String> recentSearches, RecentSearchAdapterListener listener) {
        this.recentSearches = recentSearches;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecentSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_search, parent, false);
        return new RecentSearchViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentSearchViewHolder holder, int position) {
        String recentSearch = recentSearches.get(position);
        holder.bind(recentSearch);
    }

    @Override
    public int getItemCount() {
        return recentSearches.size();
    }

    public class RecentSearchViewHolder extends RecyclerView.ViewHolder {
        private TextView recentSearchTextView;
        private ImageButton removeRecentSearchButton;
        private RelativeLayout recentSearch;
        private RecentSearchAdapterListener removeClickListener;

        public RecentSearchViewHolder(@NonNull View itemView, RecentSearchAdapterListener listener) {
            super(itemView);
            recentSearchTextView = itemView.findViewById(R.id.query);
            removeRecentSearchButton = itemView.findViewById(R.id.removeRecentSearchButton);
            recentSearch = itemView.findViewById(R.id.recentSearch);
            removeClickListener = listener;

            recentSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (removeClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            removeClickListener.onRecentSearchClick(position);
                        }
                    }
                }
            });

            removeRecentSearchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (removeClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            removeClickListener.onRecentSearchRemoveClick(position);
                        }
                    }
                }
            });
        }

        public void bind(String recentSearch) {
            recentSearchTextView.setText(recentSearch);
        }
    }
}
