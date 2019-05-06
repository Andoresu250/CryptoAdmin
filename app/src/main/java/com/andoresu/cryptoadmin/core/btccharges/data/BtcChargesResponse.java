package com.andoresu.cryptoadmin.core.btccharges.data;


import com.andoresu.cryptoadmin.utils.BaseListResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BtcChargesResponse extends BaseListResponse implements Serializable {

    public List<BtcCharge> charges = new ArrayList<>();

}
