package com.example.xo_game_java;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

public class ViewReplayPage extends AppCompatActivity {

    DatabaseHelper db = null;

    List<GameHistoryDetails> gameHistoryDetailsList = null;
    GameHistory gameHistory = null;

    private int sizeOfBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_replay_page);

        db = new DatabaseHelper(ViewReplayPage.this);

        Intent intent = getIntent();
        int gameHistoryId = intent.getIntExtra("gameHistoryId", 0);

        gameHistoryDetailsList = db.getGameHistoryDetailsByGameHistoryId(gameHistoryId);
        gameHistory = db.getGameHistoryById(gameHistoryId);

        sizeOfBoard = gameHistory.getBoardSize();

        //Log.i("GAME HISTORY ID", String.valueOf(gameHistory.getGameHistoryId()));
        //Log.i("SIZE OF DETAILS", String.valueOf(gameHistoryDetailsList.size()));

        LinearLayout mainLinearLayout = findViewById(R.id.main_replay_layout);
        TableLayout tableLayout = new TableLayout(this);

        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);

        tableLayout.setLayoutParams(tableLayoutParams);

        for (int i = 0; i < sizeOfBoard; i ++) {
            // Create a new TableRow for each row
            TableRow tableRow = new TableRow(this);

            // Create LayoutParams for the TableRow
            TableRow.LayoutParams rowParams = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);

            // Set LayoutParams for the TableRow
            tableRow.setLayoutParams(rowParams);
            //tableRow.setBackgroundResource(R.drawable.table_row_border);

            for (int j = 0; j < sizeOfBoard; j++) {

                // Create a new ImageView
                ImageView imageView = new ImageView(this);

                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                // Set the desired width and height in pixels
                Log.i("Size", String.valueOf(displayMetrics.widthPixels));

                int lockedSize = displayMetrics.widthPixels;

                int sizeOfChar = (lockedSize / sizeOfBoard) - (int)(lockedSize * 0.01);

                int tempJ = j;
                int tempI = i;

                // Set the layout parameters for the ImageView
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(sizeOfChar, sizeOfChar);
                imageView.setLayoutParams(layoutParams);

                // Set the image resource programmatically (assuming you have an image resource)
                imageView.setImageResource(R.drawable.emp_char_2);

                String idImage = String.valueOf(tempI) + "000" + String.valueOf(tempJ);
                imageView.setId(Integer.parseInt(idImage));

                tableRow.addView(imageView);
            }

            tableLayout.addView(tableRow);
        }

        mainLinearLayout.addView(tableLayout);

        try {
            replay();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void backToHomePage (View view) {
        Intent intent = new Intent(ViewReplayPage.this, HomePage.class);
        startActivity(intent);
    }

    private void testLoop (GameHistoryDetails gameHistoryDetails) {
        Log.i("GAME HIST DETAILS ID", String.valueOf(gameHistoryDetails.getGameHistDetailsId()));
        Log.i("PLAYER TURN", gameHistoryDetails.getPlayer());

        Log.i("ROW", String.valueOf(gameHistoryDetails.getRowCoordinate()));
        Log.i("COL", String.valueOf(gameHistoryDetails.getColumnCoordinate()));

        ImageView imageView = findViewById(Integer.parseInt(gameHistoryDetails.getRowCoordinate() + "000" + gameHistoryDetails.getColumnCoordinate()));
        if (gameHistoryDetails.getPlayer().equals("X")) {
            imageView.setImageResource(R.drawable.x_char_2);
        } else {
            imageView.setImageResource(R.drawable.o_char_2);
        }
    }

    private void updateCurrentPlayerTxt (String currentPlayer) {
        TextView textView = findViewById(R.id.current_player_replay_txt);
        textView.setText("รอบของผู้เล่น : " + (currentPlayer.equals("X") ? "O" : "X"));
    }

    private void replay () throws InterruptedException {

        final Handler handler = new Handler();
        final int[] i = {0};

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.i("GAME HIST DETAILS ID", String.valueOf(gameHistoryDetailsList.get(i[0]).getGameHistDetailsId()));
                Log.i("PLAYER TURN", gameHistoryDetailsList.get(i[0]).getPlayer());

                Log.i("ROW", String.valueOf(gameHistoryDetailsList.get(i[0]).getRowCoordinate()));
                Log.i("COL", String.valueOf(gameHistoryDetailsList.get(i[0]).getColumnCoordinate()));

                ImageView imageView = findViewById(Integer.parseInt(gameHistoryDetailsList.get(i[0]).getRowCoordinate() + "000" + gameHistoryDetailsList.get(i[0]).getColumnCoordinate()));
                if (gameHistoryDetailsList.get(i[0]).getPlayer().equals("X")) {
                    imageView.setImageResource(R.drawable.x_char_2);
                } else {
                    imageView.setImageResource(R.drawable.o_char_2);
                }

                updateCurrentPlayerTxt(gameHistoryDetailsList.get(i[0]).getPlayer());

                i[0]++;

                if (i[0] < gameHistoryDetailsList.size()) {
                    // Schedule the next iteration after the delay
                    handler.postDelayed(this, 2000);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ViewReplayPage.this);

                    builder.setTitle("แจ้งเตือน");
                    builder.setMessage("จบการรีเพลย์แล้ว");
                    builder.setCancelable(false);

                    builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(ViewReplayPage.this, GameHistoryListPage.class);
                            startActivity(intent);
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        };

        // Start the loop by posting the first iteration
        handler.post(runnable);

        /*
        final Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (GameHistoryDetails gameHistoryDetails : gameHistoryDetailsList) {
                    Log.i("GAME HIST DETAILS ID", String.valueOf(gameHistoryDetails.getGameHistDetailsId()));
                    Log.i("PLAYER TURN", gameHistoryDetails.getPlayer());

                    Log.i("ROW", String.valueOf(gameHistoryDetails.getRowCoordinate()));
                    Log.i("COL", String.valueOf(gameHistoryDetails.getColumnCoordinate()));

                    ImageView imageView = findViewById(Integer.parseInt(gameHistoryDetails.getRowCoordinate() + "000" + gameHistoryDetails.getColumnCoordinate()));
                    if (gameHistoryDetails.getPlayer().equals("X")) {
                        imageView.setImageResource(R.drawable.x_char_2);
                    } else {
                        imageView.setImageResource(R.drawable.o_char_2);
                    }
                    handler.postDelayed(this, 2000);
                }
            }
        };

        handler.post(runnable);


         */
    }
}