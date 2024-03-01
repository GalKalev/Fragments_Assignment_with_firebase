package com.example.fragmentassignment.userInfo;

import android.util.Log;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class UserInfo {
    private static Map<String, Map.Entry<String,String>> usersInfo = new HashMap<>();

//    private static int idCounter = 0;
//    private int _id;
    private String userEmail;
    private String userPassword;
    private String userPhone;
    private String userName;

    public UserInfo(){

    }

    public UserInfo(String userEmail, String userPassword, String userPhone, String userName) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userPhone = userPhone;
        this.userName = userName;
//        this._id = idCounter++;
    }

//    public int get_id() {
//        return _id;
//    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public boolean checkUserOnSignIn(){
        for(Map.Entry<String, Map.Entry<String,String>> entry : usersInfo.entrySet()){
            if(userEmail.equals(entry.getKey())){
                return false;
            }
        }
        usersInfo.put(userEmail, new AbstractMap.SimpleEntry<>(userPassword, userPhone));
        return true;
    }

    public boolean checkUserOnLogIn(){
        for(Map.Entry<String, Map.Entry<String,String>> entry : usersInfo.entrySet()){
            if(userEmail.equals(entry.getKey()) && userPassword.equals(entry.getValue().getKey())){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "UserInfo{" +

                ", userEmail='" + userEmail + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userPhone='" + userPhone + '\'' +
                '}';
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void printingAllUsersInfo(){
        for(Map.Entry<String, Map.Entry<String,String>> entry : usersInfo.entrySet()){
            Log.d("printing Info","username: " + entry.getKey());
            Log.d("printing Info","password: " + entry.getValue().getKey());
            Log.d("printing Info","phone: " + entry.getValue().getValue());

        }
        Log.d("printing Info","_________________________________________");
    }
}
