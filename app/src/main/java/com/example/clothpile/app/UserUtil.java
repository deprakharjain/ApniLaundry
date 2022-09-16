package com.example.clothpile.app;

import java.util.ArrayList;
import java.util.Arrays;

import io.realm.RealmList;

public class UserUtil {

    public RealmList<String> clothItemsList;
    public RealmList<Integer> clothItemsPriceList, numberOfItems;
    public String roomNumber;

    public UserUtil(){
        clothItemsList = new RealmList<>("T-shirt,pants,bedsheets,towel".split(","));
        clothItemsPriceList = new RealmList<>(5, 7, 10, 8);
        numberOfItems = new RealmList<>(0,0,0,0);
        roomNumber = "VK 398 L";
    }

    public static int sumOfArray(RealmList<Integer> arrayList){
        int sum = 0;
        for(int i=0; i < arrayList.size(); i++){
            sum += arrayList.get(i);
        }
        return sum;
    }


}
