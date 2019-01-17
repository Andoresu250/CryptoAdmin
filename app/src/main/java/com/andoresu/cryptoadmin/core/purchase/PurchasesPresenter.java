package com.andoresu.cryptoadmin.core.purchase;

import android.content.Context;
import android.util.Log;

import com.andoresu.cryptoadmin.client.ObserverResponse;
import com.andoresu.cryptoadmin.client.ServiceGenerator;
import com.andoresu.cryptoadmin.core.purchase.data.PurchasesResponse;
import com.andoresu.cryptoadmin.security.SecureData;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.andoresu.cryptoadmin.client.GsonBuilderUtils.getUserGson;

public class PurchasesPresenter implements PurchaseContract.UserActionsListener{

    String TAG = "CRYPTO_" + PurchasesPresenter.class.getSimpleName();

    private final PurchaseContract.View chargesView;

    private final Context context;

    private final PurchasesService chargesService;

    public PurchasesPresenter(PurchaseContract.View chargesView, Context context) {
        this.chargesView = chargesView;
        this.context = context;
        this.chargesService = ServiceGenerator.createService(PurchasesService.class, SecureData.getToken(), getUserGson());
    }

    @Override
    public void getPurchases(Map<String, String> options) {
        chargesService.index(options)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponse<Response<PurchasesResponse>>(chargesView){
                    @Override
                    public void onNext(Response<PurchasesResponse> chargesResponseResponse) {
                        super.onNext(chargesResponseResponse);
                        if(chargesResponseResponse.isSuccessful()){
                            PurchasesResponse purchasesResponse = chargesResponseResponse.body();
                            chargesView.showPurchases(purchasesResponse);
                        }
                    }
                });
    }
}