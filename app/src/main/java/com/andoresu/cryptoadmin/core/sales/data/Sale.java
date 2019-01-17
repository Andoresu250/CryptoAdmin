package com.andoresu.cryptoadmin.core.sales.data;

import com.andoresu.cryptoadmin.authorization.data.Country;
import com.andoresu.cryptoadmin.authorization.data.Person;
import com.andoresu.cryptoadmin.utils.BaseObject;
import com.andoresu.cryptoadmin.utils.ImageResponse;

import static com.andoresu.cryptoadmin.utils.MyUtils.toMoney;

public class Sale extends BaseObject {

    public static final String STATE_APPROVED = "aprobado";
    public static final String STATE_DENIED = "denegado";
    public static final String STATE_PENDING = "pendiente";

    public Float btc;
    public Float value;
    public String state;
    public String walletUrl;
    public ImageResponse evidence;
    public Person person;
    public Country country;

    public boolean isPending(){
        return STATE_PENDING.equals(state);
    }

    public String getValue(){
        return toMoney(value, country.symbol);
    }

}
