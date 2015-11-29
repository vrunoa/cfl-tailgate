package com.cfltailgate.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.cfltailgate.android.R;
import com.parse.Parse;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vruno on 11/28/15.
 */
public class GameAdapter extends ArrayAdapter<ParseObject> {

    public GameAdapter(Context context, List<ParseObject> objects) {
        super(context, R.layout.adapter_game_item, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.adapter_game_item, parent, false);
        }

        ParseObject game = (ParseObject)getItem(position);
        String homeTeam = game.getParseObject("home_team").getString("name");
        String awayTeam = game.getParseObject("away_team").getString("name");



        return rowView;
    }
}
