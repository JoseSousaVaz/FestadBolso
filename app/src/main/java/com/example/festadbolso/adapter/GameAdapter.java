package com.example.festadbolso.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.festadbolso.R;
import com.example.festadbolso.UI.GameDetailsActivity;
import com.example.festadbolso.model.Game;
import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {
    private List<Game> games;
    private Context context;

    public GameAdapter(Context context, List<Game> games) {
        this.context = context;
        this.games = games;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        Game game = games.get(position);
        holder.gameName.setText(game.getName());
        holder.gameDescription.setText(game.getDescription());

        // Load image using Glide
        Glide.with(holder.itemView.getContext())
                .load(game.getImageUrl())
                .placeholder(R.drawable.placeholder_image) // Placeholder image
                .error(R.drawable.error_image) // Error image
                .into(holder.gameImage);

        // Set click listener for the item
        holder.itemView.setOnClickListener(v -> {
            // Pass the game ID to GameDetailsActivity
            Intent intent = new Intent(context, GameDetailsActivity.class);
            intent.putExtra("randomId", Long.parseLong(game.getId())); // Pass the game ID as a long
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public static class GameViewHolder extends RecyclerView.ViewHolder {
        TextView gameName, gameDescription;
        ImageView gameImage;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            gameName = itemView.findViewById(R.id.gameName);
            gameDescription = itemView.findViewById(R.id.gameDescription);
            gameImage = itemView.findViewById(R.id.gameImage);
        }
    }
}