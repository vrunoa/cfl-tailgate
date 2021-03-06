package com.cfltailgate.android.activities;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cfltailgate.android.App;
import com.cfltailgate.android.R;
import com.cfltailgate.android.utils.Utils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


/**
 * Created by vruno on 11/28/15.
 */
public class ChallengeActivity extends AppCompatActivity {

    private static final String TAG_NAME = "CHALLENGE_ACTIVITY";

    public static Socket socket;
    private LinearLayout noChallengesTextView;
    private LinearLayout waitingLayout;
    private ScrollView questionsLayout;
    private LinearLayout prepareLayout;
    private boolean rigth_answer = false;
    private Dialog winDialog;
    private LinearLayout thankLayout;
    private ProgressBar pProgressBar;
    private ProgressBar qProgressBar;
    private TextView pPrepareTimeTextView;
    private TextView qPrepareTimeTextView;

    private int pointsEarned = 0;
    private ProgressBar wProgressBar;
    private TextView wPrepareTimeTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        noChallengesTextView = (LinearLayout) findViewById(R.id.noChallengesTextView);
        waitingLayout = (LinearLayout) findViewById(R.id.waitingLayout);
        questionsLayout = (ScrollView) findViewById(R.id.questionsLayout);
        prepareLayout = (LinearLayout) findViewById(R.id.prepareLayout);
        thankLayout = (LinearLayout) findViewById(R.id.thankLayout);

        pProgressBar = (ProgressBar) findViewById(R.id.pProgressBar);
        qProgressBar = (ProgressBar) findViewById(R.id.qProgressBar);
        wProgressBar = (ProgressBar) findViewById(R.id.wProgressBar);

        pPrepareTimeTextView = (TextView) findViewById(R.id.pPrepareTimeTextView);
        qPrepareTimeTextView = (TextView) findViewById(R.id.qPrepareTimeTextView);
        wPrepareTimeTextView = (TextView) findViewById(R.id.wPrepareTimeTextView);

        redraw(noChallengesTextView);

        Button backBtt = (Button) findViewById(R.id.backBtt);
        backBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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

            socket.on("prepare", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.i("prepare", "prepare");
                    prepare();
                }
            });

            socket.on("show question", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.i("show question", "show question");
                    try {
                        JSONObject obj = (JSONObject) args[0];
                        showQuestion(obj);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });

            socket.on("finish question", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.i("finish question", "finish question");
                    showResultDialog();
                }
            });

            socket.on("finish challenge", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.i("finish challenge", "finish challenge");
                    finishChallenge();
                }
            });

            socket.connect();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void finishChallenge() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try { winDialog.dismiss();}catch (Exception e){}
                TextView pointsTextView = (TextView) findViewById(R.id.pointsTextView);
                pointsTextView.setText(String.valueOf(pointsEarned));
                redraw(thankLayout);

                Button shareBtt = (Button) findViewById(R.id.shareBtt);
                shareBtt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Utils.Share(
                                ChallengeActivity.this,
                                "CFL Tailgate Challenge",
                                String.format(getResources().getString(R.string.share_points), String.valueOf(pointsEarned))
                        );
                    }
                });
            }
        });
    }

    private void showResultDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (rigth_answer) {
                    pointsEarned += 50;
                    winDialog = new Dialog(ChallengeActivity.this, R.style.fullHeightDialog);
                    View winView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.win_dialog, null, false);
                    winDialog.setContentView(winView);
                    winDialog.setCancelable(true);
                    winDialog.show();
                } else {
                    winDialog = new Dialog(ChallengeActivity.this, R.style.fullHeightDialog);
                    View winView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.lose_dialog, null, false);
                    winDialog.setContentView(winView);
                    winDialog.setCancelable(true);
                    winDialog.show();
                }
            }
        });

    }

    private void redraw(View showView) {
        questionsLayout.setVisibility(View.GONE);
        noChallengesTextView.setVisibility(View.GONE);
        waitingLayout.setVisibility(View.GONE);
        prepareLayout.setVisibility(View.GONE);
        thankLayout.setVisibility(View.GONE);

        showView.setVisibility(View.VISIBLE);
    }

    private void launch() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                redraw(waitingLayout);
                wPrepareTimeTextView.setText("10");
                new CountDownTimer(10000, 1000) {
                    @Override
                    public void onTick(long l) {
                        final int t = (int)(l/1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                wPrepareTimeTextView.setText(String.valueOf(t));
                            }
                        });
                    }

                    @Override
                    public void onFinish() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                wPrepareTimeTextView.setText(String.valueOf(0));
                            }
                        });
                    }
                }.start();
            }
        });
    }

    private void prepare() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    winDialog.dismiss();
                } catch (Exception e) {
                }
                rigth_answer = false;
                pPrepareTimeTextView.setText("3");
                redraw(prepareLayout);
                new CountDownTimer(3000, 1000) {
                    @Override
                    public void onTick(long l) {
                        final int t = (int)(l/1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pPrepareTimeTextView.setText(String.valueOf(t));
                            }
                        });
                    }

                    @Override
                    public void onFinish() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pPrepareTimeTextView.setText(String.valueOf(0));
                            }
                        });
                    }
                }.start();
            }
        });
    }

    private void showQuestion(final JSONObject question) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    qPrepareTimeTextView.setText("10");
                    new CountDownTimer(10000, 1000) {
                        @Override
                        public void onTick(long l) {
                            final int t = (int)(l/1000);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    qPrepareTimeTextView.setText(String.valueOf(t));
                                }
                            });
                        }

                        @Override
                        public void onFinish() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    qPrepareTimeTextView.setText(String.valueOf(0));
                                }
                            });
                        }
                    }.start();

                    TextView questionTextView = (TextView) findViewById(R.id.questionTextView);
                    questionTextView.setText(question.getString("q"));

                    final JSONArray answers = question.getJSONArray("a");
                    final String cId = question.getString("c");

                    final RadioButton option1 = (RadioButton) findViewById(R.id.radioButton1);
                    final JSONObject answer1 = answers.getJSONObject(0);
                    option1.setText(answer1.getString("answer"));

                    final RadioButton option2 = (RadioButton) findViewById(R.id.radioButton2);
                    final JSONObject answer2 = answers.getJSONObject(1);
                    option2.setText(answer2.getString("answer"));

                    final RadioButton option3 = (RadioButton) findViewById(R.id.radioButton3);
                    final JSONObject answer3 = answers.getJSONObject(2);
                    option3.setText(answer3.getString("answer"));

                    final RadioButton option4 = (RadioButton) findViewById(R.id.radioButton4);
                    final JSONObject answer4 = answers.getJSONObject(3);
                    option4.setText(answer4.getString("answer"));

                    option1.setEnabled(true);
                    option2.setEnabled(true);
                    option3.setEnabled(true);
                    option4.setEnabled(true);

                    option1.setChecked(false);
                    option2.setChecked(false);
                    option3.setChecked(false);
                    option4.setChecked(false);

                    Button sendButton = (Button) findViewById(R.id.sendButton);
                    sendButton.setEnabled(true);

                    sendButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            view.setEnabled(false);
                            option1.setEnabled(false);
                            option2.setEnabled(false);
                            option3.setEnabled(false);
                            option4.setEnabled(false);

                            if (option1.isChecked()) {
                                challengeAnswer(question, answer1);
                                return;
                            }
                            if (option2.isChecked()) {
                                challengeAnswer(question, answer2);
                                return;
                            }
                            if (option3.isChecked()) {
                                challengeAnswer(question, answer3);
                                return;
                            }
                            if (option4.isChecked()) {
                                challengeAnswer(question, answer4);
                                return;
                            }
                            option1.setEnabled(true);
                            option2.setEnabled(true);
                            option3.setEnabled(true);
                            option4.setEnabled(true);
                            Toast.makeText(ChallengeActivity.this, R.string.please_select_an_option, Toast.LENGTH_SHORT);
                        }
                    });

                }catch(Exception e){
                    e.printStackTrace();
                }
                redraw(questionsLayout);
            }
        });
    }

    private void challengeAnswer(JSONObject question, JSONObject answer) {
        try {
            String cId = question.getString("c");
            String aId = answer.getString("id");
            if (cId.equalsIgnoreCase(aId)) {
                rigth_answer = true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        final Dialog preloader = Utils.Preloader(ChallengeActivity.this, null, getResources().getString(R.string.sending_answer));
        App.getDataController().saveChallengeAnswer(question, answer, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                preloader.dismiss();
                if(e != null) {
                    e.printStackTrace();
                    Toast.makeText(ChallengeActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
                    return;
                }
                Toast.makeText(ChallengeActivity.this, R.string.question_saved, Toast.LENGTH_SHORT);
            }
        });
    }
}
