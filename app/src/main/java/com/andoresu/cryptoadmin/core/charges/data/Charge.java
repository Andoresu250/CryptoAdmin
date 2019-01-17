package com.andoresu.cryptoadmin.core.charges.data;

import com.andoresu.cryptoadmin.authorization.data.Country;
import com.andoresu.cryptoadmin.authorization.data.Person;
import com.andoresu.cryptoadmin.utils.BaseObject;
import com.andoresu.cryptoadmin.utils.ImageResponse;

import static com.andoresu.cryptoadmin.utils.MyUtils.toMoney;

public class Charge extends BaseObject {

    public static final String STATE_APPROVED = "aprobado";
    public static final String STATE_DENIED = "denegado";
    public static final String STATE_PENDING = "pendiente";

    public Float amount;
    public String state;
    public ImageResponse evidence;
    public Person person;
    public Country country;

    public Charge(){}

    public String getAmount(){
        return toMoney(amount, country.symbol);
    }

    public boolean isPending() {
        return STATE_PENDING.equals(state);
    }
}
