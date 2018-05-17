package com.adrian971029.filmesfamosos.utils.network;

import com.adrian971029.filmesfamosos.model.Movie;
import com.adrian971029.filmesfamosos.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adrian on 23/03/2018.
 */

public class MovieHttp {

    public static List<Movie> carregarMoviePopularJson(){
        try {
            HttpURLConnection conexao = HttpConnection.connection(Constants.URL_BASE +
                    Constants.POPULAR_MOVIE_URL +
                    Constants.API_KEY +
                    Constants.Q_LANGUAGE +
                    Constants.LANGUAGE
            );

            int resposta = conexao.getResponseCode();
            if(resposta == HttpURLConnection.HTTP_OK){
                InputStream is = conexao.getInputStream();
                JSONObject jsonObject = new JSONObject(HttpConnection.byteParaString(is));
                return lerJsonMovies(jsonObject);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static List<Movie> carregarMovieTopRated(){
        try {
            HttpURLConnection conexao = HttpConnection.connection(Constants.URL_BASE +
                    Constants.TOP_RATED_URL+
                    Constants.API_KEY +
                    Constants.Q_LANGUAGE +
                    Constants.LANGUAGE
            );

            int resposta = conexao.getResponseCode();
            if(resposta == HttpURLConnection.HTTP_OK){
                InputStream is = conexao.getInputStream();
                JSONObject jsonObject = new JSONObject(HttpConnection.byteParaString(is));
                return lerJsonMovies(jsonObject);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static List<Movie> lerJsonMovies(JSONObject json) throws JSONException {
        List<Movie> listMovie = new ArrayList<Movie>();
        String poster_path, overview, release_date, original_title, original_language, title, backdrop_path;
        boolean adult, video;
        long id, vote_count;
        float popularity, vote_average;


        JSONArray jsonResults = json.getJSONArray("results");
        for (int i = 0; i < jsonResults.length(); i++){

            JSONObject jsonPosterPath = jsonResults.getJSONObject(i);
            JSONObject jsonAdult = jsonResults.getJSONObject(i);
            JSONObject jsonOverview = jsonResults.getJSONObject(i);
            JSONObject jsonRelaseDate = jsonResults.getJSONObject(i);
            JSONObject jsonId = jsonResults.getJSONObject(i);
            JSONObject jsonOriginalTitle = jsonResults.getJSONObject(i);
            JSONObject jsonOriginalLanguage = jsonResults.getJSONObject(i);
            JSONObject jsonTitle = jsonResults.getJSONObject(i);
            JSONObject jsonBackdropPath = jsonResults.getJSONObject(i);
            JSONObject jsonPopularity = jsonResults.getJSONObject(i);
            JSONObject jsonVoteCount = jsonResults.getJSONObject(i);
            JSONObject jsonVideo = jsonResults.getJSONObject(i);
            JSONObject jsonVoteAverage = jsonResults.getJSONObject(i);

            poster_path = jsonPosterPath.getString("poster_path");
            adult = jsonAdult.getBoolean("adult");
            overview = jsonOverview.getString("overview");
            release_date = jsonRelaseDate.getString("release_date");
            id = jsonId.getLong("id");
            original_title = jsonOriginalTitle.getString("original_title");
            original_language = jsonOriginalLanguage.getString("original_language");
            title = jsonTitle.getString("title");
            backdrop_path = jsonBackdropPath.getString("backdrop_path");
            popularity = (float) jsonPopularity.getDouble("popularity");
            vote_count = jsonVoteCount.getLong("vote_count");
            video = jsonVideo.getBoolean("video");
            vote_average = (float) jsonVoteAverage.getDouble("vote_average");

            Movie movie = new Movie(
                    poster_path,
                    adult,
                    overview,
                    release_date,
                    id,
                    original_title,
                    original_language,
                    title,
                    backdrop_path,
                    popularity,
                    vote_count,
                    video,
                    vote_average
            );
            listMovie.add(movie);
        }
        return listMovie;
    }

}
