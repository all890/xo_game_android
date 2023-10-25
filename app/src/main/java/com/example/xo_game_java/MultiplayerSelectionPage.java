package com.example.xo_game_java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

public class MultiplayerSelectionPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_selection_page);
    }

    public void onStartGameClick (View view) {
        Intent intent = new Intent(MultiplayerSelectionPage.this, MainPage.class);
        Spinner spinner = findViewById(R.id.size_board_spn);
        String selectedSize = spinner.getSelectedItem().toString();
        int intSize = 0;
        switch (selectedSize) {
            case "3x3 (3 แต้ม)" : intSize = 3; break;
            case "4x4 (4 แต้ม)" : intSize = 4; break;
            case "5x5 (4 แต้ม)" : intSize = 5; break;
            case "6x6 (5 แต้ม)" : intSize = 6; break;
            case "7x7 (5 แต้ม)" : intSize = 7; break;
            case "8x8 (5 แต้ม)" : intSize = 8; break;
            case "9x9 (5 แต้ม)" : intSize = 9; break;
            case "10x10 (5 แต้ม)" : intSize = 10; break;
            case "11x11 (6 แต้ม)" : intSize = 11; break;
        }
        intent.putExtra("selectedSize", intSize);
        int winScore = 0;
        if (intSize == 3 || intSize == 4) {
            winScore = 3;
        } else if (intSize == 5) {
            winScore = 4;
        } else if (intSize > 5 && intSize <= 10) {
            winScore = 5;
        } else {
            winScore = 6;
        }
        intent.putExtra("winScore", winScore);
        startActivity(intent);
    }

    public void backToHomePage (View view) {
        Intent intent = new Intent(MultiplayerSelectionPage.this, HomePage.class);
        startActivity(intent);
    }
}