package com.example.abaproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    public static BusInfo Businfo;
    public static int AsyncTaskFinish = 0;
    public static ArrayList<BusStationList> busStationLists;
    public static ArrayList<AdInformationList> adInformationLists;

    private ProgressDialog progressDialog;
    private BackgroundThread backgroundThread;
    private XmlParsing xmlParsing;
    private String RouteNM;
    private boolean check[] = new boolean[4], threadcheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        busStationLists = new ArrayList<BusStationList>();
        adInformationLists = new ArrayList<AdInformationList>();


        Businfo = new BusInfo();
        XmlParsing X = new XmlParsing();
        MapJsonParsing m = new MapJsonParsing();
        for(int i=0; i<4;i++) {
            check[i] = true;
        }
        String test = "well...";

        System.out.println("MainActivity : "+test);
        Businfo.BusInfo_Input(0,100,null);
        RouteNM = "100";

        progressDialog = ProgressDialog.show(
                MainActivity.this, "Loading...","Wait Please,,,");
        backgroundThread = new BackgroundThread();
        backgroundThread.setRunning(true);
        backgroundThread.start();

        Log.v("thread test","background end");
        while(true){
            if(threadcheck == true){
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        System.out.println("Route Name : "+Businfo.BusInfo_Output_RouteNM());
        System.out.println("Route ID : "+Businfo.BusInfo_Output_RouteID());
        /*for(int i=0;i<Businfo.BusInfo_Output_BusStationList().size();i++){
            /*
            * private int BusRouteID; //버스노선아이디
    private int BusRouteName; //버스노선번호
    private int StationID; //정류장번호
    private double Station_X; //정류장 X좌표
    private double Station_Y; //정류장 Y좌표

    private String StationName ; //정류장이름
    private String StationPlace; //정류장위치(동)*/
            /*System.out.println("Station Name : "+Businfo.BusInfo_Output_BusStationList().get(i).BusStation_Output_StationName());
            System.out.println("Station ID : "+Businfo.BusInfo_Output_BusStationList().get(i).BusStation_Output_StationID());
            System.out.println("Station X : "+Businfo.BusInfo_Output_BusStationList().get(i).BusStation_Output_StationX());
            System.out.println("Station Y : "+Businfo.BusInfo_Output_BusStationList().get(i).BusStation_Output_StationY());
        }*/
    }
    private class BackgroundThread extends Thread{
        volatile boolean running = false;
        int cnt;
        void setRunning(boolean b){
            running = b;
            cnt = 10;
        }

        @Override
        public void run() {
            super.run();
            BusRoute_Load(RouteNM);
            BusLocation_Load();
            for(int i = 0; i < Businfo.BusInfo_Output_BusStationList().size(); i++) {
                Station_Load(i);
            }
            System.out.println("thread test");
            threadcheck = true;
            while(running){
                if(threadcheck == true)
                    running = false;
                handler.sendMessage(handler.obtainMessage());
            }
        }
        private boolean BusRoute_Load(String RouteNM){
            xmlParsing = new XmlParsing();
            System.out.println("BusRoute : " + RouteNM);
            xmlParsing.execute("BusRoute", RouteNM);
            while (true) {
                if (xmlParsing.finish == true) {
                    break;
                }
            }
            return true;
        }
        private boolean BusLocation_Load(){
            xmlParsing = new XmlParsing();
            System.out.println("BusLocation part");
            xmlParsing.execute("BusLocation", Integer.toString(Businfo.BusInfo_Output_RouteID()));
            while (true) {
                if (xmlParsing.finish == true) {
                    break;
                }
            }
            return true;

        }
        private boolean Station_Load(int i){
            xmlParsing = new XmlParsing();
            xmlParsing.execute("Station", Integer.toString(Businfo.BusInfo_Output_BusStationList().get(i).BusStation_Output_StationID()));
            while (true) {
                if (xmlParsing.finish == true) {
                    break;
                }
            }
            return true;
        }
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progressDialog.dismiss();

            boolean retry = true;
            while(retry){
                try {
                    backgroundThread.join();
                    retry = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Toast.makeText(MainActivity.this,"Finish",Toast.LENGTH_LONG).show();
            }
        }
    };
}
