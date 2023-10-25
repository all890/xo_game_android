package com.example.xo_game_java;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, "XO_GAME_DB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createGameHistoryTable = "Create Table if not exists GameHistory(gameHistoryId Integer PRIMARY KEY AUTOINCREMENT, playDate Text, " +
                "result Text, playType Text, boardSize Integer)";
        sqLiteDatabase.execSQL(createGameHistoryTable);
        String createGameHistDetailsTable = "Create Table if not exists GameHistoryDetails(gameHistDetailsId Integer PRIMARY KEY AUTOINCREMENT, rowCoordinate Integer, " +
                "columnCoordinate Integer, player Text, gameHistoryId Integer, FOREIGN KEY (gameHistoryId) REFERENCES GameHistory(gameHistoryId))";
        sqLiteDatabase.execSQL(createGameHistDetailsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop Table If Exists DailyExpense");
        this.onCreate(db);
    }

    public int addGameHistory (GameHistory gameHistory) {
        long result = -1;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("playDate", gameHistory.getPlayDate());
            values.put("result", gameHistory.getResult());
            values.put("playType", gameHistory.getPlayType());
            values.put("boardSize", gameHistory.getBoardSize());

            result = db.insert("GameHistory", null, values);
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (result == -1)
            return 0;
        else
            return (int)result;
    }

    public boolean addGameHistoryDetails (GameHistoryDetails gameHistoryDetails) {
        long result = -1;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("rowCoordinate", gameHistoryDetails.getRowCoordinate());
            values.put("columnCoordinate", gameHistoryDetails.getColumnCoordinate());
            values.put("player", gameHistoryDetails.getPlayer());
            values.put("gameHistoryId", gameHistoryDetails.getGameHistoryId());

            result = db.insert("GameHistoryDetails", null, values);
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (result == -1)
            return false;
        else
            return true;
    }

    public List<GameHistory> getAllGameHistories () {
        List<GameHistory> list = new ArrayList<GameHistory>();
        SQLiteDatabase db= this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select gameHistoryId, playDate, result, playType, boardSize from GameHistory", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                GameHistory gameHistory = new GameHistory(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4));
                list.add(gameHistory);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    public List<GameHistoryDetails> getGameHistoryDetailsByGameHistoryId (int gameHistoryId) {
        List<GameHistoryDetails> list = new ArrayList<>();
        SQLiteDatabase db= this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select gameHistDetailsId, rowCoordinate, columnCoordinate, player, gameHistoryId from GameHistoryDetails where gameHistoryId = ?", new String[] {String.valueOf(gameHistoryId)});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                GameHistoryDetails gameHistoryDetails = new GameHistoryDetails(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3), cursor.getInt(4));
                list.add(gameHistoryDetails);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

    public GameHistory getGameHistoryById (int gameHistoryId) {
        GameHistory gameHistory = new GameHistory();
        SQLiteDatabase db= this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select gameHistoryId, playDate, result, playType, boardSize from GameHistory Where gameHistoryId= ?", new String[] {String.valueOf(gameHistoryId)});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                gameHistory.setGameHistoryId(cursor.getInt(0));
                gameHistory.setPlayDate(cursor.getString(1));
                gameHistory.setResult(cursor.getString(2));
                gameHistory.setPlayType(cursor.getString(3));
                gameHistory.setBoardSize(cursor.getInt(4));
            } while (cursor.moveToNext());
        }
        db.close();
        return gameHistory;
    }
}
