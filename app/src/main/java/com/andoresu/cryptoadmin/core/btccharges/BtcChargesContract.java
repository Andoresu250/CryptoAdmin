package com.andoresu.cryptoadmin.core.btccharges;

import com.andoresu.cryptoadmin.core.btccharges.data.BtcCharge;
import com.andoresu.cryptoadmin.core.btccharges.data.BtcChargesResponse;
import com.andoresu.cryptoadmin.utils.BaseView;

import java.util.Map;

public interface BtcChargesContract {
    interface View extends BaseView {

        void showCharges(BtcChargesResponse usersResponse);

    }

    interface  UserActionsListener {

        void getCharges(Map<String, String> options);

    }

    interface InteractionListener {

        void goToChargeDetail(BtcCharge btcCharge);

    }
}
