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
    public BusInfo Businfo;
    public static int AsyncTaskFinish = 0;
    public static ArrayList<BusStationList> busStationLists;
    public static ArrayList<AdList_Schedule> adList_schedules;

    private String RouteNM, BusName;

    private String Local_Path;
    private String folder_device;
    private String folder_server;
    private String filename;
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
        Car_Num = findViewById(R.id.editText2);///
        Bus_Num = findViewById(R.id.editText3);///

        Businfo = new BusInfo();

        Local_Path = Environment.getExternalStorageDirectory().getAbsolutePath();
        folder_device = Local_Path + "/ABAProject/";//route for make folder in android device
        folder_server = "var/www/ABA/g5/file/free/";//point of server folder with ad video
        filename = "/";//add filename(ad video name)
        dir = new File(folder_device);
        Folder_Setting();//make folder and allow permission for use storage

        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouteNM = Bus_Num.getText().toString();
                BusName = Car_Num.getText().toString();
                System.out.println("RouteNM / BusName : "+RouteNM +" / " + BusName);
                intent.putParcelableArrayListExtra("adList_schedules", adList_schedules);
                intent.putExtra("RouteNM", RouteNM);
                intent.putExtra("BusName", BusName);
                intent.putExtra("fileRoute", folder_device);
                startActivity(intent);
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
}
