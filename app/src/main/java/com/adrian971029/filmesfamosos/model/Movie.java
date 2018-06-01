package com.adrian971029.filmesfamosos.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Adrian on 23/03/2018.
 */

public class Movie implements Parcelable{

    @SerializedName("poster_path")
    private String poster_path;
    @SerializedName("adult")
    private boolean adult;
    @SerializedName("overview")
    private String overview;
    @SerializedName("release_date")
    private String release_date;
    @SerializedName("id")
    private long id;
    @SerializedName("original_title")
    private String original_title;
    @SerializedName("title")
    private String title;
    @SerializedName("backdrop_path")
    private String backdrop_path;
    @SerializedName("vote_average")
    private float vote_average;

    public Movie() {
    }

    private Movie(Parcel in) {
        poster_path = in.readString();
        adult = in.readByte() != 0;
        overview = in.readString();
        release_date = in.readString();
        id = in.readLong();
        original_title = in.readString();
        title = in.readString();
        backdrop_path = in.readString();
        vote_average = in.readFloat();
    }

    public static final Parcelable.Creator<Movie>
            CREATOR = new Parcelable.Creator<Movie>() {

        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

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

    public float getVote_average() {
        return vote_average;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(poster_path);
        parcel.writeByte((byte)(adult ? 1 : 0));
        parcel.writeString(overview);
        parcel.writeString(release_date);
        parcel.writeLong(id);
        parcel.writeString(original_title);
        parcel.writeString(title);
        parcel.writeString(backdrop_path);
        parcel.writeFloat(vote_average);
    }
}
