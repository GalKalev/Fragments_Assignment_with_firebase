package com.example.fragmentassignment.ManagerList;

public class ManagerListModel {
    private String itemName;
    private String itemAmount;
    private int _id;

    public ManagerListModel(){

    }
    public ManagerListModel(String itemName, String itemAmount, int _id) {
        this.itemName = itemName;
        this.itemAmount = itemAmount;
        this._id = _id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(String itemAmount) {
        this.itemAmount = itemAmount;
    }

    public int get_id() {
        return _id;
    }
}
