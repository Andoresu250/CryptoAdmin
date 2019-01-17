package com.andoresu.cryptoadmin.core.purchasedetail;

import android.net.Uri;

import com.andoresu.cryptoadmin.core.purchase.data.Purchase;
import com.andoresu.cryptoadmin.utils.BaseView;

public interface PurchaseDetailContract {
    interface View extends BaseView {

        void showPurchase(Purchase purchase);
    }

    interface  UserActionsListener {

        void approvePurchase(Purchase purchase,  Uri fileUri);

        void denyPurchase(Purchase purchase);

    }
}
