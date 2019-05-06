package com.andoresu.cryptoadmin.core.btccharges.data;

import android.util.Log;

import com.andoresu.cryptoadmin.authorization.data.Country;
import com.andoresu.cryptoadmin.authorization.data.Person;
import com.andoresu.cryptoadmin.utils.BaseObject;
import com.andoresu.cryptoadmin.utils.ImageResponse;

import static com.andoresu.cryptoadmin.utils.MyUtils.toMoney;

public class BtcCharge extends BaseObject {

    public static final String STATE_PENDING = "pendiente";
    public static final String STATE_ACCEPTED = "aceptado";
    public static final String STATE_SUCCESSFUL = "exitoso";
    public static final String STATE_TO_VALIDATE = "validando";
    public static final String STATE_DENIED = "denegado";

    public String btc;
    public String state;
    public ImageResponse evidence;
    public ImageResponse qr;
    public Person person;
    public Country country;

    public BtcCharge(){}

    public String getAmount(){
        return toMoney(btc, "Ƀ");
    }

    public boolean isPending() {
        return STATE_PENDING.equals(state);
    }
    public boolean isAccepted(){
        return STATE_ACCEPTED.equals(state);
    }
    public boolean isChecked(){
        return STATE_TO_VALIDATE.equals(state);
    }

    public boolean canDeny(){
        return isPending() || isAccepted() || isChecked();
    }
    public boolean canApprove(){
        return isPending();
    }
    public boolean canComplete(){
        return isChecked();
    }

}
