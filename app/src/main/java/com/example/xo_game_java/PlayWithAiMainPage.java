package com.example.xo_game_java;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlayWithAiMainPage extends AppCompatActivity {

    private char statusPlayer;
    private char statusAI;
    private char currentPlayer;
    private char winPlayer;
    private int sizeOfBoard = 3;
    private char[][] board;
    private boolean wantToPlayFirstTurn;
    List<GameHistoryDetails> gameHistoryDetails = null;
    GameHistory gameHistory = null;
    private DatabaseHelper db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_with_ai_main_page);

        db = new DatabaseHelper(PlayWithAiMainPage.this);

        gameHistory = new GameHistory();
        gameHistoryDetails = new ArrayList<>();

        Intent intent = getIntent();
        statusPlayer = intent.getStringExtra("playerChoice").equals("X") ? 'X' : 'O';
        statusAI = statusPlayer == 'X'? 'O' : 'X';
        wantToPlayFirstTurn = intent.getBooleanExtra("wantToPlayFirstTurn", false);
        //sizeOfBoard = 2;

        System.out.println("STATUS PLAYER IS : " + statusPlayer);
        System.out.println("STATUS AI IS : " + statusAI);
        System.out.println("WANT TO PLAY FIRST TURN : " + wantToPlayFirstTurn);
        System.out.println("SIZE IS : " + sizeOfBoard);

        if (wantToPlayFirstTurn) {
            currentPlayer = statusPlayer;
        }

        board = new char[sizeOfBoard][sizeOfBoard];

        LinearLayout mainLinearLayout = findViewById(R.id.main_layout);
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

                board[i][j] = 'N';

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

                String idImage = tempI + "" + tempJ;
                System.out.println("IMAGE ID IS : " + idImage);
                imageView.setId(Integer.parseInt(idImage));

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //currentNumberOfTurns++;
                        if (statusPlayer == 'X') {
                            imageView.setImageResource(R.drawable.x_char_2);
                        } else {
                            imageView.setImageResource(R.drawable.o_char_2);
                        }
                        imageView.setEnabled(false);
                        board[tempI][tempJ] = statusPlayer;

                        GameHistoryDetails gameHistoryDetail = new GameHistoryDetails(tempI, tempJ, String.valueOf(currentPlayer), 0);
                        gameHistoryDetails.add(gameHistoryDetail);

                        checkWinAndAlertDialog();

                        currentPlayer = statusAI;


                        if (!checkDraw()) {
                            aiMakeMove();
                        }

                    }
                });

                tableRow.addView(imageView);
            }

            tableLayout.addView(tableRow);
        }

        mainLinearLayout.addView(tableLayout);

        if (!wantToPlayFirstTurn) {
            aiMakeMove();
        }
    }

    private void checkWinAndAlertDialog () {
        if (checkWin(board)) {
            Log.i("WIN", "WINNER IS " + currentPlayer + " PLAYER!");
            winPlayer = currentPlayer;
            AlertDialog.Builder builder = new AlertDialog.Builder(PlayWithAiMainPage.this);

            builder.setTitle("แจ้งเตือน");
            builder.setMessage("ผู้ชนะในเกมนี้คือผู้เล่น " + currentPlayer);
            builder.setCancelable(false);

            builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    //Set game history object
                    recordGameHistoryOnDatabase("WIN");

                    Intent intent = new Intent(PlayWithAiMainPage.this, HomePage.class);
                    startActivity(intent);
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else if (checkTie()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(PlayWithAiMainPage.this);

            builder.setTitle("แจ้งเตือน");
            builder.setMessage("เกมนี้เสมอ!");
            builder.setCancelable(false);

            builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    recordGameHistoryOnDatabase("TIE");
                    Intent intent = new Intent(PlayWithAiMainPage.this, HomePage.class);
                    startActivity(intent);
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    private boolean checkTie () {
        for (int i = 0; i < sizeOfBoard; i++) {
            for (int j = 0; j < sizeOfBoard; j++) {
                if (board[i][j] == 'N') {
                    return false;
                }
            }
        }
        return true;
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
            if (points == (sizeOfBoard - 1)) {
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
            if (points == (sizeOfBoard - 1)) {
                return true;
            }
        }
        return false;
    }

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
        return points == (sizeOfBoard - 1);
    }

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
        return points == (sizeOfBoard - 1);
    }

    private void recordGameHistoryOnDatabase (String endStatus) {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        gameHistory.setPlayDate(dateFormat.format(currentDate));
        String ha = "";
        if (winPlayer == statusPlayer) {
            ha = "HUMAN";
        } else {
            ha = "AI";
        }
        gameHistory.setResult(!endStatus.equals("TIE") ? "ผู้เล่น " + ha + " ชนะ" : "เสมอ");
        gameHistory.setPlayType("เล่นกับบอท");
        gameHistory.setBoardSize(sizeOfBoard);

        int gameHistoryId = db.addGameHistory(gameHistory);

        for (int i = 0; i < gameHistoryDetails.size(); i++) {
            gameHistoryDetails.get(i).setGameHistoryId(gameHistoryId);
            db.addGameHistoryDetails(gameHistoryDetails.get(i));
        }
    }

    private void aiMakeMove () {
        int bestScore = -10000;
        int bestRowMove = 0;
        int bestColMove = 0;

        for (int i = 0; i < sizeOfBoard; i++) {
            for (int j = 0; j < sizeOfBoard; j++) {
                if (board[i][j] == 'N') {
                    board[i][j] = statusAI;
                    int score = minimax(board, 0, false);
                    board[i][j] = 'N';
                    if (score > bestScore) {
                        bestScore = score;
                        bestRowMove = i;
                        bestColMove = j;
                    }
                }
            }
        }

        System.out.println("ROW : " + bestRowMove + " COL: " + bestColMove);
        //Thread.sleep(1000);

        board[bestRowMove][bestColMove] = statusAI;
        ImageView imageView = findViewById(Integer.parseInt(bestRowMove + "" + bestColMove));
        if (statusAI == 'X') {
            imageView.setImageResource(R.drawable.x_char_2);
        } else {
            imageView.setImageResource(R.drawable.o_char_2);
        }
        imageView.setEnabled(false);

        GameHistoryDetails gameHistoryDetail = new GameHistoryDetails(bestRowMove, bestColMove, String.valueOf(currentPlayer), 0);
        gameHistoryDetails.add(gameHistoryDetail);

        checkWinAndAlertDialog();

        currentPlayer = statusPlayer;

    }

    private int minimax (char[][] board, int depth, boolean isMaximizing) {
        if (checkHorizontalWinForEachPlayer(statusAI)
                || checkVerticalWinForEachPlayer(statusAI)
                || checkDiagonalWinForEachPlayer(statusAI)
                || checkAntiDiagonalWinForEachPlayer(statusAI)) {
            return 100;
        } else if (checkHorizontalWinForEachPlayer(statusPlayer)
                || checkVerticalWinForEachPlayer(statusPlayer)
                || checkDiagonalWinForEachPlayer(statusPlayer)
                || checkAntiDiagonalWinForEachPlayer(statusPlayer)) {
            return -100;
        } else if (checkDraw()) {
            return 0;
        }

        //System.out.println("Depth : " + depth);

        if (isMaximizing) {
            int bestScore = (int)Double.NEGATIVE_INFINITY;
            for (int i = 0; i < sizeOfBoard; i++) {
                for (int j = 0; j < sizeOfBoard; j++) {
                    if (board[i][j] == 'N') {
                        board[i][j] = statusAI;
                        int score = minimax(board, depth + 1, false);
                        board[i][j] = 'N';
                        if (score > bestScore) {
                            bestScore = score;
                        }
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = (int)Double.POSITIVE_INFINITY;
            for (int i = 0; i < sizeOfBoard; i++) {
                for (int j = 0; j < sizeOfBoard; j++) {
                    if (board[i][j] == 'N') {
                        board[i][j] = statusPlayer;
                        int score = minimax(board, depth + 1,true);
                        board[i][j] = 'N';
                        if (score < bestScore) {
                            bestScore = score;
                        }
                    }
                }
            }
            return bestScore;
        }
    }

    private boolean checkHorizontalWinForEachPlayer (char player) {
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
            if (points == (sizeOfBoard - 1) && strRes.charAt(0) == player) {
                return true;
            }
        }
        return false;
    }

    private boolean checkVerticalWinForEachPlayer (char player) {
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
            if (points == (sizeOfBoard - 1) && strRes.charAt(0) == player) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDiagonalWinForEachPlayer (char player) {
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
        return points == (sizeOfBoard - 1) && strRes.charAt(0) == player;
    }

    private boolean checkAntiDiagonalWinForEachPlayer (char player) {
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
        return points == (sizeOfBoard - 1) && strRes.charAt(0) == player;
    }

    private boolean checkDraw () {
        for (int i = 0; i < sizeOfBoard; i++) {
            for (int j = 0; j < sizeOfBoard; j++) {
                if (board[i][j] == 'N') {
                    return false;

                }
            }
        }
        return true;
    }

    public void backToHomePage (View view) {
        Intent intent = new Intent(PlayWithAiMainPage.this, HomePage.class);
        startActivity(intent);
    }
}