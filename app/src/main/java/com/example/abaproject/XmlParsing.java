package com.example.abaproject;

import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.example.abaproject.MainActivity.Businfo;
import static com.example.abaproject.MainActivity.AsyncTaskFinish;
/*
* 사용법
* string[0] : 받을 api 주소
* string[1] : BusLocation : Route 값, Station : Station 값, BusPosition : Route 값, BusRoute : 노선번호 값
* 순서 : BusRoute -> BusLocation -> Station
* */
public class XmlParsing extends AsyncTask<String, Void, Boolean> {
    private String result = null;
    private String line = null;
    private String BusAPIKey = "?serviceKey=8uiEDcNjEfxFOoq%2BIjRY2M7MAEKuW7AwNs9%2FyHFZUqmzm4Ci2hyvtfZdgZ7vGHBI6RjxsgBlnq%2BogcZfanSA%2Bw%3D%3D";
    private String URL_BusLoaction = "http://openapi.changwon.go.kr/rest/bis/BusLocation/";// 노선별 정류소 정보
    private String URL_BusPosition = "http://openapi.changwon.go.kr/rest/bis/BusPosition/";// 노선별 버스 위치 정보
    private String URL_Station = "http://openapi.changwon.go.kr/rest/bis/Station/";//정류소 정보
    private String URL_BusRoute = "http://openapi.changwon.go.kr/rest/bis/Bus/";//노선
    private int parserEvent = 0;
    public boolean finish = false;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        URL url;
        XmlPullParserFactory xmlPullParserFactory;
        XmlPullParser parser = null;
        String URL_String;
        String URL_String_Route = "379001000"; //기본값 100번 노선
        String URL_String_Station = null;
        String Getname = null;
        String GetText = null;
        BusStationList tmpB;
        String tmp_RouteID = null, tmp_RouteNM = null;
        int case_int = 0;
        double StationX = 0.0, StationY = 0.0;

        switch(strings[0]){
            case "BusLocation":
                if(strings.length > 1){
                    URL_String_Route = strings[1];
                }
                URL_String = URL_BusLoaction + BusAPIKey + "&route=" + URL_String_Route;
                case_int = 1;
            break;
            case "Station":
                if(strings.length > 1) {
                    URL_String_Station = strings[1];
                }
                URL_String = URL_Station + URL_String_Station + BusAPIKey;
                case_int = 2;
                break;
            case "BusRoute":
                URL_String = URL_BusRoute + BusAPIKey;
                case_int = 3;
                break;
            case "BusPosition":
                URL_String = URL_BusPosition + BusAPIKey + "&route=" + strings[1];
                case_int = 4;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + strings[0]);
        }
        try{
            url = new URL(URL_String);
            xmlPullParserFactory = XmlPullParserFactory.newInstance();
            parser = xmlPullParserFactory.newPullParser();

            parser.setInput(url.openStream(), null);

            parserEvent = parser.getEventType();
           // System.out.println("Parsing start and type :" + parser.getEventType());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        tmpB = new BusStationList();
        if(parser != null){
            //System.out.println("parser part + parsernum : " + parserEvent);
            while(parserEvent != XmlPullParser.END_DOCUMENT){
                switch(parserEvent){
                    case XmlPullParser.START_TAG:
                        Getname = parser.getName();
                        break;
                    case XmlPullParser.TEXT:
                        GetText = parser.getText();
                        if(case_int == 1) {//BusLocation
                            if (GetText != null && !(GetText.equals(""))) {
                                if (Getname != null && Getname.equals("ROUTE_ID")) {
                                    //System.out.println("route_id : " + GetText);
                                    tmpB.BusStation_Input_BusLocationPart(Integer.parseInt(GetText), 0, 0, null);
                                } else if (Getname != null && Getname.equals("ROUTE_NM")) {
                                    //System.out.println("route_nm : " + GetText);
                                    tmpB.BusStation_Input_BusLocationPart(0, Integer.parseInt(GetText), 0, null);
                                } else if (Getname != null && Getname.equals("STATION_ID")) {
                                    //System.out.println("station_id : " + GetText);
                                    tmpB.BusStation_Input_BusLocationPart(0, 0, Integer.parseInt(GetText), null);
                                } else if (Getname != null && Getname.equals("STATION_NM")) {
                                    //System.out.println("station_nm : " + GetText);
                                    tmpB.BusStation_Input_BusLocationPart(0, 0, 0, GetText);
                                    Businfo.BusInfo_Input(0,0,tmpB);
                                    tmpB = new BusStationList();
                                }
                            }
                        }
                        else if(case_int == 2){//Station
                            if(Getname != null && Getname.equals("LOCAL_X")){
                                StationX = Double.parseDouble(GetText);
                                System.out.println("Station X : " + GetText);
                            }
                            if(Getname != null && Getname.equals("LOCAL_Y")){
                                StationY = Double.parseDouble(GetText);
                                System.out.println("Station y : " + GetText);
                                int stationnm = Integer.parseInt(strings[1]);
                                for(int i=0;i<Businfo.BusInfo_Output_BusStationList().size();i++){
                                    if(Businfo.BusInfo_Output_BusStationList().get(i).BusStation_Output_StationID() == stationnm){
                                        Businfo.BusInfo_Output_BusStationList().get(i).BusStation_Input_StationPart(StationX,StationY);
                                    }
                                }
                            }
                        }
                        else if(case_int == 3){//BusRoute

                            if(Getname != null && Getname.equals("ROUTE_ID")){
                                tmp_RouteID = GetText;
                                //System.out.println("Route id : " + tmp_RouteID);
                            }
                            if(Getname != null && Getname.equals("ROUTE_NM")/* && StationNM_Check == true*/){
                                tmp_RouteNM = GetText;
                                //System.out.println("Route ID : " + tmp_RouteID + " Route_NM : " + tmp_RouteNM);
                                if(tmp_RouteNM != null && tmp_RouteNM.equals(strings[1])) {
                                    System.out.println("Route NM : " + tmp_RouteID + " string[1] : " + strings[1]);
                                    Businfo.BusInfo_Input(Integer.parseInt(tmp_RouteID), Integer.parseInt(strings[1]), null);
                                }
                            }
                        }
                        else if (case_int == 4) {
                            if (Getname != null && Getname.equals("ARRV_STATION_ID")) {
                                System.out.println("ARRV_STATION_ID : " + GetText);
                            } else if (Getname != null && Getname.equals("PLATE_NO")) {
                               System.out.println("PLATE_NO : " + GetText);
                               if (GetText.equals(strings[2])) {
                                   AsyncTaskFinish =1;
                                }
                            }
                        }
                        Getname = "";
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
        this.finish = true;
        AsyncTaskFinish = 1;
        return true;
    }
}
