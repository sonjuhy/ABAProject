package com.example.abaproject;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class AdList_Schedule implements Parcelable {




    private String StationPlace; //재생위치위치(동)
    private ArrayList<Integer> adList_informations_0 ;
    private ArrayList<Integer> adList_informations_1 ;
    private ArrayList<Integer> adList_informations_2 ;

    protected AdList_Schedule(Parcel in) {
        StationPlace = in.readString();
    }
    public AdList_Schedule() {
        StationPlace =null;
        adList_informations_0 = new ArrayList<Integer>();
        adList_informations_1 = new ArrayList<Integer>();
       adList_informations_2 = new ArrayList<Integer>();
    }


    public static final Creator<AdList_Schedule> CREATOR = new Creator<AdList_Schedule>() {
        @Override
        public AdList_Schedule createFromParcel(Parcel in) {
            return new AdList_Schedule(in);
        }

        @Override
        public AdList_Schedule[] newArray(int size) {
            return new AdList_Schedule[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(StationPlace);
    }

    public String getStationPlace() {
        return StationPlace;
    }



    public ArrayList<Integer> getAdList_informations(int i) {

        if (i == 0) {
            return adList_informations_0;
        } else if (i == 1) {
            return adList_informations_1;
        } else if (i == 2) {
            return adList_informations_2;
        }
        System.out.println("Time Error");
        return null;
    }
    public void setStationPlace(String stationPlace) {
        StationPlace = stationPlace;
    }

}
