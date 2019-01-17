package com.andoresu.cryptoadmin.authorization.data;


import com.andoresu.cryptoadmin.utils.BaseObject;

import java.io.Serializable;


public abstract class Profile extends BaseObject implements Serializable{

    public String type;
    public String fullName;

    public Profile(){}

    public Profile(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        String s = "\n";
        s += ("type: " + type);
        s += ("fullName: " + fullName);
        return super.toString() + s;
    }
}