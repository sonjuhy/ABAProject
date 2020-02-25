package com.example.abaproject;

import java.io.Serializable;
import java.util.ArrayList;

public class AdList_Schedule implements Serializable {

    private String StationPlace; //재생위치위치(동)
    private int[] time = new int[3];
    private  ArrayList<AdList_Information> adList_informations_0 = new ArrayList<AdList_Information>();
    private  ArrayList<AdList_Information> adList_informations_1 = new ArrayList<AdList_Information>();
    private  ArrayList<AdList_Information> adList_informations_2 = new ArrayList<AdList_Information>();

}
