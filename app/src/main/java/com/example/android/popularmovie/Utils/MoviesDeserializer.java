package com.example.android.popularmovie.utils;

import com.example.android.popularmovie.data.Movie;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.LocalDate;

public class MoviesDeserializer implements JsonDeserializer<Movie[]> {
    private final String resultsElementName;

    public MoviesDeserializer(String resultsElementName) {
        this.resultsElementName = resultsElementName;
    }

    @Override
    public Movie[] deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                .create()
                .fromJson(json.getAsJsonObject().getAsJsonArray(resultsElementName), type);
    }
}