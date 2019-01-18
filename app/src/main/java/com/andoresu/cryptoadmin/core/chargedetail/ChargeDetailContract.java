package com.andoresu.cryptoadmin.core.chargedetail;

import com.andoresu.cryptoadmin.core.charges.data.Charge;
import com.andoresu.cryptoadmin.utils.BaseView;

public interface ChargeDetailContract {
    interface View extends BaseView {

        void showCharge(Charge charge);

    }

    interface  UserActionsListener {

        void approveCharge(Charge charge);

        void denyCharge(Charge charge);

    }
}
