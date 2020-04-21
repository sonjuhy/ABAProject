package com.example.abaproject;

import java.util.ArrayList;
///////////////서버 광고정보 저장
public class Ad_Information {

    private int ADnumber;
    private String name;

    private String fileName;
    private String Advertisingpath; //동영상 경로
    private int MaximumPlays; //최대재생횟수
    private int count; //현재 재생횟수
    private ArrayList<Integer> time; //재생할시간

    private ArrayList<String> StationPlace; //재생위치위치(동)
    public Ad_Information(int ADnumber, String name, int maximumPlays, int count, ArrayList<Integer> time , String StationPlace) {
        this.ADnumber = ADnumber;
        this.name = name;
        MaximumPlays = maximumPlays;
        this.count = count;
        this.time = time;
        this.StationPlace =new ArrayList<String>();
        this.StationPlace.add(StationPlace);
    }
    public Ad_Information(int ADnumber, String name, int maximumPlays, int count, ArrayList<Integer> time , String StationPlace, String fileName) {
        this.ADnumber = ADnumber;
        this.name = name;
        MaximumPlays = maximumPlays;
        this.count = count;
        this.time = time;
        this.fileName =fileName;
        this.StationPlace =new ArrayList<String>();
        this.StationPlace.add(StationPlace);
    }

    public Ad_Information() {

    }


    public void setAdvertisingpath(String advertisingpath) {
        Advertisingpath = advertisingpath;
    }
    public void setMaximumPlays(int maximumPlays) {
        MaximumPlays = maximumPlays;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public void setTime(ArrayList<Integer> time) {
        this.time = time;
    }
    public void setStationPlace(ArrayList<String> stationPlace) {
        StationPlace = stationPlace;
    }
    public void setADnumber(int ADnumber) {
        this.ADnumber = ADnumber;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }
    public int getADnumber() {
        return ADnumber;
    }
    public String getName() {
        return name;
    }
    public ArrayList<String> getStationPlace() {
        return StationPlace;
    }
    public String getAdvertisingpath() {
        return Advertisingpath;
    }
    public int getMaximumPlays() {
        return MaximumPlays;
    }
    public int getCount() {
        return count;
    }
    public ArrayList<Integer> getTime() {
        return time;
    }
}
