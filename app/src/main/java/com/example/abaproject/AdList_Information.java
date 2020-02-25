package com.example.abaproject;

import java.util.ArrayList;
///////////////서버 광고정보 저장
public class AdList_Information {

    private String Advertisingpath; //동영상 경로
    private int MaximumPlays; //최대재생횟수
    private int count; //현재 재생횟수
    private ArrayList<Integer> time; //재생할시간
    private ArrayList<String> StationPlace; //재생위치위치(동)


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

class AdList_Information_Network{

}
