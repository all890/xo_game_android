package com.example.xo_game_java;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainPage extends AppCompatActivity {

    private char statusPlayer = 'O';
    private char winPlayer;
    private int sizeOfBoard = 0;
    private int winScore = 0;
    private int maxNumberOfTurns = sizeOfBoard * sizeOfBoard;
    private int currentNumberOfTurns = 0;
    private char[][] charArray;
    private DatabaseHelper db = null;

    List<GameHistoryDetails> gameHistoryDetails = null;
    GameHistory gameHistory = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Intent intent = getIntent();
        sizeOfBoard = intent.getIntExtra("selectedSize", sizeOfBoard);
        winScore = intent.getIntExtra("winScore", winScore);
        maxNumberOfTurns = sizeOfBoard * sizeOfBoard;

        db = new DatabaseHelper(MainPage.this);

        gameHistory = new GameHistory();
        gameHistoryDetails = new ArrayList<>();

        TextView current_player_txt = findViewById(R.id.current_player_txt);
        current_player_txt.setText("รอบของผู้เล่น : " + statusPlayer);
        //current_player_txt = findViewById(R.id.current_player_txt);
        //current_player_txt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        //current_player_txt.setText("Current player is : " + statusPlayer);

        charArray = new char[sizeOfBoard][sizeOfBoard];

        LinearLayout mainLinearLayout = findViewById(R.id.main_linear_layout);
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

                charArray[i][j] = 'N';

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

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentNumberOfTurns++;
                        if (statusPlayer == 'X') {
                            imageView.setImageResource(R.drawable.x_char_2);
                        } else {
                            imageView.setImageResource(R.drawable.o_char_2);
                        }
                        imageView.setEnabled(false);
                        charArray[tempI][tempJ] = statusPlayer;

                        GameHistoryDetails gameHistoryDetail = new GameHistoryDetails(tempI, tempJ, String.valueOf(statusPlayer), 0);
                        gameHistoryDetails.add(gameHistoryDetail);

                        if (checkWin(charArray)) {
                            Log.i("WIN", "WINNER IS " + statusPlayer + " PLAYER!");
                            winPlayer = statusPlayer;
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainPage.this);

                            builder.setTitle("แจ้งเตือน");
                            builder.setMessage("ผู้ชนะในเกมนี้คือผู้เล่น " + statusPlayer);
                            builder.setCancelable(false);


                            builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    //Set game history object
                                    recordGameHistoryOnDatabase("WIN");

                                    Intent intent = new Intent(MainPage.this, HomePage.class);
                                    startActivity(intent);
                                }
                            });

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        } else if (checkTie()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainPage.this);

                            builder.setTitle("แจ้งเตือน");
                            builder.setMessage("เกมนี้เสมอ!");
                            builder.setCancelable(false);

                            builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    recordGameHistoryOnDatabase("TIE");
                                    Intent intent = new Intent(MainPage.this, HomePage.class);
                                    startActivity(intent);
                                }
                            });

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                        //System.out.println("COL : " + tempJ + " ROW : " + tempI + " was taken by player " + statusPlayer);
                        switchTurn();
                        System.out.println("NUMBER OF TURNS : " + currentNumberOfTurns);
                    }
                });

                tableRow.addView(imageView);
            }

            tableLayout.addView(tableRow);
        }

        mainLinearLayout.addView(tableLayout);
    }

    private void switchTurn () {
        statusPlayer = statusPlayer == 'X' ? 'O' : 'X';
        updateCurrentPlayerTxt();
    }

    private void updateCurrentPlayerTxt () {
        TextView current_player_txt = findViewById(R.id.current_player_txt);
        current_player_txt.setText("รอบของผู้เล่น : " + statusPlayer);
    }

    private boolean checkTie () {
        return maxNumberOfTurns == currentNumberOfTurns;
    }

    private void recordGameHistoryOnDatabase (String endStatus) {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        gameHistory.setPlayDate(dateFormat.format(currentDate));
        gameHistory.setResult(!endStatus.equals("TIE") ? "ผู้เล่น " + winPlayer + " ชนะ" : "เสมอ");
        gameHistory.setPlayType("เล่นกับเพื่อน");
        gameHistory.setBoardSize(sizeOfBoard);

        int gameHistoryId = db.addGameHistory(gameHistory);

        for (int i = 0; i < gameHistoryDetails.size(); i++) {
            gameHistoryDetails.get(i).setGameHistoryId(gameHistoryId);
            db.addGameHistoryDetails(gameHistoryDetails.get(i));
        }
    }

    private boolean isDiagonal (char[][] board) {
        int points;
        //i and j condition value equals k-1
        for (int i = 0; i < sizeOfBoard - (winScore - 1); i++) {
            for (int j = 0; j < sizeOfBoard - (winScore - 1); j++) {
                String strRes = "";
                points = 0;
                //k condition value for target score
                for (int k = 0; k < winScore; k++) {
                    strRes += board[i+k][k+j];
                }
                for (int l = 0; l < strRes.length(); l++) {
                    if (l < strRes.length() - 1) {
                        if (strRes.charAt(l) == strRes.charAt(l+1) && strRes.charAt(l) != 'N') {
                            points++;
                        }
                    }
                }
                if (points == (winScore - 1)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkWin (char[][] board) {
        return isStraightHorizontalLine(board) || isStraightVerticalLine(board) || isDiagonal(board) || isAntiDiagonal(board);
    }

    private boolean isStraightHorizontalLine (char[][] board) {
        int points;
        for (int i = 0; i < sizeOfBoard; i++) {
            String strRes = "";
            points = 0;
            for (int j = 0; j < sizeOfBoard; j++) {
                strRes += board[i][j];
            }
            for (int k = 0; k < strRes.length(); k++) {
                if (k < strRes.length() - 1) {
                    if (strRes.charAt(k) == strRes.charAt(k+1) && strRes.charAt(k) != 'N') {
                        points++;
                    }
                }
            }
            if (points == (winScore - 1)) {
                return true;
            }
        }
        return false;
    }

    private boolean isStraightVerticalLine (char[][] board) {
        int points;
        for (int i = 0; i < sizeOfBoard; i++) {
            String strRes = "";
            points = 0;
            for (int j = 0; j < sizeOfBoard; j++) {
                strRes += board[j][i];
            }
            for (int k = 0; k < strRes.length(); k++) {
                if (k < strRes.length() - 1) {
                    if (strRes.charAt(k) == strRes.charAt(k+1) && strRes.charAt(k) != 'N') {
                        points++;
                    }
                }
            }
            if (points == (winScore - 1)) {
                return true;
            }
        }
        return false;
    }

    /*
    private boolean isDiagonal (char[][] board) {
        String strRes = "";
        int points = 0;
        for (int i = 0; i < sizeOfBoard; i++) {
            strRes += board[i][i];
        }
        for (int j = 0; j < strRes.length(); j++) {
            if (j < strRes.length() - 1) {
                if (strRes.charAt(j) == strRes.charAt(j+1) && strRes.charAt(j) != 'N') {
                    points++;
                }
            }
        }
        if (sizeOfBoard >= 4) {
            return points == 3;
        } else {
            return points == 2;
        }
    }
    */

    private boolean isAntiDiagonal (char[][] board) {
        int points;
        //i and j condition value equals k-1
        for (int i = 0; i < sizeOfBoard - (winScore - 1); i++) {
            for (int j = 0; j < sizeOfBoard - (winScore - 1); j++) {
                String strRes = "";
                points = 0;
                //k condition value for target score
                for (int k = 0; k < winScore; k++) {
                    strRes += board[((winScore - 1) - k) + i][k+j];
                }
                for (int l = 0; l < strRes.length(); l++) {
                    if (l < strRes.length() - 1) {
                        if (strRes.charAt(l) == strRes.charAt(l+1) && strRes.charAt(l) != 'N') {
                            points++;
                        }
                    }
                }
                if (points == (winScore - 1)) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
    private boolean isAntiDiagonal (char[][] board) {
        String strRes = "";
        int points = 0;
        for (int i = 0; i < sizeOfBoard; i++) {
            strRes += board[i][sizeOfBoard - (i+1)];
        }
        for (int j = 0; j < strRes.length(); j++) {
            if (j < strRes.length() - 1) {
                if (strRes.charAt(j) == strRes.charAt(j+1) && strRes.charAt(j) != 'N') {
                    points++;
                }
            }
        }
        if (sizeOfBoard >= 4) {
            return points == 3;
        } else {
            return points == 2;
        }
    }
    */

    public void backToHomePage (View view) {
        Intent intent = new Intent(MainPage.this, HomePage.class);
        startActivity(intent);
    }

}