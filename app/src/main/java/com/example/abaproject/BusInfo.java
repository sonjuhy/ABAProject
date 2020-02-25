package com.example.abaproject;

import java.util.ArrayList;

public class BusInfo {
    private int RouteID;
    private int RouteNM;
    private String BusNum;
    private ArrayList<BusStationList> B;

    BusInfo(){
        RouteID = 0;
        RouteNM = 0;
        BusNum = null;
        B = new ArrayList<>();
    }
    public void BusInfo_Input(int RouteID, int RouteNM, BusStationList B){
        if(RouteNM != 0) {
            this.RouteNM = RouteNM;
        }
        if(RouteID != 0) {
            this.RouteID = RouteID;
        }
        if(B != null){
            this.B.add(B);
        }
    }
    public int BusInfo_Output_RouteID(){
        return this.RouteID;
    }
    public int BusInfo_Output_RouteNM(){
        return this.RouteNM;
    }
    public ArrayList<BusStationList> BusInfo_Output_BusStationList(){
        return this.B;
    }
}
