package com.example.abaproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static BusInfo Businfo;
    public static int AsyncTaskFinish = 0;
    public static ArrayList<BusStationList> busStationLists;
    public static ArrayList<AdList_Schedule> adList_schedules;

    private ProgressDialog progressDialog;
    private BackgroundThread backgroundThread;
    private XmlParsing xmlParsing;
    private MapJsonParsing mapJsonParsing;
    private String RouteNM;
    private boolean threadcheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adList_schedules = new ArrayList<AdList_Schedule>();


        Businfo = new BusInfo();
        XmlParsing X = new XmlParsing();
        MapJsonParsing m = new MapJsonParsing();
        String test = "well...";

        System.out.println("MainActivity : " + test);
        //여기 밑에 두곳을 원하는 노선번호 고치면 한번에 다 처리됨
        Businfo.BusInfo_Input(0, 100, null);
        RouteNM = "100";
/*
        progressDialog = ProgressDialog.show(
                MainActivity.this, "Loading...", "Wait Please,,,");
        backgroundThread = new BackgroundThread();
        backgroundThread.setRunning(true);
        backgroundThread.start();

        Log.v("thread test", "background end");
        while (true) {
            if (threadcheck == true) {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }
        }

 */


        final Intent intent = new Intent(this, SubActivity.class);
        Button button1 = (Button) findViewById(R.id.button);
        button1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : click event

                intent.putExtra("adList_schedules", adList_schedules);
                intent.putExtra("Businfo", Businfo);
                startActivity(intent);
            }
        });


        System.out.println("Route Name : " + Businfo.BusInfo_Output_RouteNM());
        System.out.println("Route ID : " + Businfo.BusInfo_Output_RouteID());
        for (int i = 0; i < Businfo.BusInfo_Output_BusStationList().size(); i++) {
            System.out.println("Station Name : " + Businfo.BusInfo_Output_BusStationList().get(i).BusStation_Output_StationName());
            System.out.println("Station ID : " + Businfo.BusInfo_Output_BusStationList().get(i).BusStation_Output_StationID());
            System.out.println("Station X : " + Businfo.BusInfo_Output_BusStationList().get(i).BusStation_Output_StationX());
            System.out.println("Station Y : " + Businfo.BusInfo_Output_BusStationList().get(i).BusStation_Output_StationY());
            System.out.println("Station Place : " + Businfo.BusInfo_Output_BusStationList().get(i).BusStation_Output_StationPlace());
        }
    }

    private class BackgroundThread extends Thread {
        volatile boolean running = false;
        int cnt;

        void setRunning(boolean b) {
            running = b;
            cnt = 10;
        }

        @Override
        public void run() {
            super.run();
            BusRoute_Load(RouteNM);
            System.out.println("BusRoute Load End");
            BusLocation_Load();
            System.out.println("BusLocation Load End");
            Station_Load();
            System.out.println("Station Load End");
            StationPlace_Load();
            System.out.println("Station Place End");
            System.out.println("thread test");
            threadcheck = true;
            while (running) {
                if (threadcheck == true)
                    running = false;
                handler.sendMessage(handler.obtainMessage());
            }
        }

        private boolean BusRoute_Load(String RouteNM) {
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

        private boolean BusLocation_Load() {
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

        private boolean Station_Load() {
            xmlParsing = new XmlParsing();
            xmlParsing.execute("Station");
            while (true) {
                if (xmlParsing.finish == true) {
                    break;
                }
            }
            return true;
        }

        private void StationPlace_Load() {
            for (int i = 0; i < Businfo.BusInfo_Output_BusStationList().size(); i++) {
                mapJsonParsing = new MapJsonParsing();
                mapJsonParsing.execute(
                        Double.toString(Businfo.BusInfo_Output_BusStationList().get(i).BusStation_Output_StationX()),
                        Double.toString(Businfo.BusInfo_Output_BusStationList().get(i).BusStation_Output_StationY()),
                        Businfo.BusInfo_Output_BusStationList().get(i).BusStation_Output_StationName(), Integer.toString(i));
            }
            while (true) {
                if (mapJsonParsing.finish == true) {
                    break;
                }
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progressDialog.dismiss();

            boolean retry = true;
            while (retry) {
                try {
                    backgroundThread.join();
                    retry = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Toast.makeText(MainActivity.this, "Finish", Toast.LENGTH_LONG).show();
            }
        }
    };
}
