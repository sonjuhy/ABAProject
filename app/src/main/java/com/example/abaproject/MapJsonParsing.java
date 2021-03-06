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
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import static com.example.abaproject.SubActivity.busInfo;

public class MapJsonParsing extends AsyncTask<String, Void, String> {
    private String apikey = "539c81f10377d160a0e0235df1e66207";
    private URL url;
    private HttpURLConnection httpURLConnection;
    private OutputStream wr;
    private InputStream inputStream;
    private String StationPlace;

    public boolean finish = false;

    private static String getRegionAddress(String jsonString) throws JSONException {
        System.out.println("getRegionAddress is working");
        String value = "";
        JSONObject jObj = new JSONObject(jsonString);
        JSONObject meta = (JSONObject) jObj.get("meta");
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
                //value =(String) subJobj.get("address_name");
                value =(String) subJobj.get("region_3depth_name");
            }
        }
        System.out.println("value : " + value);
        return value;
    }
    private void Get_BusPlace(String mJsonString, String count){
        try{
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("documents");
            for(int i = 0;i <jsonArray.length(); i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                if("B".equals(jsonObject1.getString("region_type"))){
                    StationPlace = jsonObject1.getString("region_3depth_name");
                    //System.out.println("region 3 : " + StationPlace);
                    busInfo.BusInfo_Output_BusStationList().get(Integer.parseInt(count)).BusStation_Input_KakaoPart(StationPlace);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        finish = true;
    }
    @Override
    protected String doInBackground(String... strings) {//string0 = x, string1 = y, string3 = count
        String url_string = "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?";
        String x, y;
        //x = "x=128.6999664";
        //y = "y=35.22329942";
        x = "x=" + strings[0];
        y = "y=" + strings[1];
        //System.out.println("x : " + x + "y : " + y);
        try {
            this.url = new URL(url_string+x+"&"+y);
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
                //System.out.println("Response OK");
            }
            else{
                inputStream = httpURLConnection.getErrorStream();
                //System.out.println("Response Failed");
            }

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
            BufferedReader reader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while((line = reader.readLine()) != null){
                stringBuilder.append(line);
            }
            httpURLConnection.disconnect();
            //System.out.println("here : "+stringBuilder.toString());

            Get_BusPlace(stringBuilder.toString(), strings[3]);
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
        //Get_BusPlace(s);
    }
}
