package com.wang.base;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 28724 on 2018/3/27.
 */

public class User {

        private String userName;
        private String passWord;
        private String email;
        private ArrayList<String> room;

    public User(String username, String password, String email) {
        this.userName=username;
        this.passWord=password;
        this.email=email;
    }


    public User(String userName) {
        this.userName = userName;
    }
    public User(ArrayList<String> room) {
        this.room = room;
    }

    public User() {
    }

    public String getUsername(){

            return userName;
        }

        public void setUsername(String username){

            this.userName = username;
        }

        public String getPassword(){

            return passWord;
        }

        public void setPassword(String password){

            this.passWord = password;
        }

        public String getEmail(){

            return email;
        }

        public void setEmail(String email){

            this.email = email;
        }
         public List getRoom(){

             return room;
    }

         public void setRoom(ArrayList<String> room){

              this.room = room;
    }
    }


