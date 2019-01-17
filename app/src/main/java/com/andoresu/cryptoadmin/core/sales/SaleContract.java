package com.andoresu.cryptoadmin.core.sales;

import com.andoresu.cryptoadmin.core.sales.data.Sale;
import com.andoresu.cryptoadmin.core.sales.data.SalesResponse;
import com.andoresu.cryptoadmin.utils.BaseView;

import java.util.Map;

public interface SaleContract {
    interface View extends BaseView {

        void showSales(SalesResponse salesResponse);

    }

    interface  UserActionsListener {

        void getSales(Map<String, String> options);

    }

    interface InteractionListener {

        void goToSaleDetail(Sale sale);

    }
}
