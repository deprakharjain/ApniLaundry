package com.example.clothpile.app.entity;

import com.example.clothpile.app.UserUtil;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class ClothesList extends RealmObject {

    @PrimaryKey
    private String listId;

    private int totalClothes;

    private double bill;
    private RealmList<Integer> numberOfItems, priceList, itemTotalPrice;
    private RealmList<String> itemsList;


    private String roomNumber, collectionDate;

    public ClothesList(){

    }

    public RealmList<String> getItemsList() {
        return itemsList;
    }

    public RealmList<Integer> getNumberOfItems() {
        return numberOfItems;
    }

    public RealmList<Integer> getPriceList() {
        return priceList;
    }

    public void setItemsList(RealmList<String> itemsList) {
        this.itemsList = itemsList;
    }

    public ClothesList(String roomNumber, int totalClothes){
        this.roomNumber = roomNumber;
        this.totalClothes = totalClothes;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public String getRoomNumber(){
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public double getBill() {
        return bill;
    }

    public void setBill(double bill) {
        this.bill = bill;
    }

    public int getTotalClothes() {
        return totalClothes;
    }

    public void setTotalClothes() {
        totalClothes = UserUtil.sumOfArray(numberOfItems);
    }

    public void  calculateBill(){
        for(int i = 0; i < numberOfItems.size(); i++){
            //int n = priceList.get(i);
            try {
                itemTotalPrice.add(i,  numberOfItems.get(i) * priceList.get(i));
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        setBill(UserUtil.sumOfArray(itemTotalPrice));

    }

    public void setNumberOfItems(RealmList<Integer> numberOfItems) {
        this.numberOfItems = numberOfItems;
        setTotalClothes();
    }

    public void setPriceList(RealmList<Integer> priceList) {
        this.priceList = priceList;
    }

    public void setCollectionDate(String collectionDate) {
        this.collectionDate = collectionDate;
    }

    public String getCollectionDate() {
        return collectionDate;
    }
}
