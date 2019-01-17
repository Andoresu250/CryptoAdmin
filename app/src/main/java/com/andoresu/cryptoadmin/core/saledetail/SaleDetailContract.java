package com.andoresu.cryptoadmin.core.saledetail;

import android.net.Uri;

import com.andoresu.cryptoadmin.core.sales.data.Sale;
import com.andoresu.cryptoadmin.utils.BaseView;

public interface SaleDetailContract {
    interface View extends BaseView {

        void showSale(Sale sale);
    }

    interface  UserActionsListener {

        void approveSale(Sale sale,  Uri fileUri);

        void denySale(Sale sale);

    }
}
