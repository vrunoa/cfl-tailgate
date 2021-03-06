package com.cfltailgate.android;

import android.content.Context;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONObject;

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
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Play");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                // handle the list
            }
        });

    }

    public void getGames(FindCallback<ParseObject> callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Game");
        query.include("home_team");
        query.include("away_team");

        query.findInBackground(callback);
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
        ParseObject game = new ParseObject("Game");
        game.setObjectId(gameId);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Bet");
        query.whereEqualTo("game", game);
        query.orderByAscending("tickStart");
        query.findInBackground(callback);
    }

    public void saveChallengeAnswer(JSONObject q, JSONObject a, final SaveCallback callback) {
        try {
            ParseObject userQuestion = new ParseObject("User_Question");
            ParseObject question = new ParseObject("Question");
            question.setObjectId(q.getString("id"));
            ParseObject answer = new ParseObject("Answer");
            answer.setObjectId(a.getString("id"));

            userQuestion.put("answer", answer);
            userQuestion.put("question", question);
            userQuestion.put("user", ParseUser.getCurrentUser());
            userQuestion.saveInBackground(callback);

        }catch(Exception e){
            e.printStackTrace();
            callback.done(new com.parse.ParseException(404, e.getMessage()));
        }
    }
}
