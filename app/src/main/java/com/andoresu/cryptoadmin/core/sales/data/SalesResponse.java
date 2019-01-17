package com.andoresu.cryptoadmin.core.sales.data;

import com.andoresu.cryptoadmin.utils.BaseListResponse;

import java.io.Serializable;
import java.util.List;

public class SalesResponse extends BaseListResponse implements Serializable {

    public List<Sale> sales;

}
