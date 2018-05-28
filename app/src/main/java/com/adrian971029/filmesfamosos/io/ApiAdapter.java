package com.adrian971029.filmesfamosos.io;

import com.adrian971029.filmesfamosos.utils.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiAdapter {

    private static ApiService API_SERVICE;

    public static ApiService getApiService() {

        if (API_SERVICE == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.URL_BASE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            API_SERVICE = retrofit.create(ApiService.class);
        }

        return API_SERVICE;
    }

}
