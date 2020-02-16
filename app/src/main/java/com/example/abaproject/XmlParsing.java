package com.example.abaproject;

import android.content.Context;
import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class XmlParsing extends AsyncTask<String, Void, XmlPullParser> {
    private String result = null;
    private String line = null;
    private Context mContext;
    private int parserEvent = 0;

    @Override
    protected XmlPullParser doInBackground(String... strings) {
        URL url;
        XmlPullParserFactory xmlPullParserFactory;
        XmlPullParser parser = null;
        String Getname = null;
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
                //System.out.println("paramEvent : "+parserEvent);
                switch(parserEvent){
                    case XmlPullParser.START_TAG:
                        System.out.println("parser.getName : " + parser.getName());
                        Getname = parser.getName();
                        if(parser.getName().equals("ROUTE_ID")){
                            System.out.println("get NAme: " + Getname);
                            Getname = parser.getName();
                        }
                        else if(parser.getName().equals("ROUTE_NM")){
                            Getname = parser.getName();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        System.out.println("parser.getText : " + parser.getText());
                        if(Getname.equals("ROUTE_ID")) {
                            System.out.println("route_id : " + parser.getText());
                        }
                        else if(Getname.equals("ROUTE_NM")) {
                            System.out.println("route_nm : " + parser.getText());
                        }
                        else if(Getname.equals("station_id")) {
                            System.out.println("station_id : " + parser.getText());
                        }
                        else if(Getname.equals("station_id")) {
                            System.out.println("station_nm : " + parser.getText());
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
        if(parser != null) {
            return parser;
        }
        else{
            return null;
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(XmlPullParser parser) {
        super.onPostExecute(parser);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
}
