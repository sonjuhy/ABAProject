package com.example.abaproject;

import android.content.Intent;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        XmlParsing xmlParsing =new XmlParsing();

        Intent intent =getIntent();




        xmlParsing.execute("BusPosition","","차량번호");











    }
}
