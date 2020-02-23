package com.example.abaproject;

public class BusStationList {
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
        if(StationName.equals(null)){
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
}
