package com.adrian971029.filmesfamosos.io.response;

import com.adrian971029.filmesfamosos.model.Movie;
import com.adrian971029.filmesfamosos.model.Video;

import java.util.ArrayList;

public class VideoResponse {
    private ArrayList<Video> results;

    public ArrayList<Video> getResults() {
        return results;
    }

    public void setResults(ArrayList<Video> results) {
        this.results = results;
    }
}
