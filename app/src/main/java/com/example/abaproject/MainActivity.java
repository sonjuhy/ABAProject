package com.example.abaproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    public static BusInfo Businfo;
    public static int AsyncTaskFinish = 0;
    public static ArrayList<BusStationList> busStationLists;
    public static ArrayList<AdInformationList> adInformationLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        busStationLists = new ArrayList<BusStationList>();
        adInformationLists = new ArrayList<AdInformationList>();


        Businfo = new BusInfo();
        XmlParsing X = new XmlParsing();
        MapJsonParsing m = new MapJsonParsing();
        String test = "well...";
        //X.execute("BusLocation");

        System.out.println("MainActivity : "+test);

        try {
            System.out.println(X.execute("BusPosition","379001000","경남71자1155").get()+"\n\n\n\n\n");/////////// 차량번호 예시 : 경남71자4015
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (true) {
            if (AsyncTaskFinish != 0) {
                break;
            }
        }
        System.out.println("MainActivity : " + test);
    }
}
