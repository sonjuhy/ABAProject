package com.example.abaproject;


import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

public class MapJsonParsing extends AsyncTask<String, Void, String> {
    private String apikey = "539c81f10377d160a0e0235df1e66207";
    private URL url;
    private HttpURLConnection httpURLConnection;
    private OutputStream wr;
    private InputStream inputStream;

    private static String getRegionAddress(String jsonString) throws JSONException {
        String value = "";
        JSONObject jObj = new JSONObject(jsonString);
        JSONObject meta = (JSONObject) jObj.get("meta");
        //long size = (long) meta.get("total_count");
        Number n = (Number)meta.get("total_count");
        long size = n.longValue();
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

    @Override
    protected String doInBackground(String... strings) {
        String url_string = "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?";
        //String location = "query="+strings[0];
        String x, y;
        x = "x=128.6999664";
        y = "y=35.22329942";

        try {
            this.url = new URL(url_string+x+"&"+y);
            System.out.println("url string : "+url_string+x+"&"+y);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);

            httpURLConnection.setRequestProperty("Authorization","KakaoAK " + apikey);
            httpURLConnection.setRequestProperty("X-Requested-With", "curl");
            httpURLConnection.setRequestProperty("charset","UTF-8");

            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDefaultUseCaches(false);
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();

            int responseStatusCode = httpURLConnection.getResponseCode();

            if(responseStatusCode == HttpURLConnection.HTTP_OK){
                inputStream = httpURLConnection.getInputStream();
                System.out.println("Response OK");
            }
            else{
                inputStream = httpURLConnection.getErrorStream();
                System.out.println("Response Failed");
            }

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
            BufferedReader reader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while((line = reader.readLine()) != null){
                stringBuilder.append(line);
            }
            httpURLConnection.disconnect();
            System.out.println("here : "+stringBuilder.toString());

            return stringBuilder.toString();

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            getRegionAddress(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
