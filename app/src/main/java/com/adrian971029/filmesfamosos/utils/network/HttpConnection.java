package com.adrian971029.filmesfamosos.utils.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpConnection {

    protected static HttpURLConnection connection(String urlArquivo) throws IOException {
        final int SEGUNDOS = 1000;
        URL url = new URL(urlArquivo);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setReadTimeout((10*SEGUNDOS));
        connection.setConnectTimeout(15*SEGUNDOS);
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        connection.setDoOutput(false);
        connection.connect();
        return connection;
    }

    public static boolean temConexao(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    public static String byteParaString(InputStream is) throws IOException{
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream bufferzao = new ByteArrayOutputStream();
        int byteLidos;
        while((byteLidos = is.read(buffer)) != -1){
            bufferzao.write(buffer,0,byteLidos);
        }
        return  new String(bufferzao.toByteArray(),"UTF-8");
    }

}
