package com.wang.base;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 28724 on 2018/5/7.
 */

public class PersonInfo {
    private String userName;
    private String email;
    private String deviceNum;
    private String roomNum;
    private String level;
    private String area;


    public PersonInfo(String username, String email, String devicenum,String roomnum,String level) {
        this.userName=username;
        this.deviceNum=devicenum;
        this.email=email;
        this.roomNum=roomnum;
        this.level=level;
    }


    public PersonInfo(String userName) {
        this.userName = userName;
    }


    public PersonInfo() {
    }

    public String getUsername(){

        return userName;
    }

    public void setUsername(String username){

        this.userName = username;
    }

    public String getDeviceNum(){

        return deviceNum;
    }

    public void setDeviceNum(String deviceNum){

        this.deviceNum = deviceNum;
    }

    public String getEmail(){

        return email;
    }

    public void setEmail(String email){

        this.email = email;
    }
    public String getRoomNum(){

        return roomNum;
    }

    public void setRoom(String roomNum){

        this.roomNum = roomNum;
    }
    public String getLevel(){

        return level;
    }

    public void setLevel(String level){

        this.level = level;
    }
    public String getArea(){

        return area;
    }
    public void setArea(String area){

        this.area = area;
    }

}
