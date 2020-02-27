package com.example.abaproject;

import android.content.Intent;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import static com.example.abaproject.MainActivity.AsyncTaskFinish;




public class SubActivity extends AppCompatActivity {


    private XmlParsing xmlParsing;
    private String busStop;
    private AdScheduleManager adScheduleManager = new AdScheduleManager();
    private String station_name =null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent intent =getIntent();

        final BusInfo busInfo = (BusInfo) intent.getSerializableExtra("Businfo");
        final ArrayList<AdList_Schedule>  adList_schedule= intent.getParcelableArrayListExtra("adList_schedules");




        Timer timer = new Timer();
        TimerTask TT = new TimerTask() {
            @Override
            public void run() {
                // 반복실행할 구문
                try {
                    busStop =new XmlParsing().execute("BusPosition","379000100","경남71자1078").get();

                    if(AsyncTaskFinish !=0)
                    {
                        System.out.println(busStop);
                        test(busInfo, adList_schedule ,Integer.parseInt(busStop));
                    }


                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(TT, 0, 8000); //Timer 실행







      //  timer.cancel();//타이머 종료
    }


    public void test(BusInfo busInfo, ArrayList<AdList_Schedule> adList_schedule, int station_id)////////String 까지 받을수 있도록 수정
    {



        for (int i = 0 ; i < busInfo.BusInfo_Output_BusStationList().size() ; i++) {
            if (busInfo.BusInfo_Output_BusStationList().get(i).BusStation_Output_StationID() == station_id) {

                System.out.println("test");
                station_name = busInfo.BusInfo_Output_BusStationList().get(i).BusStation_Output_StationName();
                break;
            }
            System.out.println(busInfo.BusInfo_Output_BusStationList().get(i).BusStation_Output_StationID());
        }


        if(station_name != null)
        {
            //////////////////
            System.out.println("찾음");

            for (int i = 0 ; i < adList_schedule.size() ; i++) {
                if (busInfo.BusInfo_Output_BusStationList().get(i).BusStation_Output_StationID() == station_id) {

                    System.out.println("test");
                    station_name = busInfo.BusInfo_Output_BusStationList().get(i).BusStation_Output_StationName();
                    break;
                }
                System.out.println(busInfo.BusInfo_Output_BusStationList().get(i).BusStation_Output_StationID());
            }

        }

    }

}
