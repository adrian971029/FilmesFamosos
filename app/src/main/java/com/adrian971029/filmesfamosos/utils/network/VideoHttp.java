package com.adrian971029.filmesfamosos.utils.network;

import com.adrian971029.filmesfamosos.model.Video;
import com.adrian971029.filmesfamosos.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class VideoHttp {

    public static List<Video> carregarVideoJson(String id){
        try {
            HttpURLConnection conexao = HttpConnection.connection(Constants.URL_BASE +
                    Constants.MOVIE +
                    id + "/" +
                    Constants.VIDEO_URL +
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

    public static List<Video> lerJsonMovies(JSONObject json) throws JSONException {
        List<Video> listVideo = new ArrayList<Video>();
        String id, iso_639_1, iso_3166_1, key, name, site, type;
        Integer size;


        JSONArray jsonResults = json.getJSONArray("results");
        for (int i = 0; i < jsonResults.length(); i++){

            JSONObject jsonId = jsonResults.getJSONObject(i);
            JSONObject jsonIso_639_1 = jsonResults.getJSONObject(i);
            JSONObject jsonIso_3166_1 = jsonResults.getJSONObject(i);
            JSONObject jsonKey = jsonResults.getJSONObject(i);
            JSONObject jsonName = jsonResults.getJSONObject(i);
            JSONObject jsonSite = jsonResults.getJSONObject(i);
            JSONObject jsonSize = jsonResults.getJSONObject(i);
            JSONObject jsonType = jsonResults.getJSONObject(i);

            id = jsonId.getString("id");
            iso_639_1 = jsonIso_639_1.getString("iso_639_1");
            iso_3166_1 = jsonIso_3166_1.getString("iso_3166_1");
            key = jsonKey.getString("key");
            name = jsonName.getString("name");
            site = jsonSite.getString("site");
            size = jsonSize.getInt("size");
            type= jsonType.getString("type");

            Video video = new Video(
                    id,
                    iso_639_1,
                    iso_3166_1,
                    key,
                    name,
                    site,
                    size,
                    type
            );
            listVideo.add(video);
        }
        return listVideo;
    }

}
