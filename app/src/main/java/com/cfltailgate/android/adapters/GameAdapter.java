package com.cfltailgate.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cfltailgate.android.App;
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
        ((TextView)rowView.findViewById(R.id.home_name_textview)).setText(homeTeam);
        ((TextView)rowView.findViewById(R.id.away_name_textview)).setText(awayTeam);

        String homeId = game.getParseObject("home_team").getObjectId();
        String awayId = game.getParseObject("away_team").getObjectId();

        int homeResId = App.getLogoStore().getLogoResId(homeId);
        int awayResId = App.getLogoStore().getLogoResId(awayId);

        ((ImageView)rowView.findViewById(R.id.home_logo_imageview)).setImageResource(homeResId);
        ((ImageView)rowView.findViewById(R.id.away_logo_imageview)).setImageResource(awayResId);

        return rowView;
    }
}
