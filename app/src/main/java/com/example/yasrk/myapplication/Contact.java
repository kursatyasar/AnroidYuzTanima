package com.example.yasrk.myapplication;

import java.sql.Blob;

/**
 * Created by yasrk on 14/10/2017.
 */

public class Contact {
    private int id;
    private String uname,pass;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String uname){

        this.uname=uname;
    }
    public String getUsername(){
        return this.uname;
    }

    public void setPassword(String pass){

        this.pass=pass;
    }
    public String getPassword(){
        return this.pass;

    }
}
