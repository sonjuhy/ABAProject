package com.example.abaproject;

import android.content.Context;
import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static com.example.abaproject.MainActivity.B;
public class XmlParsing extends AsyncTask<String, Void, String> {
    private String result = null;
    private String line = null;
    private String BusAPIKey = "?serviceKey=8uiEDcNjEfxFOoq%2BIjRY2M7MAEKuW7AwNs9%2FyHFZUqmzm4Ci2hyvtfZdgZ7vGHBI6RjxsgBlnq%2BogcZfanSA%2Bw%3D%3D";
    private String URL_BusLoaction = "http://openapi.changwon.go.kr/rest/bis/BusLocation/";// 노선별 정류소 정보
    private String URL_Station = "http://openapi.changwon.go.kr/rest/bis/Station/";//정류소 정보
    private String URL_BusRoute = "http://openapi.changwon.go.kr/rest/bis/Bus/";//노선
    private Context mContext;
    private int parserEvent = 0;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        URL url;
        XmlPullParserFactory xmlPullParserFactory;
        XmlPullParser parser = null;
        String Getname = null;
        String GetText = null;
        BusStationList tmpB = new BusStationList();
        try{
           url = new URL("http://openapi.changwon.go.kr/rest/bis/BusLocation/?serviceKey=8uiEDcNjEfxFOoq%2BIjRY2M7MAEKuW7AwNs9%2FyHFZUqmzm4Ci2hyvtfZdgZ7vGHBI6RjxsgBlnq%2BogcZfanSA%2Bw%3D%3D&route=379001000");
            xmlPullParserFactory = XmlPullParserFactory.newInstance();
            parser = xmlPullParserFactory.newPullParser();

            parser.setInput(url.openStream(), null);

            parserEvent = parser.getEventType();
            System.out.println("Parsing start and type :" + parser.getEventType());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        if(parser != null){
            System.out.println("parser part + parsernum : " + parserEvent);
            while(parserEvent != XmlPullParser.END_DOCUMENT){
                switch(parserEvent){
                    case XmlPullParser.START_TAG:
                        Getname = parser.getName();
                        break;
                    case XmlPullParser.TEXT:
                        GetText = parser.getText();
                        if(!(GetText.equals(""))) {
                            if (Getname.equals("ROUTE_ID")) {
                                System.out.println("route_id : " + GetText);
                                tmpB.BusStation_Input_BusLocationPart(Integer.parseInt(GetText),0,0,null);
                            } else if (Getname.equals("ROUTE_NM")) {
                                System.out.println("route_nm : " + GetText);
                                tmpB.BusStation_Input_BusLocationPart(0,Integer.parseInt(GetText),0,null);
                            } else if (Getname.equals("STATION_ID")) {
                                System.out.println("station_id : " + GetText);
                                tmpB.BusStation_Input_BusLocationPart(0,0,Integer.parseInt(GetText),null);
                            } else if (Getname.equals("STATION_NM")) {
                                System.out.println("station_nm : " + GetText);
                                tmpB.BusStation_Input_BusLocationPart(0,0,0,GetText);
                            }
                            Getname = "";
                            B.add(tmpB);
                        }
                        break;
                }
                try {
                    parserEvent = parser.next();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
