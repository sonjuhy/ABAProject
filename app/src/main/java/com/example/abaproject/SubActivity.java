package com.example.abaproject;

import android.content.Intent;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import static com.example.abaproject.MainActivity.AsyncTaskFinish;


public class SubActivity extends AppCompatActivity {


    private XmlParsing xmlParsing;
    private String busStop;
    private AdScheduleManager adScheduleManager = new AdScheduleManager();
    private String station_place = null;
    private int adList_num = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        final BusInfo busInfo = (BusInfo) intent.getSerializableExtra("Businfo");
        final ArrayList<AdList_Schedule> adList_schedule = intent.getParcelableArrayListExtra("adList_schedules");


        Timer timer = new Timer();
        TimerTask TT = new TimerTask() {
            @Override
            public void run() {
                // 반복실행할 구문
                try {
                    busStop = new XmlParsing().execute("BusPosition", "379000100", "경남71자1084").get();

                    if (AsyncTaskFinish != 0) {
                        System.out.println(busStop);
                        searching_bus(busInfo, adList_schedule, Integer.parseInt(busStop));
                    }


                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        timer.schedule(TT, 0, 8000); //Timer 실행
        play_Ad(busInfo,adList_schedule);

        //  timer.cancel();//타이머 종료
    }


    public void searching_bus(BusInfo busInfo, ArrayList<AdList_Schedule> adList_schedule, int station_id)////////String 까지 받을수 있도록 수정
    {
        for (int i = 0; i < busInfo.BusInfo_Output_BusStationList().size(); i++) {

            // System.out.println(station_id);
            // System.out.println(busInfo.BusInfo_Output_BusStationList().get(i).BusStation_Output_StationID());
            if (busInfo.BusInfo_Output_BusStationList().get(i).BusStation_Output_StationID() == station_id) {


                station_place = busInfo.BusInfo_Output_BusStationList().get(i).BusStation_Output_StationPlace();
                System.out.println(station_place);
                return;
            }
        }
    }


    public void play_Ad(BusInfo busInfo, ArrayList<AdList_Schedule> adList_schedule)////////String 까지 받을수 있도록 수정
    {

        int time_count = -1;
        Calendar calendar = Calendar.getInstance();


        while (true) {

            if (adList_num < 0) {
                adList_num = searchLocal_in_adList(adList_schedule);
            } else if (!station_place.equals(adList_schedule.get(adList_num).getStationPlace()))///// 지역검사
            {
                adList_num = searchLocal_in_adList(adList_schedule);
            }////////////////////장소 확인

            if (time_count < 0)///////////////시간 확인
            {
                time_count = adList_schedule.get(adList_num).getTime(0);
            } else if (time_count < calendar.get(Calendar.HOUR_OF_DAY)) {
                time_count++;
            }


            if (true) {
                /////////////////동영상 재생
            }
        }
    }


    public int searchLocal_in_adList(ArrayList<AdList_Schedule> adList_schedule)////////String 까지 받을수 있도록 수정
    {
        for (int i = 0; i < adList_schedule.size(); i++) {
            if (adList_schedule.get(i).getStationPlace().equals(station_place)) {
                return i;
            }
        }
        return -1;
    }


}
