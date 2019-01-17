package com.andoresu.cryptoadmin.core.charges;

import com.andoresu.cryptoadmin.core.charges.data.Charge;
import com.andoresu.cryptoadmin.core.charges.data.ChargesResponse;
import com.andoresu.cryptoadmin.utils.BaseView;

import java.util.Map;

public interface ChargesContract {
    interface View extends BaseView {

        void showCharges(ChargesResponse usersResponse);

    }

    interface  UserActionsListener {

        void getCharges(Map<String, String> options);

    }

    interface InteractionListener {

        void goToChargeDetail(Charge charge);

    }
}
