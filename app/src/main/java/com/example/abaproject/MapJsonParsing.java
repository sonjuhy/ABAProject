package com.example.abaproject;


import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

public class MapJsonParsing {
    String apikey = "539c81f10377d160a0e0235df1e66207";
    URL url;

    public void mapJsonParser() {


        {
            try {
                String lcoation = URLEncoder.encode("카카오프렌즈", "UTF-8");
                url = new URL("https://dapi.kakao.com/v2/local/search/keyword.json?query =" + lcoation);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                String auth = "KakaoAK " + apikey;
                conn.setRequestMethod("GET");
                conn.setRequestProperty("X-Requested-With", "curl");
                conn.setRequestProperty("Authorization", auth);
                conn.connect();
                //InputStreamReader isr = new InputStreamReader(url.openConnection().getInputStream(), "UTF-8");
                // JSONObject jsonObject = (JSONObject)JSON
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
