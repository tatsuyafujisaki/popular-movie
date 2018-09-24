package com.example.android.popularmovie.Utils;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

public final class GsonWrapper {
    private static final Gson gson = new Gson();
    private static final JsonParser jsonParser = new JsonParser();

    public static <T> T fromJson(String json, String arrayName, Class<T> c) {
        return gson.fromJson(jsonParser.parse(json).getAsJsonObject().getAsJsonArray(arrayName), c);
    }
}