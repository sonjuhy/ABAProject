package com.example.abaproject;

import android.content.Intent;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
    private ArrayList<Ad_Information> adList_Information = new ArrayList<Ad_Information>();
    private ArrayList<AdList_Schedule> adList_schedule = new ArrayList<AdList_Schedule>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        final BusInfo busInfo = (BusInfo) intent.getSerializableExtra("Businfo");
        final String RouteNM = intent.getExtras().getString("RouteNM");///버스번호
        final String BusName = intent.getExtras().getString("BusName");///차량번호

        adScheduleManager = new AdScheduleManager(busInfo, adList_schedule, adList_Information);
        try {
            //adScheduleManager.Network_DataArrangement("반지동"); //////////testing
            adScheduleManager.Network_DataArrangement();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        System.out.println("크기 : " + adList_schedule.size());

        ////////////광고 리스트 지역별 출력
        for (int i = 0; i < adList_schedule.size(); i++) {
            System.out.println("adList_schedule 동이름 : " + adList_schedule.get(i).getStationPlace());
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
                    busStop = new XmlParsing().execute("BusPosition", sortingRouteNM(RouteNM), BusName).get();

                    if (AsyncTaskFinish != 0) {

                        searching_bus(busInfo, adList_schedule, Integer.parseInt(busStop));
                       // station_place = "신월동";/////////--------------test용!
                        System.out.println("time per 30's___station_place : " + station_place);
                    }


                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };


        timer.schedule(TT, 0, 20000); //Timer 실행
        /*try {
            while (station_place == null) {
            }
            play_Ad(adList_schedule);

        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        //  timer.cancel();//타이머 종료
    }
    public String sortingRouteNM (String RouteNM)
    {

        if(RouteNM.length() == 1)
        {
            return "3790000"+RouteNM+"0";
        }
        else if(RouteNM.length() == 2)
        {
            return "379000"+RouteNM+"0" ;
        }
        else
        {
            return "37900"+RouteNM+"0";
        }
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


    public void play_Ad(ArrayList<AdList_Schedule> adList_schedule) throws JSONException////////String 까지 받을수 있도록 수정
    {

        int Ad_List_time_count = 0;//////시간별 재생위치
        int current_time = -999;
        int temp = 0;
        int start_time = adScheduleManager.getServer_time();
        AdList_Schedule playAdList=null;

        while (station_place != null) {


            try {
                playAdList  = adList_schedule.get(searchLocal_in_adList());
            } catch (ArrayIndexOutOfBoundsException e) {
                try {
                    System.out.println("해당지역 광고 없음 ..........");
                    Thread.sleep(10000);
                    continue;
                } catch (InterruptedException e2) {

                }
            }

            ///장소확인
            if (!playAdList.getStationPlace().equals(station_place)) {
                playAdList = adList_schedule.get(searchLocal_in_adList());
            }
            ///시간확인
            temp = (adScheduleManager.getServer_time() - start_time);
            if (current_time != temp) {
                current_time = temp;
                Ad_List_time_count = 0;
                System.out.println("current_time : " + current_time);
            }

            ////// 재생
            for (int i = 0; i < this.adList_Information.size(); i++) {
                //////재생되어야할 광고번호 광고 리스트에서찾기

                if(playAdList.getAdList_informations(current_time).size() <= 0)
                {
                    try {
                        System.out.println("해당지역 시간 없음 ..........");
                        Thread.sleep(10000);
                        continue;
                    } catch (InterruptedException e2) {

                    }
                }


                else if (playAdList.getAdList_informations(current_time).get(Ad_List_time_count) == this.adList_Information.get(i).getADnumber()) {
                    //this.adList_Information.get(i).getAdvertisingpath();///동영상 경로 // 나중에 살리기

                    //////////// play
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ///////// play
                    System.out.println("play : " + this.adList_Information.get(i).getADnumber());


                    Ad_List_time_count++;
                    if (playAdList.getAdList_informations(current_time).size() <= Ad_List_time_count) {
                        Ad_List_time_count = 0;
                    }
                    break;
                    ///////////////test용 딜레이
                }
            }
        }
    }


    public int searchLocal_in_adList()////////String 까지 받을수 있도록 수정
    {
        for (int i = 0; i < adList_schedule.size(); i++) {
            if (adList_schedule.get(i).getStationPlace().equals(station_place)) {
                return i;
            }
        }
        System.out.println("**************adList_Information does not exist**********");
        return -1;
    }


}
