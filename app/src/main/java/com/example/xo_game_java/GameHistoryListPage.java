package com.example.xo_game_java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xo_game_java.databinding.ActivityGameHistoryListPageBinding;
import com.example.xo_game_java.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class GameHistoryListPage extends AppCompatActivity {

    private DatabaseHelper db = null;

    List<GameHistory> gameHistories = null;

    ActivityGameHistoryListPageBinding binding;
    ListAdapter listAdapter;
    GameHistory gameHistory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameHistoryListPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = new DatabaseHelper(GameHistoryListPage.this);

        gameHistories = db.getAllGameHistories();

        listAdapter = new ListAdapter(GameHistoryListPage.this, gameHistories);

        binding.listview.setAdapter(listAdapter);
        binding.listview.setClickable(true);

        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("GAME ID : ", String.valueOf(gameHistories.get(position).getGameHistoryId()));
                Intent intent = new Intent(GameHistoryListPage.this, ViewReplayPage.class);
                intent.putExtra("gameHistoryId", gameHistories.get(position).getGameHistoryId());
                startActivity(intent);
            }
        });

    }

    public void backToHomePage (View view) {
        Intent intent = new Intent(GameHistoryListPage.this, HomePage.class);
        startActivity(intent);
    }
}