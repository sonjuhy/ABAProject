package com.example.abaproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;

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
        X.WhereIsBus();
        //X.execute();
        //m.execute();
    }
}
