package com.example.abaproject;


import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

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

    public AdScheduleManager(){

    }

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
        System.out.println(busInfo.BusInfo_Output_BusStationList().size());
        local.add( busInfo.BusInfo_Output_BusStationList().get(0).BusStation_Output_StationPlace());
        for(i =1 ; i < busInfo.BusInfo_Output_BusStationList().size(); i++)
        {
            storage_permission = true;
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
                System.out.println(busInfo.BusInfo_Output_BusStationList().get(i).BusStation_Output_StationPlace());
            }
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
                System.out.println(Network_data);
                System.out.println("Asyn finish");
                break;
            }
        }
    }
    public boolean Network_DataArrangement(String local) throws UnsupportedEncodingException { //testing
        //_param mean String[] _param

            local_information_storage();
            Network_Access("Get_AD_information", URLEncoder.encode(("AD_local"),"UTF-8") +"="+ URLEncoder.encode(local,"UTF-8"));//Running Network
        return true;//Working is Success
    }

    public boolean Network_DataArrangement() throws UnsupportedEncodingException { //Setting for Network Class Value before Working Network Class
        //_param mean String[] _param

        local_information_storage();
        for(int i =0 ; i < local.size() ; i++) {
            Network_Access("Get_AD_Information", URLEncoder.encode("AD_local"+local.get(i),"UTF-8"));
        }


     /*   if(_param != null){
            switch(_param[0]){//Frist Parameter(String)
                case "DownLoad"://Download User data part
                    Network_Access("Get_ScheduleData",U.UserID_Output());//Sending command to Network Class & Running Network
                    Get_ScheduleData(Network_data, U);//translate JSonData from Server to Java and Save Data
                    break;
            }
        }*/
        return true;//Working is Success
    }
    private void Get_ScheduleData(String mJsonString){//Parsing data(JSon to Java)
        System.out.println("mjson : "+mJsonString);

        int Sound, Vibration ,AlarmRepeatCount;
        double Place_x = 0.0, Place_y = 0.0;
        String Name, Contens, Time, Place;
        try{
            JSONObject jsonObject = new JSONObject(mJsonString);//Make object for Checking frist object data in JsonArray
            JSONArray jsonArray = jsonObject.getJSONArray(mJsonString);//Checking JSonArray

            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);//JSonArray[i] Data is moved to jsonObject1

                Sound = Integer.parseInt(jsonObject1.getString("Sound"));
                Vibration = Integer.parseInt(jsonObject1.getString("Vibration"));
                AlarmRepeatCount = Integer.parseInt(jsonObject1.getString("AlarmRepeatCount"));
                Name = jsonObject1.getString("calendarName");
                Contens = jsonObject1.getString("calendarContens");
                Time = jsonObject1.getString("calendarTime");
                Place = jsonObject1.getString("calendarPlace");

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

