package com.example.abaproject;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class AdScheduleManager {
    private ArrayList<Integer> System_count = new ArrayList<Integer>();
    private String station_name = null;
    private BusInfo busInfo;
    private ArrayList<AdList_Schedule> adList_schedules;
    private ArrayList<String> local;
    private ArrayList<Ad_Information> adList_Information = new ArrayList<Ad_Information>();
    private StringBuilder urlData = new StringBuilder("");
    private String Network_data;
    private Network n;


    class Time_storage {
        int number;
        ArrayList<Integer> time = new ArrayList<Integer>();


        public Time_storage(int number, ArrayList<Integer> time) {
            this.number = number;
            this.time = time;
        }
    }


    public AdScheduleManager() {

    }

    public AdScheduleManager(BusInfo busInfo, ArrayList<AdList_Schedule> adList_schedules, ArrayList<Ad_Information> adList_Information) {
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

    public int getServer_time() throws JSONException {
        int server_time = -999;

        Network_Access("Get_server_time", "");//Running Network
        JSONObject jsonObject = new JSONObject(Network_data);//Make object for Checking frist object data in JsonArray
        JSONArray jsonArray = jsonObject.getJSONArray("data");//Checking JSonArray
        JSONObject jsonObject1 = jsonArray.getJSONObject(0);//JSonArray[i] Data is moved to jsonObject1
        server_time = Integer.parseInt(jsonObject1.getString("Time"));

        return server_time;
    }

    public void saveADInformaion(ArrayList<Ad_Information> adList_Information) throws JSONException, UnsupportedEncodingException {

        for (int i = 0; i < adList_Information.size(); i++) {
            Network_Access("Set_AD_Informaion", URLEncoder.encode(("count"), "UTF-8") + "=" + URLEncoder.encode(Integer.toString(adList_Information.get(i).getCount()), "UTF-8") +
                                                        "&" + URLEncoder.encode("AD_number", "UTF-8") + "=" + URLEncoder.encode(Integer.toString(adList_Information.get(i).getADnumber()), "UTF-8"))
            ;//Running Network
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
            System.out.println("test :" + local.get(i));
            Network_Access("Get_AD_information", URLEncoder.encode(("AD_local"), "UTF-8") + "=" + URLEncoder.encode(local.get(i), "UTF-8"));//Running Network
            Get_ADData(Network_data, local.get(i));//translate JSonData from Server to Java and Save Data
        }

        Get_file();
        return true;//Working is Success
    }


    private void Get_ADData(String mJsonString, String local) {//Parsing data(JSon to Java)
        Calendar calendar = Calendar.getInstance();
        int schedule_count = 0;
        int ADnumber = -9999;
        int MaxCount;
        int count;
        int time;
        int server_time = -999;
        Ad_Information temp_adList_Information;
        String ADname;
        String ADfile;
        int ADnumber_time;
        ArrayList<Integer> tempTime = null;
        AdList_Schedule tmepList = null;

        System.out.println("mjson : " + mJsonString);
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);//Make object for Checking frist object data in JsonArray
            JSONArray jsonArray = jsonObject.getJSONArray("data");//Checking JSonArray


            for (int i = 0; i < jsonArray.length(); i++) {
                System.out.println("parsing : " + mJsonString);
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);//JSonArray[i] Data is moved to jsonObject1
                ADnumber = Integer.parseInt(jsonObject1.getString("ADnumber"));
                MaxCount = Integer.parseInt(jsonObject1.getString("MaxCount"));
                count = Integer.parseInt(jsonObject1.getString("count"));
                ADname = jsonObject1.getString("ADname");
                ADfile = jsonObject1.getString("ADfile");
                /////////fileName 추가
                ////// 광고 정보

                tempTime = new ArrayList<Integer>();
                Network_Access("Get_AD_Time", URLEncoder.encode(("AD_number"), "UTF-8") + "=" + ADnumber);//Running Network
                JSONObject jsonObject_time = new JSONObject(Network_data);//Make object for Checking frist object data in JsonArray
                JSONArray jsonArray_time = jsonObject_time.getJSONArray("data");//Checking JSonArray
                ////// 광고 시간정보
                for (int j = 0; j < jsonArray_time.length(); j++) {
                    JSONObject jsonObject1_time = jsonArray_time.getJSONObject(j);//JSonArray[i] Data is moved to jsonObject1

                    time = Integer.parseInt(jsonObject1_time.getString("time"));
                    System.out.println("time_data : " + time);
                    tempTime.add(time);
                }
                if (server_time < 0) {
                    jsonArray_time = jsonObject_time.getJSONArray("server");//Checking JSonArray

                    jsonObject1 = jsonArray_time.getJSONObject(0);//JSonArray[i] Data is moved to jsonObject1
                    server_time = Integer.parseInt(jsonObject1.getString("server_time"));
                    System.out.println("server_time : " + server_time);
                }


                if (!System_count.contains(ADnumber)) {

                    temp_adList_Information = new Ad_Information(ADnumber, ADname, MaxCount, count, tempTime, local, ADfile);

                    System.out.println("---------------------" + ADnumber);
                    adList_Information.add(temp_adList_Information);

                    System_count.add(ADnumber);
                } else {
                    for (int j = 0; j < adList_Information.size(); j++) {
                        if (adList_Information.get(j).getADnumber() == ADnumber) {
                            adList_Information.get(j).getStationPlace().add(local);
                            System.out.println("******************" + ADnumber);
                        }
                    }
                }

                if (i == 0) {
                    tmepList = new AdList_Schedule();
                }
                for (int j = 0; j < tempTime.size(); j++) {
                    tmepList.getAdList_informations(tempTime.get(j) - server_time).add(ADnumber);///////////// 서버 시간으로 바꿔야됨
                }
            }
            if (tmepList != null) {
                tmepList.setStationPlace(local);
                adList_schedules.add(tmepList);
            }
        } catch (
                JSONException e) {
            e.printStackTrace();
        } catch (
                UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    private void Get_file() {
        for (int i = 0; i < adList_Information.size(); i++) {
            //SSH ssh = new SSH("sonjuhy.iptime.org","sonjuhy","son278298");
            //ssh.execute("SFTP", "folder_server", "folder_device" + adList_Information.get(i).getFileName());
        }
    }
}

