package com.example.abaproject;


import org.json.JSONArray;
import org.json.JSONException;
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

    private static String getRegionAddress(String jsonString) throws JSONException {
        String value = "";
        JSONObject jObj = new JSONObject(jsonString);
        JSONObject meta = (JSONObject) jObj.get("meta");
        long size = (long) meta.get("total_count");
        if(size>0){
            JSONArray jArray = (JSONArray) jObj.get("documents");
            JSONObject subJobj = (JSONObject) jArray.get(0);
            JSONObject roadAddress =  (JSONObject) subJobj.get("road_address");
            if(roadAddress == null){
                JSONObject subsubJobj = (JSONObject) subJobj.get("address");
                value = (String) subsubJobj.get("address_name");
            }else{
                value = (String) roadAddress.get("address_name");
            }
            if(value.equals("") || value==null){
                subJobj = (JSONObject) jArray.get(1);
                subJobj = (JSONObject) subJobj.get("address");
                value =(String) subJobj.get("address_name");
            }
        }
        return value;
    }

}
