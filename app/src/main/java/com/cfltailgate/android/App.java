package com.cfltailgate.android;

import android.app.Application;
import android.content.Context;

import com.cfltailgate.android.model.LogoStore;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

/**
 * Created by julianlo on 11/27/15.
 */
public class App extends Application {

    private static App _instance;
    private static DataController _dataController;
    private static LogoStore _logoStore;

    public App() {
        super();
        _instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "O42EnlSusAV8acWyShmFdjQywzf93LHLcn3XHX3Q", "rjWTgjijmbZJex6eiew9V4apNRIU6RulsemLcLSj");
        ParseFacebookUtils.initialize(getApplicationContext());
    }

    public static App getInstance() {
        return _instance;
    }

    public static DataController getDataController() {
        if(_dataController == null) {
            _dataController = new DataController(getInstance().getApplicationContext());
        }
        return _dataController;
    }

    public static LogoStore getLogoStore() {
        if (_logoStore == null) {
            _logoStore = new LogoStore();
        }
        return _logoStore;
    }
}
