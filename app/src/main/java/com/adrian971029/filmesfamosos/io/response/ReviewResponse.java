package com.adrian971029.filmesfamosos.io.response;

import com.adrian971029.filmesfamosos.model.Movie;
import com.adrian971029.filmesfamosos.model.Review;

import java.util.ArrayList;

public class ReviewResponse {
    private ArrayList<Review> results;

    public ArrayList<Review> getResults() {
        return results;
    }

    public void setResults(ArrayList<Review> results) {
        this.results = results;
    }
}
