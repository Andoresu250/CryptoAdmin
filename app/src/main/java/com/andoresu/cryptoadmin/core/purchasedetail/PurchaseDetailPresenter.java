package com.andoresu.cryptoadmin.core.purchasedetail;

import android.content.Context;
import android.net.Uri;

import com.andoresu.cryptoadmin.client.ObserverResponse;
import com.andoresu.cryptoadmin.client.ServiceGenerator;
import com.andoresu.cryptoadmin.core.purchase.PurchasesService;
import com.andoresu.cryptoadmin.core.purchase.data.Purchase;
import com.andoresu.cryptoadmin.security.SecureData;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.andoresu.cryptoadmin.client.GsonBuilderUtils.getUserGson;
import static com.andoresu.cryptoadmin.utils.MyUtils.prepareFilePart;

public class PurchaseDetailPresenter implements PurchaseDetailContract.UserActionsListener{

    String TAG = "CRYPTO_" + PurchaseDetailPresenter.class.getSimpleName();

    private final PurchaseDetailContract.View purchaseDetailView;

    private final Context context;

    private final PurchasesService purchasesService;

    public PurchaseDetailPresenter(PurchaseDetailContract.View chargeDetailView, Context context) {
        this.purchaseDetailView = chargeDetailView;
        this.context = context;
        this.purchasesService = ServiceGenerator.createService(PurchasesService.class, SecureData.getToken(), getUserGson());
    }

    @Override
    public void approvePurchase(Purchase purchase, Uri fileUri) {


        MultipartBody.Part body = prepareFilePart("purchase[evidence]", fileUri, context);

        purchasesService.approve(purchase.id, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponse<Response<Purchase>>(purchaseDetailView, true) {
                    @Override
                    public void onNext(Response<Purchase> purchaseResponse) {
                        super.onNext(purchaseResponse);
                        if(purchaseResponse.isSuccessful()){
                            purchase.state = Purchase.STATE_APPROVED;
                            purchaseDetailView.showPurchase(purchaseResponse.body());
                            purchaseDetailView.showMessage("Compra aprobada exitosamente");
                        }
                    }
                });

    }

    @Override
    public void denyPurchase(Purchase charge) {
        purchasesService.deny(charge.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponse<Response<ResponseBody>>(purchaseDetailView, true) {
                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {
                        super.onNext(responseBodyResponse);
                        if(responseBodyResponse.isSuccessful()){
                            charge.state = Purchase.STATE_DENIED;
                            purchaseDetailView.showPurchase(charge);
                            purchaseDetailView.showMessage("Compra rechazada exitosamente");
                        }
                    }
                });
    }
}