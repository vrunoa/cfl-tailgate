package com.cfltailgate.android.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.parse.Parse;
import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * Created by vruno on 11/28/15.
 */
public class GameAdapter extends ArrayAdapter<ParseObject> {

    public GameAdapter(Context context, int resource, ArrayList<ParseObject> objects) {
        super(context, resource, objects);
    }
}
