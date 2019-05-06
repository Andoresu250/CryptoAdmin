package com.andoresu.cryptoadmin.core.chargepoints.data;

import com.andoresu.cryptoadmin.authorization.data.Country;
import com.andoresu.cryptoadmin.core.charges.data.Charge;
import com.andoresu.cryptoadmin.utils.BaseObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ChargePoint extends BaseObject implements Serializable {

    @SerializedName("owner")
    @Expose
    public String owner;
    @SerializedName("accountType")
    @Expose
    public String accountType;
    @SerializedName("number")
    @Expose
    public String number;
    @SerializedName("ownerIdentification")
    @Expose
    public String ownerIdentification;
    @SerializedName("iban")
    @Expose
    public String iban;

    @SerializedName("bank")
    @Expose
    public String bank;

    @SerializedName("country")
    @Expose
    public Country country;

    @SerializedName("country_id")
    @Expose
    public String countryId;

    public ChargePoint(){}



}
