package com.example.abaproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    public static BusInfo Businfo;

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
        X.execute("BusLocation");
        try {
            test = m.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            m.getRegionAddress(test);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("MainActivity : "+test);
    }
}
