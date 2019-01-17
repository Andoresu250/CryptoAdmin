package com.andoresu.cryptoadmin.utils;

import java.io.Serializable;

public class ImageResponse implements Serializable {

    public String url;
    public Thumb thumb;

    public ImageResponse(){}

    @Override
    public String toString() {
        String s = "";
        s += "url: " + url + "\n";
        s += "thumb: " + thumb + "\n";
        return s;
    }
}
