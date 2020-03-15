package com.example.abaproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
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
    private String RouteNM, BusName;
    private SSH ssh;//for ssh connect and sftp connect
    private boolean threadcheck = false;

    private String Local_Path;
    private String folder_device;
    private String folder_server;
    private String filename;
    private String[] command;
    private File dir;

    private Button button_ok;
    private TextView Car_Num, Bus_Num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adList_schedules = new ArrayList<AdList_Schedule>();
        final Intent intent = new Intent(this, SubActivity.class);

        button_ok = findViewById(R.id.button);
        Car_Num = findViewById(R.id.editText2);
        Bus_Num = findViewById(R.id.editText3);

        Businfo = new BusInfo();

        Local_Path = Environment.getExternalStorageDirectory().getAbsolutePath();
        folder_device = Local_Path + "/ABAProject/";//route for make folder in android device
        folder_server = "var/www/ABA/g5/file/free/";//point of server folder with ad video
        filename = "/";//add filename(ad video name)
        Folder_Setting();//make folder and allow permission for use storage

        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouteNM = Bus_Num.getText().toString();
                BusName = Car_Num.getText().toString();
                System.out.println("RouteNM / BusName : "+RouteNM +" / " + BusName);
                Businfo.BusInfo_Input(0, Integer.parseInt(RouteNM), BusName,null);

                progressDialog = ProgressDialog.show(//show progressbar while download information about ABA Project
                        MainActivity.this, "Loading...", "Wait Please,,,");
                backgroundThread = new BackgroundThread();
                backgroundThread.setRunning(true);
                backgroundThread.start();//start download information
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
                Log.v("thread test", "background end");
                if (AsyncTaskFinish != 0) {
                    intent.putParcelableArrayListExtra("adList_schedules", adList_schedules);
                    intent.putExtra("Businfo", Businfo);
                    startActivity(intent);
                }
            }
        });
    }
    private void Folder_Setting(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            //if storage permission isn't allow, popup to allow selecting
            System.out.println("Storage Permission is denied");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        else{
            System.out.println("Storage Permission is allowed");
        }
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            //check existing about out storage
            System.out.println("Can use SD");
        }
        if(!dir.exists()){
            System.out.println("folder isn't exist");
            //make folder
            dir.mkdirs();
        }
        else{
            System.out.println("already exist");
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
            /*
            * write here about load AD info list
            * */
            if(!("".equals(filename))) {//Download AD video from server
                ssh = new SSH("sonjuhy.iptime.org","sonjuhy","son278298");
                ssh.execute("SFTP", folder_server, folder_device + filename);
            }
            /* use here when after make AD info list
            if(!("".equals(filename))){//Send Command to Raspberry Pi(ex : send video, show video etc)
                ssh = new SSH("sonjuhy.iptime.org","sonjuhy","son278298");
                ssh.execute("SSH", command[0]);
            }
            */
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
