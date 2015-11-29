package com.cfltailgate.android.model;

import com.cfltailgate.android.R;

import java.util.HashMap;

/**
 * Created by julianlo on 11/28/15.
 */
public class LogoStore {
    HashMap<String, Integer> _logos = new HashMap<>();

    public LogoStore() {
        initialize();
    }

    private void initialize() {
        _logos.put("4QQqXkXmME", R.drawable.edmonton);
        _logos.put("9Rgbhy5OpV", R.drawable.redblacks);
        _logos.put("TAk1uYANhK", R.drawable.argos);
        _logos.put("qk4IoqebP7", R.drawable.bc);
        _logos.put("2lSJ7Qjl6v", R.drawable.bombers);
        _logos.put("56VcyUP7UL", R.drawable.tigercats);
        _logos.put("Zo3oT48bGX", R.drawable.stampeders);
        _logos.put("BUTkPgERLZ", R.drawable.sask);
        _logos.put("IupvPQwUGJ", R.drawable.alouettes);
    }

    public int getLogoResId(String teamId) {
        return _logos.get(teamId);
    }
}
