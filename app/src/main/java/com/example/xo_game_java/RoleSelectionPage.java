package com.example.xo_game_java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Spinner;

public class RoleSelectionPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selection_page);
    }

    public void onPlayButtonClick (View view) {
        String playerChoice = "";
        boolean isWantToPlayFirstTurn = false;
        Spinner spinner = findViewById(R.id.char_select_spn);
        playerChoice = spinner.getSelectedItem().toString();
        CheckBox checkBox = findViewById(R.id.wanttofirstturn_chkbox);
        if (checkBox.isChecked()) {
            isWantToPlayFirstTurn = true;
        }
        System.out.println("PLAYER CHOICE : " + playerChoice);
        System.out.println("WANT TO PLAY FIRST TURN : " + isWantToPlayFirstTurn);
        Intent intent = new Intent(RoleSelectionPage.this, PlayWithAiMainPage.class);
        intent.putExtra("playerChoice", playerChoice);
        intent.putExtra("wantToPlayFirstTurn", isWantToPlayFirstTurn);
        startActivity(intent);
    }

    public void backToHomePage (View view) {
        Intent intent = new Intent(RoleSelectionPage.this, HomePage.class);
        startActivity(intent);
    }
}