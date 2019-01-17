package com.andoresu.cryptoadmin.authorization.data;


import com.andoresu.cryptoadmin.utils.BaseObject;
import com.andoresu.cryptoadmin.utils.ImageResponse;

import java.io.Serializable;
import java.util.Date;

import static com.andoresu.cryptoadmin.utils.MyUtils.toMoney;

public class Person extends Profile implements Serializable {

    public String firstNames;
    public String lastNames;
    public String identification;
    public String phone;

    public ImageResponse identificationFront;
    public ImageResponse identificationBack;
    public ImageResponse publicReceipt;

    public Double balance;
    public DocumentType documentType;

    public Country country;

    public Person(){}

    @Override
    public String toString() {

        String s = "Person: \n";
        s += "id: " + id + "\n";
        s += "firstName: " + firstNames + "\n";
        s += "lastName: " + lastNames + "\n";
        s += "phone: " + phone + "\n";
        s += "identification: " + identification + "\n";
        return s;
    }

    public String getPhone(){
        return "+" + country.code + " " + phone;
    }

    public String getBalance(){
        return toMoney(balance, country.symbol);
    }

}
