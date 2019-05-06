package com.andoresu.cryptoadmin.core.btcchargedetail;

import android.net.Uri;

import com.andoresu.cryptoadmin.core.btccharges.data.BtcCharge;
import com.andoresu.cryptoadmin.core.charges.data.Charge;
import com.andoresu.cryptoadmin.utils.BaseView;

public interface BtcChargeDetailContract {
    interface View extends BaseView {

        void showCharge(BtcCharge charge);

    }

    interface  UserActionsListener {

        void approveCharge(BtcCharge charge, Uri fileUri);

        void denyCharge(BtcCharge charge);

        void completeCharge(BtcCharge charge);

    }
}
