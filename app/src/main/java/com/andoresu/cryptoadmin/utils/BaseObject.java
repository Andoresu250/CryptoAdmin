package com.andoresu.cryptoadmin.utils;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseObject implements Serializable {

    private static final String DATE_FORMAT_SIMPLE = "dd-MM-yyyy";
    private static final String DATE_FORMAT_FULL = "dd-MM-yyyy hh:mm";

    public String id;
    public Date createdAt;
    public Date updatedAt;

    public BaseObject(){}

    @Override
    public String toString() {
        String s = "";
        s += "id: " + id + "\n";
        s += "createdAt: " + createdAt + "\n";
        s += "updatedAt: " + updatedAt + "\n";
        return s;
    }

    @SuppressLint("SimpleDateFormat")
    public String getSimpleCreatedAt(){
        return new SimpleDateFormat(DATE_FORMAT_SIMPLE).format(createdAt);
    }

    @SuppressLint("SimpleDateFormat")
    public String getCreatedAt(){
        return new SimpleDateFormat(DATE_FORMAT_FULL).format(createdAt);
    }
}
