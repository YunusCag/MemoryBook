package com.yunuscagliyan.memorybook.adapter;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.yunuscagliyan.memorybook.listeners.Filters;

public class MemoryBookApp extends Application {
    public static void writeShared(Context context, int selectedFilter){
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putInt("filter",selectedFilter);
        editor.apply();
    }
    public static int readShared(Context context){
        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(context);
        int selectedFilter=preferences.getInt("filter", Filters.NOFILTER);
        return selectedFilter;

    }
}
