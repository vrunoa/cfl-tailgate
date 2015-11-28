package com.cfltailgate.android.model;

import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;

import com.cfltailgate.android.App;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by julianlo on 11/28/15.
 */
public class GameUpdater {

    public interface UpdateListener {
        void onBetResolved(Bet bet);
        void onNewPlay(String text);
        //void onScoreUpdated(int home, int away);
    }

    private String _gameId;

    private UpdateListener _listener;

    public GameUpdater(String gameId) {
        _gameId = gameId;
    }

    public void setUpdateListener(final UpdateListener listener) {
        _listener = listener;

        App.getDataController().getPlays(_gameId, new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                Handler handler = new Handler();
                for (int i = 0; i < list.size(); i++) {
                    final ParseObject play = list.get(i);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            listener.onNewPlay(play.getString("description"));
                        }
                    }, 1500 + i * 5000);
                }
            }
        });
    }


}
