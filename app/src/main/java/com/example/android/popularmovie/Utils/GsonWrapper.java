package com.example.android.popularmovie.Utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public final class GsonWrapper {
    private static final Gson gson = new Gson();
    private static final JsonParser jsonParser = new JsonParser();

    public static <T> T fromJson(JsonElement je, Class<T> c) {
        return gson.fromJson(je, c);
    }

    public static JsonArray getJsonArray(String json, String arrayName) {
        return jsonParser.parse(json).getAsJsonObject().getAsJsonArray(arrayName);
    }
}