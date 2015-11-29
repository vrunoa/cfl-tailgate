package com.cfltailgate.android.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cfltailgate.android.R;

import org.json.JSONArray;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


/**
 * Created by vruno on 11/28/15.
 */
public class ChallengeActivity extends AppCompatActivity {

    private static final String TAG_NAME = "CHALLENGE_ACTIVITY";

    public static Socket socket;
    private TextView noChallengesTextView;
    private LinearLayout waitingLayout;
    private ScrollView questionsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        noChallengesTextView = (TextView) findViewById(R.id.noChallengesTextView);
        waitingLayout = (LinearLayout) findViewById(R.id.waitingLayout);
        questionsLayout = (ScrollView) findViewById(R.id.questionsLayout);

        questionsLayout.setVisibility(View.GONE);
        noChallengesTextView.setVisibility(View.VISIBLE);
        waitingLayout.setVisibility(View.GONE);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setProgress(10);

        try {
            socket = IO.socket("http://192.75.244.215:3000");

            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.i(TAG_NAME, "connected");
                }
            });

            socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.i(TAG_NAME, "disconnected");

                }
            });
            socket.on(Socket.EVENT_ERROR, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.i(TAG_NAME, "error");
                }
            });

            socket.on("launch", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.i("launch", "launch");
                    launch();
                }
            });

            socket.on("start", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.i("start", "start");
                    start();
                }
            });

            socket.connect();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void launch() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                questionsLayout.setVisibility(View.GONE);
                noChallengesTextView.setVisibility(View.GONE);
                waitingLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void start() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                questionsLayout.setVisibility(View.VISIBLE);
                noChallengesTextView.setVisibility(View.GONE);
                waitingLayout.setVisibility(View.GONE);
            }
        });

    }
}
