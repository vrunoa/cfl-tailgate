package com.cfltailgate.android;

import android.content.Context;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by julianlo on 11/27/15.
 */
public class DataController {

    private Context _context;

    public DataController(Context context) {
        _context = context;
    }

    public void foo() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Team");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                // handle the list
            }
        });
    }

    public void getPlays(String gameId, FindCallback<ParseObject> callback) {
        ParseObject game = new ParseObject("Game");
        game.setObjectId(gameId);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Play");
        query.whereEqualTo("game", game);
        query.orderByAscending("tick");
        query.findInBackground(callback);
    }

    public void getBets(String gameId, FindCallback<ParseObject> callback) {

    }
}
