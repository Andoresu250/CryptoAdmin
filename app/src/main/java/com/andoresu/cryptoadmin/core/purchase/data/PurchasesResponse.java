package com.andoresu.cryptoadmin.core.purchase.data;

import com.andoresu.cryptoadmin.utils.BaseListResponse;

import java.io.Serializable;
import java.util.List;

public class PurchasesResponse extends BaseListResponse implements Serializable {

    public List<Purchase> purchases;

}
