package com.andoresu.cryptoadmin.core.settingdetail.data;

import com.andoresu.cryptoadmin.authorization.data.Country;
import com.andoresu.cryptoadmin.utils.BaseObject;

import java.text.DecimalFormat;

import static com.andoresu.cryptoadmin.utils.MyUtils.parseFloat;
import static com.andoresu.cryptoadmin.utils.MyUtils.parseInt;

public class Setting extends BaseObject {

    public Float lastTradePrice;
    public Float purchasePercentage;
    public Float salePercentage;
    public Float hourVolume;
    public Integer activeTraders;
    public Float marketCap;
    public Integer dailyTransactions;
    public Integer activeAccounts;
    public Integer supportedCountries;
    public Country country;
    public String countryId;

    public Setting(){}

    public Setting(Float lastTradePrice, Float purchasePercentage, Float salePercentage, Float hourVolume, Integer activeTraders) {
        this.lastTradePrice = lastTradePrice;
        this.purchasePercentage = purchasePercentage;
        this.salePercentage = salePercentage;
        this.hourVolume = hourVolume;
        this.activeTraders = activeTraders;
    }

    public Setting(String lastTradePrice, String purchasePercentage, String salePercentage, String hourVolume, String activeTraders, String marketCap, String dailyTransactions, String activeAccounts, String supportedCountries) {
        setData(lastTradePrice, purchasePercentage, salePercentage, hourVolume, activeTraders, marketCap, dailyTransactions, activeAccounts, supportedCountries);
    }

    public void setData(String lastTradePrice, String purchasePercentage, String salePercentage, String hourVolume, String activeTraders, String marketCap, String dailyTransactions, String activeAccounts, String supportedCountries){
        this.lastTradePrice = parseFloat(lastTradePrice);
        this.purchasePercentage = parseFloat(purchasePercentage);
        this.salePercentage = parseFloat(salePercentage);
        this.hourVolume = parseFloat(hourVolume);
        this.activeTraders = parseInt(activeTraders);
        this.marketCap = parseFloat(marketCap);
        this.dailyTransactions = parseInt(dailyTransactions);
        this.activeAccounts = parseInt(activeAccounts);
        this.supportedCountries = parseInt(supportedCountries);
    }

    public String getSalePercentage(){
        return new DecimalFormat("#.##").format(this.salePercentage * 100) + "%";
    }
    public String getPurchasePercentage(){
        return new DecimalFormat("#.##").format(this.purchasePercentage * 100) + "%";
    }

    public void setCountry(Country country){
        this.country = country;
        this.countryId = country.id;
    }
}
