package com.adrian971029.filmesfamosos.io;

import com.adrian971029.filmesfamosos.BuildConfig;
import com.adrian971029.filmesfamosos.io.response.PopularResponse;
import com.adrian971029.filmesfamosos.io.response.ReviewResponse;
import com.adrian971029.filmesfamosos.io.response.TopRatedResponse;
import com.adrian971029.filmesfamosos.io.response.VideoResponse;
import com.adrian971029.filmesfamosos.utils.Constants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    public final String API_KEY = BuildConfig.api_key;

    @GET(Constants.POPULAR_MOVIE_URL +
                    API_KEY +
                    Constants.Q_LANGUAGE +
                    Constants.LANGUAGE)
    Call<PopularResponse> getPopularMovies();

    @GET(Constants.TOP_RATED_URL +
            API_KEY +
            Constants.Q_LANGUAGE +
            Constants.LANGUAGE)
    Call<TopRatedResponse> getTopRatedMovies();

    @GET(Constants.MOVIE +
            "{id}" + "/" +
            Constants.VIDEO_URL +
            API_KEY +
            Constants.Q_LANGUAGE +
            Constants.LANGUAGE)
    Call<VideoResponse> getVideos(@Path(value = "id", encoded = true) String id);

    @GET(Constants.MOVIE +
            "{id}" + "/" +
            Constants.REVIEWS_URL +
            API_KEY +
            Constants.Q_LANGUAGE +
            Constants.LANGUAGE)
    Call<ReviewResponse> getReviews(@Path(value = "id", encoded = true) String id);

}
