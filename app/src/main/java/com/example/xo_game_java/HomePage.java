package com.example.xo_game_java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomePage extends AppCompatActivity {

    //private DatabaseHelper db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //db = new DatabaseHelper(HomePage.this);

        //GameHistory gameHistory = new GameHistory("21-10-2023", "Tie", "HUMAN VS HUMAN");
        //db.addGameHistory(gameHistory);
    }

    public void onViewGameHistoryListPage (View view) {
        Intent intent = new Intent(HomePage.this, GameHistoryListPage.class);
        startActivity(intent);
    }

    public void onMultiplayerChoice (View view) {
        Intent intent = new Intent(HomePage.this, MultiplayerSelectionPage.class);
        intent.putExtra("playType", "MULTIPLAYER");
        startActivity(intent);
    }

    public void onVersusAIChoice (View view) {
        Intent intent = new Intent(HomePage.this, RoleSelectionPage.class);
        startActivity(intent);
    }

}