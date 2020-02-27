package com.example.abaproject;

import java.io.Serializable;

public class BusStationList implements Serializable {
    private int BusRouteID; //버스노선아이디
    private int BusRouteName; //버스노선번호
    private int StationID; //정류장번호
    private double Station_X; //정류장 X좌표
    private double Station_Y; //정류장 Y좌표

    private String StationName ; //정류장이름
    private String StationPlace; //정류장위치(동)

    public void BusStation_Input_BusLocationPart(int BusRouteId, int BusRouteName, int StationID, String StationName){
        if(BusRouteId != 0) {
            this.BusRouteID = BusRouteId;
        }
        if(BusRouteName != 0){
            this.BusRouteName = BusRouteName;
        }
        if(StationID != 0){
            this.StationID = StationID;
        }
        if(StationName != null){
            this.StationName = StationName;
        }
    }
    public void BusStation_Input_StationPart(double X, double Y){
        this.Station_X = X;
        this.Station_Y = Y;
    }
    public void BusStation_Input_KakaoPart(String StationPlace){
        this.StationPlace = StationPlace;
    }
    public int BusStation_Output_BusRouteID(){
        return this.BusRouteID;
    }
    public int BusStation_Output_BusRouteName(){
        return this.BusRouteName;
    }
    public int BusStation_Output_StationID(){
        return this.StationID;
    }
    public String BusStation_Output_StationName(){
        return this.StationName;
    }
    public double BusStation_Output_StationX(){
        return this.Station_X;
    }
    public double BusStation_Output_StationY(){
        return this.Station_Y;
    }
    public String BusStation_Output_StationPlace(){
        return this.StationPlace;
    }
}
class AllStation{
    private double X;
    private double Y;
    private String StationID;

    AllStation(){
        X = 0.0;
        Y = 0.0;
        StationID = null;
    }
    public void AllStation_Input(double X, double Y, String StationID){
        this.X = X;
        this.Y = Y;
        this.StationID = StationID;
    }
    public double AllStation_Output_X(){
        return this.X;
    }
    public double AllStation_Output_Y(){
        return this.Y;
    }
    public String AllStation_Output_StationID(){
        return this.StationID;
    }
}
