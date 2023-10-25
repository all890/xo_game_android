package com.example.xo_game_java;

public class GameHistoryDetails {

    private int gameHistDetailsId;
    private int rowCoordinate;
    private int columnCoordinate;
    private String player;
    private int gameHistoryId;

    public GameHistoryDetails () {}

    public GameHistoryDetails(int rowCoordinate, int columnCoordinate, String player, int gameHistoryId) {
        this.rowCoordinate = rowCoordinate;
        this.columnCoordinate = columnCoordinate;
        this.player = player;
        this.gameHistoryId = gameHistoryId;
    }

    public GameHistoryDetails(int gameHistDetailsId, int rowCoordinate, int columnCoordinate, String player, int gameHistoryId) {
        this.gameHistDetailsId = gameHistDetailsId;
        this.rowCoordinate = rowCoordinate;
        this.columnCoordinate = columnCoordinate;
        this.player = player;
        this.gameHistoryId = gameHistoryId;
    }

    public int getGameHistDetailsId() {
        return gameHistDetailsId;
    }

    public void setGameHistDetailsId(int gameHistDetailsId) {
        this.gameHistDetailsId = gameHistDetailsId;
    }

    public int getRowCoordinate() {
        return rowCoordinate;
    }

    public void setRowCoordinate(int rowCoordinate) {
        this.rowCoordinate = rowCoordinate;
    }

    public int getColumnCoordinate() {
        return columnCoordinate;
    }

    public void setColumnCoordinate(int columnCoordinate) {
        this.columnCoordinate = columnCoordinate;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getGameHistoryId() {
        return gameHistoryId;
    }

    public void setGameHistoryId(int gameHistoryId) {
        this.gameHistoryId = gameHistoryId;
    }
}
