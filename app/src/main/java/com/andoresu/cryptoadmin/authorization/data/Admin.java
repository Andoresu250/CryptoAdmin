package com.andoresu.cryptoadmin.authorization.data;

import java.io.Serializable;

public class Admin extends Profile implements Serializable {

    public String name;

    public Admin(){
        super(User.TYPE_ADMIN);
    }

    @Override
    public String toString() {
        String s = "\n";
        s += "name: " + name + "\n";
        return super.toString() + s;
    }

}
