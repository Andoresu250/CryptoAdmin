package com.andoresu.cryptoadmin.core.purchase;

import com.andoresu.cryptoadmin.core.purchase.data.Purchase;
import com.andoresu.cryptoadmin.core.purchase.data.PurchasesResponse;
import com.andoresu.cryptoadmin.utils.BaseView;

import java.util.Map;

public interface PurchaseContract {
    interface View extends BaseView {

        void showPurchases(PurchasesResponse purchasesResponse);

    }

    interface  UserActionsListener {

        void getPurchases(Map<String, String> options);

    }

    interface InteractionListener {

        void goToPurchaseDetail(Purchase purchase);

    }
}
