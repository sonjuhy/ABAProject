package com.example.abaproject;


import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class AdScheduleManager{

    private String station_name =null;



    public void test(BusInfo busInfo, ArrayList<AdList_Schedule> adList_schedule, int station_id)////////String 까지 받을수 있도록 수정
    {



        for (int i = 0; i < busInfo.BusInfo_Output_BusStationList().size(); i++) {
            if (busInfo.BusInfo_Output_BusStationList().get(i).BusStation_Output_StationID() == station_id) {
                station_name = busInfo.BusInfo_Output_BusStationList().get(i).BusStation_Output_StationName();
                break;
            }


        }


        if(station_name != null)
        {
            System.out.println("찾음");

        }

    }


}
