package com.example.android.popularmovie.utils;

import com.example.android.popularmovie.data.Movie;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class MoviesDeserializer implements JsonDeserializer<Movie[]> {
    private final String resultsElementName;

    public MoviesDeserializer(String resultsElementName) {
        this.resultsElementName = resultsElementName;
    }

    @Override
    public Movie[] deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        return new Gson().fromJson(json.getAsJsonObject().getAsJsonArray(resultsElementName), type);
    }
}