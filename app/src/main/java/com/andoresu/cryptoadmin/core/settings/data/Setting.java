package com.andoresu.cryptoadmin.core.settings.data;

import com.andoresu.cryptoadmin.utils.BaseObject;

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

    public Setting(Float lastTradePrice, Float purchasePercentage, Float salePercentage, Float hourVolume, Integer activeTraders) {
        this.lastTradePrice = lastTradePrice;
        this.purchasePercentage = purchasePercentage;
        this.salePercentage = salePercentage;
        this.hourVolume = hourVolume;
        this.activeTraders = activeTraders;
    }

    public Setting(String lastTradePrice, String purchasePercentage, String salePercentage, String hourVolume, String activeTraders, String marketCap, String dailyTransactions, String activeAccounts, String supportedCountries) {
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
}
