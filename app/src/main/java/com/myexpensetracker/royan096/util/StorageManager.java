package com.myexpensetracker.royan096.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.myexpensetracker.royan096.model.Transaction;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class StorageManager {
    private static final String PREFS = "expense_prefs";
    private static final String KEY_DATA = "transactions";

    public static void saveList(Context ctx, ArrayList<Transaction> list) {
        SharedPreferences sp = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String json = new Gson().toJson(list);
        sp.edit().putString(KEY_DATA, json).apply();
    }

    public static ArrayList<Transaction> loadList(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        String json = sp.getString(KEY_DATA, null);
        if (json == null) return new ArrayList<>();
        Type type = new TypeToken<ArrayList<Transaction>>(){}.getType();
        return new Gson().fromJson(json, type);
    }
}
