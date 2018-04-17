package com.wang.base;

/**
 * Created by 28724 on 2018/3/27.
 */

public class User {

        private String userName;
        private String passWord;
        private String familyName;

    public User(String username, String password, String familyname) {
        this.userName=username;
        this.passWord=password;
        this.familyName=familyname;
    }


    public User(String userName) {
        this.userName = userName;
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

        public String getFamilyname(){

            return familyName;
        }

        public void setFamilyname(String familyname){

            this.familyName = familyname;
        }

    }


