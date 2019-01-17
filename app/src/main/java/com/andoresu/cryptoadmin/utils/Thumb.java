package com.andoresu.cryptoadmin.utils;

import java.io.Serializable;

public class Thumb implements Serializable {

    public String url;

    public Thumb(){}

    @Override
    public String toString() {
        return "url: " + url + "\n";
    }
}
