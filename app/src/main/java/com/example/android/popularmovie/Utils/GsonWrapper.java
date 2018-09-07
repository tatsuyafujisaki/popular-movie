package com.example.android.popularmovie.Utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public final class GsonWrapper {
    private static final Gson gson = new Gson();
    private static final JsonParser jsonParser = new JsonParser();

    public static <T> T fromJson(String json, String arrayName, Class<T> class1) {
        return gson.fromJson(getJsonArray(json, arrayName), class1);
    }

    private static JsonArray getJsonArray(String json, String arrayName) {
        return jsonParser.parse(json).getAsJsonObject().getAsJsonArray(arrayName);
    }
}