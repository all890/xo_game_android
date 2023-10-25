package com.example.xo_game_java;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends ArrayAdapter<GameHistory> {
    public ListAdapter(@NonNull Context context, List<GameHistory> gameHistoryArrayList) {
        super(context, R.layout.history_item, gameHistoryArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        GameHistory gameHistory = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.history_item, parent, false);
        }

        ImageView listImage = view.findViewById(R.id.listImage);
        TextView playDateTxt = view.findViewById(R.id.playDateTxt);
        TextView playTypeTxt = view.findViewById(R.id.playTypeTxt);
        TextView resultTxt = view.findViewById(R.id.resultTxt);
        TextView boardSizeTxt = view.findViewById(R.id.boardSizeTxt);

        if (gameHistory.getPlayType().equals("เล่นกับเพื่อน")) {
            listImage.setImageResource(R.drawable.human_circular_icon);
        } else {
            listImage.setImageResource(R.drawable.robot_circular_icon);
        }

        playDateTxt.setText("วันที่เล่น : " + gameHistory.getPlayDate());
        playTypeTxt.setText("ประเภทเกม : " + gameHistory.getPlayType());
        resultTxt.setText("ผลเกม : " + gameHistory.getResult());
        boardSizeTxt.setText("ขนาด : " + gameHistory.getBoardSize() + "x" + gameHistory.getBoardSize());

        return  view;
    }
}
