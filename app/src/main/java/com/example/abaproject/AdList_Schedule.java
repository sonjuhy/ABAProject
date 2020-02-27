package com.example.abaproject;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class AdList_Schedule implements Parcelable {

    private String StationPlace; //재생위치위치(동)
    private int[] time = new int[3];
    private  ArrayList<AdList_Information> adList_informations_0 = new ArrayList<AdList_Information>();
    private  ArrayList<AdList_Information> adList_informations_1 = new ArrayList<AdList_Information>();
    private  ArrayList<AdList_Information> adList_informations_2 = new ArrayList<AdList_Information>();

    protected AdList_Schedule(Parcel in) {
        StationPlace = in.readString();
        time = in.createIntArray();
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
        parcel.writeIntArray(time);
    }
}
