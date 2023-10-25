package com.example.xo_game_java;

import java.util.Date;

public class GameHistory {

    private int gameHistoryId;
    private String playDate;
    private String result;

    //Human vs AI OR Human vs Human
    private String playType;
    private int boardSize;

    public GameHistory () {}

    public GameHistory(String playDate, String result, String playType, int boardSize) {
        this.playDate = playDate;
        this.result = result;
        this.playType = playType;
        this.boardSize = boardSize;
    }

    public GameHistory(int gameHistoryId, String playDate, String result, String playType, int boardSize) {
        this.gameHistoryId = gameHistoryId;
        this.playDate = playDate;
        this.result = result;
        this.playType = playType;
        this.boardSize = boardSize;
    }

    public int getGameHistoryId() {
        return gameHistoryId;
    }

    public void setGameHistoryId(int gameHistoryId) {
        this.gameHistoryId = gameHistoryId;
    }

    public String getPlayDate() {
        return playDate;
    }

    public void setPlayDate(String playDate) {
        this.playDate = playDate;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getPlayType() {
        return playType;
    }

    public void setPlayType(String playType) {
        this.playType = playType;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }
}
