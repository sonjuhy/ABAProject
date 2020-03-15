package com.example.abaproject;

import android.content.Intent;

import android.os.Bundle;
import android.view.SurfaceHolder;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import static com.example.abaproject.MainActivity.AsyncTaskFinish;


public class SubActivity extends AppCompatActivity {

    private AD_player ad_player = new AD_player();
    private XmlParsing xmlParsing;
    private String busStop;
    private AdScheduleManager adScheduleManager;
    private String station_place = null;
    private int adList_num = -1;
    private ArrayList<AdList_Information> adList_Information = new ArrayList<AdList_Information>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        final BusInfo busInfo = (BusInfo) intent.getSerializableExtra("Businfo");
        final ArrayList<AdList_Schedule> adList_schedule = intent.getParcelableArrayListExtra("adList_schedules");


        adScheduleManager = new AdScheduleManager(busInfo, adList_schedule, adList_Information);
        try {
            //adScheduleManager.Network_DataArrangement("반지동"); //////////testing
            adScheduleManager.Network_DataArrangement();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        System.out.println("크기 : " + adList_schedule.size());


        for (int i = 0; i < adList_schedule.size(); i++) {
            System.out.println("동이름 : " + adList_schedule.get(i).getStationPlace());
            for (int j = 0; j < 3; j++) {
                if (adList_schedule.get(i).getAdList_informations(j) != null) {
                    for (int k = 0; k < adList_schedule.get(i).getAdList_informations(j).size(); k++)
                        System.out.println(j + " = adnumber : " + adList_schedule.get(i).getAdList_informations(j).get(k));
                }

            }
        }


        // System.out.println(adList_Information.get(0).getStationPlace().get(0));


        Timer timer = new Timer();
        TimerTask TT = new TimerTask() {
            @Override
            public void run() {
                // 반복실행할 구문
                try {
                    busStop = new XmlParsing().execute("BusPosition", "379000100", "경남71자1102").get();

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
        try {
            play_Ad(adList_schedule);
        } catch (JSONException e) {
            e.printStackTrace();
        }

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


    public void play_Ad( ArrayList<AdList_Schedule> adList_schedule) throws JSONException////////String 까지 받을수 있도록 수정
    {

        int Ad_List_time_count = 0;//////시간별 재생위치
        int current_time = -999;
        int temp = 0;
        int start_time = adScheduleManager.getServer_time();
        AdList_Schedule playAdList = adList_schedule.get(searchLocal_in_adList(adList_schedule));

        while (station_place !=null) {
            ///장소확인
            if (!playAdList.getStationPlace().equals(station_place)) {
                playAdList = adList_schedule.get(searchLocal_in_adList(adList_schedule));
            }
            ///시간확인
            temp = (adScheduleManager.getServer_time() - start_time);
            if (current_time != temp) {
                current_time = temp;
                Ad_List_time_count = 0;
            }

            ////// 재생
            for (int i = 0; i < this.adList_Information.size(); i++) {
                //////재생되어야할 광고번호 광고 리스트에서찾기
                if (playAdList.getAdList_informations(current_time).get(Ad_List_time_count) == this.adList_Information.get(i).getADnumber()) {
                    this.adList_Information.get(i).getAdvertisingpath();///동영상 경로

                    //////////// play
                    System.out.println("play : " + this.adList_Information.get(i).getADnumber());


                    Ad_List_time_count++;
                }
            }
        }
    }


    public int searchLocal_in_adList(ArrayList<AdList_Schedule> adList_schedule)////////String 까지 받을수 있도록 수정
    {
        for (int i = 0; i < adList_schedule.size(); i++) {
            if (adList_schedule.get(i).getStationPlace().equals(station_place)) {
                System.out.println("station_place : "+station_place);
                System.out.println("adList_Information : "+i);
                return i;
            }
        }
        System.out.println("adList_Information does not exist");
        return -1;
    }


}
