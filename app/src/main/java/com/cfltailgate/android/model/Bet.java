package com.cfltailgate.android.model;

/**
 * Created by julianlo on 11/28/15.
 */
public class Bet {

    public enum Result {
        Incomplete,
        True,
        False
    }

    private String _betText;
    private Result _result;

    public Bet(String betText) {
        _betText = betText;
        _result = Result.Incomplete;
    }

    public String getText() {
        return _betText;
    }

    public void setAnswer(boolean answer) {
        _result = answer ? Result.True : Result.False;
    }

    public Result getResult() {
        return _result;
    }
}
