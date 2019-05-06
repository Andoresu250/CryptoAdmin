package com.andoresu.cryptoadmin.authorization.data;

import com.andoresu.cryptoadmin.utils.BaseObject;

import java.io.Serializable;

public class Country extends BaseObject implements Serializable {

    public String name;
    public String code;
    public String locale;
    public String timeZone;
    public String moneyCode;
    public String symbol;

    public Country(){}

    @Override
    public String toString() {
        return name;
    }


}
