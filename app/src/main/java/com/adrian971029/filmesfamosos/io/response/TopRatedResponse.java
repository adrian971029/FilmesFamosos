package com.adrian971029.filmesfamosos.io.response;

import com.adrian971029.filmesfamosos.model.Movie;

import java.util.ArrayList;

public class TopRatedResponse {
    private ArrayList<Movie> results;

    public ArrayList<Movie> getResults() {
        return results;
    }

    public void setResults(ArrayList<Movie> results) {
        this.results = results;
    }
}
