package com.adrian971029.filmesfamosos.utils.network;

import com.adrian971029.filmesfamosos.model.Review;
import com.adrian971029.filmesfamosos.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class ReviewHttp {

    public static List<Review> carregarReviewJson(String id){
        try {
            HttpURLConnection conexao = HttpConnection.connection(Constants.URL_BASE +
                    Constants.MOVIE +
                    id + "/" +
                    Constants.REVIEWS_URL +
                    Constants.API_KEY +
                    Constants.Q_LANGUAGE +
                    Constants.LANGUAGE
            );

            int resposta = conexao.getResponseCode();
            if(resposta == HttpURLConnection.HTTP_OK){
                InputStream is = conexao.getInputStream();
                JSONObject jsonObject = new JSONObject(HttpConnection.byteParaString(is));
                return lerJsonReviews(jsonObject);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static List<Review> lerJsonReviews(JSONObject json) throws JSONException {
        List<Review> listReviews = new ArrayList<Review>();
        String id, author, content, url;


        JSONArray jsonResults = json.getJSONArray("results");
        for (int i = 0; i < jsonResults.length(); i++){

            JSONObject jsonId = jsonResults.getJSONObject(i);
            JSONObject jsonAuthor = jsonResults.getJSONObject(i);
            JSONObject jsonContent = jsonResults.getJSONObject(i);
            JSONObject jsonUrl = jsonResults.getJSONObject(i);

            id = jsonId.getString("id");
            author = jsonAuthor.getString("author");
            content = jsonContent.getString("content");
            url = jsonUrl.getString("url");

           Review review = new Review(
                   id,
                   author,
                   content,
                   url
           );
            listReviews.add(review);
        }
        return listReviews;
    }

}
