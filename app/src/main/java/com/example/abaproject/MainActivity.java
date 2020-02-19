package com.example.abaproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        XmlParsing X = new XmlParsing();
        MapJsonParsing m = new MapJsonParsing();
        m.mapJsonParser();;
        //X.execute();
    }
}
