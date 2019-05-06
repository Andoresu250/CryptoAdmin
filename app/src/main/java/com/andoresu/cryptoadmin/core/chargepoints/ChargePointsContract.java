package com.andoresu.cryptoadmin.core.chargepoints;

import com.andoresu.cryptoadmin.authorization.data.Country;
import com.andoresu.cryptoadmin.core.chargepoints.data.ChargePoint;
import com.andoresu.cryptoadmin.core.charges.data.Charge;
import com.andoresu.cryptoadmin.core.charges.data.ChargesResponse;
import com.andoresu.cryptoadmin.utils.BaseView;

import java.util.List;
import java.util.Map;

public interface ChargePointsContract {
    interface View extends BaseView {

        void showChargePoints(List<ChargePoint> chargePoints);

        void setCountries(List<Country> countries);

    }

    interface  UserActionsListener {

        void getChargePoints(Map<String, String> options);

        void getCountries();

    }

    interface InteractionListener {

        void goToChargePoint(ChargePoint chargePoint);

    }
}
