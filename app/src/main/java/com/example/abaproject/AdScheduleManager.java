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

public class AdScheduleManager {

    private String station_name = null;
    private BusInfo busInfo;
    private ArrayList<AdList_Schedule> adList_schedules;
    private ArrayList<String> local;
    private ArrayList<AdList_Information> adList_Information = new ArrayList<AdList_Information>();
    private StringBuilder urlData = new StringBuilder("");
    private String Network_data;
    private Network n;


    class Time_storage
    {
        int number;
        ArrayList<Integer> time = new ArrayList<Integer>();


        public Time_storage(int number, ArrayList<Integer> time) {
            this.number = number;
            this.time = time;
        }
    }


    public AdScheduleManager() {

    }

    public AdScheduleManager(BusInfo busInfo, ArrayList<AdList_Schedule> adList_schedules,ArrayList<AdList_Information>adList_Information) {
        this.busInfo = busInfo;
        this.adList_schedules = adList_schedules;
        local = new ArrayList<String>();
        this.adList_Information = adList_Information;
    }


    public void local_information_storage() {
        int i = 0;
        int j = 0;
        boolean storage_permission = true;
        System.out.println(busInfo.BusInfo_Output_BusStationList().size());
        local.add(busInfo.BusInfo_Output_BusStationList().get(0).BusStation_Output_StationPlace());
        for (i = 1; i < busInfo.BusInfo_Output_BusStationList().size(); i++) {
            storage_permission = true;
            for (j = 0; j < local.size(); j++) {
                if (local.get(j).equals(busInfo.BusInfo_Output_BusStationList().get(i).BusStation_Output_StationPlace())) {
                    storage_permission = false;
                    break;
                }
            }
            if (storage_permission) {
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
        while (true) {
            if (n.finish == true) {  //when Network doInBackground is End
                System.out.println(Network_data);
                System.out.println("Asyn finish");
                break;
            }
        }
    }

    public boolean Network_DataArrangement(String local) throws UnsupportedEncodingException { //testing
        //_param mean String[] _param

        local_information_storage();
        Network_Access("Get_AD_information", URLEncoder.encode(("AD_local"), "UTF-8") + "=" + URLEncoder.encode(local, "UTF-8"));//Running Network
        Get_ADData(Network_data, local);//translate JSonData from Server to Java and Save Data
        return true;//Working is Success
    }

    public boolean Network_DataArrangement() throws UnsupportedEncodingException { //Setting for Network Class Value before Working Network Class
        //_param mean String[] _param

        local_information_storage();
        for (int i = 0; i < local.size(); i++) {
            Network_Access("Get_AD_information", URLEncoder.encode(("AD_local"), "UTF-8") + "=" + URLEncoder.encode(local.get(i), "UTF-8"));//Running Network
            Get_ADData(Network_data, local.get(i));//translate JSonData from Server to Java and Save Data
        }
        return true;//Working is Success
    }


    private void Get_ADData(String mJsonString, String local) {//Parsing data(JSon to Java)

        ArrayList<Integer> System_count = new ArrayList<Integer>();
        int ADnumber= -9999;
        int MaxCount;
        int count;
        int time;
        AdList_Information temp_adList_Information;
        String ADname;
        int ADnumber_time;
        ArrayList<Integer> temp= null;


        System.out.println("mjson : " + mJsonString);
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);//Make object for Checking frist object data in JsonArray
            JSONArray jsonArray = jsonObject.getJSONArray("data");//Checking JSonArray





            for (int i = 0; i < jsonArray.length(); i++) {
                System.out.println("parsing : "+mJsonString);
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);//JSonArray[i] Data is moved to jsonObject1
                ADnumber = Integer.parseInt(jsonObject1.getString("ADnumber"));
                MaxCount = Integer.parseInt(jsonObject1.getString("MaxCount"));
                count = Integer.parseInt(jsonObject1.getString("count"));
                ADname = jsonObject1.getString("ADname");

                if(!System_count.contains(ADnumber)) {
                    temp = new ArrayList<Integer>();
                    Network_Access("Get_AD_Time", URLEncoder.encode(("AD_number"), "UTF-8") + "=" + URLEncoder.encode(String.valueOf(ADnumber),"UTF-8"));//Running Network
                    JSONObject jsonObject_time = new JSONObject(Network_data);//Make object for Checking frist object data in JsonArray
                    JSONArray jsonArray_time = jsonObject_time.getJSONArray("data");//Checking JSonArray

                    for (int j = 0; j < jsonArray_time.length(); j++) {
                        JSONObject jsonObject1_time = jsonArray_time.getJSONObject(j);//JSonArray[i] Data is moved to jsonObject1
                        ADnumber = Integer.parseInt(jsonObject1_time.getString("ADnumber"));
                        time = Integer.parseInt(jsonObject1_time.getString("time"));

                        System.out.println("time_data : " + time);
                        temp.add(time);
                    }
                    System_count.add(ADnumber);
                }

                if(ADnumber > -1)
                {
                    if(!System_count.contains(ADnumber))
                    {
                        System.out.println("insert AdList_Information : " +ADnumber+ADname+MaxCount+count+temp.toString());
                        temp_adList_Information = new AdList_Information(ADnumber,ADname,MaxCount,count, temp);
                        adList_Information.add(temp_adList_Information);
                    }
                    else
                    {
                        for(int j =0 ; j<adList_Information.size();j++)
                        {
                            if(adList_Information.get(j).getADnumber() == ADnumber)
                            {
                                adList_Information.get(j).getStationPlace().add(local);
                            }
                        }
                    }
                    ADnumber =-999;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }
}

