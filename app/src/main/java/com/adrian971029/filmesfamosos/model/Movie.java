package com.adrian971029.filmesfamosos.model;

import java.io.Serializable;

/**
 * Created by Adrian on 23/03/2018.
 */

public class Movie implements Serializable{

    private String poster_path;
    private boolean adult;
    private String overview;
    private String release_date;
    private long id;
    private String original_title;
    private String original_language;
    private String title;
    private String backdrop_path;
    private float popularity;
    private long vote_count;
    private boolean video;
    private float vote_average;

    public Movie(){

    }

    public Movie(String poster_path, boolean adult, String overview, String release_date, long id, String original_title, String original_language, String title, String backdrop_path, float popularity, long vote_count, boolean video, float vote_average) {
        this.setPoster_path(poster_path);
        this.setAdult(adult);
        this.setOverview(overview);
        this.setRelease_date(release_date);
        this.setId(id);
        this.setOriginal_title(original_title);
        this.setOriginal_language(original_language);
        this.setTitle(title);
        this.setBackdrop_path(backdrop_path);
        this.setPopularity(popularity);
        this.setVote_count(vote_count);
        this.setVideo(video);
        this.vote_average = vote_average;
    }



    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public long getVote_count() {
        return vote_count;
    }

    public void setVote_count(long vote_count) {
        this.vote_count = vote_count;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public float getVote_average() {
        return vote_average;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }


}
