package com.andoresu.cryptoadmin.core.chargepointdetail;

import com.andoresu.cryptoadmin.authorization.data.Country;
import com.andoresu.cryptoadmin.core.chargepointdetail.data.ChargePointErrors;
import com.andoresu.cryptoadmin.core.chargepoints.data.ChargePoint;
import com.andoresu.cryptoadmin.utils.BaseView;

import java.util.List;

public interface ChargePointDetailContrant {

    interface View extends BaseView {

        void showChargePoint(ChargePoint chargePoint);

        void setCountries(List<Country> counties);

        void showChargePointErrors(ChargePointErrors chargePointErrors);

    }

    interface  UserActionsListener {

        void createChargePoint(ChargePoint chargePoint);

        void updateChargePoint(ChargePoint chargePoint);

        void getCountries();

    }
}
