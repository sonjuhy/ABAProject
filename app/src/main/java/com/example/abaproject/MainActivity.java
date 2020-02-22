package com.example.abaproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static ArrayList<BusStationList> B;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        B = new ArrayList<>();
        XmlParsing X = new XmlParsing();
        MapJsonParsing m = new MapJsonParsing();
        X.execute();
        //m.execute();
    }
}
