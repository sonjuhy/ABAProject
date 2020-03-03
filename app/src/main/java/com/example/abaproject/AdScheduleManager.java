package com.example.abaproject;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class AdScheduleManager{

    private String station_name =null;
    private BusInfo busInfo;
    private ArrayList<AdList_Schedule> adList_schedules;
    private ArrayList<String> local;
    private StringBuilder urlData = new StringBuilder("");
    private String Network_data;
    private Network n;

    public AdScheduleManager(BusInfo busInfo, ArrayList<AdList_Schedule> adList_schedules) {
        this.busInfo = busInfo;
        this.adList_schedules = adList_schedules;
        local = new ArrayList<String>();
    }


    public void local_information_storage()
    {
        int i=0;
        int j=0;
        boolean storage_permission = true;

        local.add( busInfo.BusInfo_Output_BusStationList().get(0).BusStation_Output_StationPlace());
        for(i =1 ; i < busInfo.BusInfo_Output_BusStationList().size(); i++)
        {
            for(j =0 ; j < local.size(); j++)
            {
                if(local.get(j).equals(busInfo.BusInfo_Output_BusStationList().get(i).BusStation_Output_StationPlace()))
                {
                    storage_permission =false;
                    break;
                }
            }
            if(storage_permission)
            {
                local.add(busInfo.BusInfo_Output_BusStationList().get(i).BusStation_Output_StationPlace());
            }
        }

        for(i =0 ; i < local.size(); i++)
        {
            urlData.append("AD_local[]=");
            urlData.append(local.get(i));
            urlData.append("&");
        }
    }

    private void Network_Access(String Action, String Data) {
        n = new Network();//for Using Network without AsyncTask error
        n.Input_data(Action, Data);
        try {
            Network_data = n.execute().get(); //execute Network and take return value to Network_data
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while(true){
            if(n.finish == true){  //when Network doInBackground is End
                System.out.println("Asyn finish");
                break;
            }
        }
    }

    public boolean Network_DataArrangement(){ //Setting for Network Class Value before Working Network Class
       // case "DownLoad"://Download User data part
        local_information_storage();
        Network_Access("Download", urlData.toString().substring(urlData.length()));//Running Network


        //Get_GroupData(Network_data, U);//translate JSonData from Server to Java and Save Data


        return true;//Working is Success
    }
}

