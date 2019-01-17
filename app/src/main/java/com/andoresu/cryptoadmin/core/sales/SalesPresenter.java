package com.andoresu.cryptoadmin.core.sales;

import android.content.Context;

import com.andoresu.cryptoadmin.client.ObserverResponse;
import com.andoresu.cryptoadmin.client.ServiceGenerator;
import com.andoresu.cryptoadmin.core.sales.data.SalesResponse;
import com.andoresu.cryptoadmin.security.SecureData;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.andoresu.cryptoadmin.client.GsonBuilderUtils.getUserGson;

public class SalesPresenter implements SaleContract.UserActionsListener{

    String TAG = "CRYPTO_" + SalesPresenter.class.getSimpleName();

    private final SaleContract.View chargesView;

    private final Context context;

    private final SalesService chargesService;

    public SalesPresenter(SaleContract.View chargesView, Context context) {
        this.chargesView = chargesView;
        this.context = context;
        this.chargesService = ServiceGenerator.createService(SalesService.class, SecureData.getToken(), getUserGson());
    }

    @Override
    public void getSales(Map<String, String> options) {
        chargesService.index(options)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponse<Response<SalesResponse>>(chargesView){
                    @Override
                    public void onNext(Response<SalesResponse> chargesResponseResponse) {
                        super.onNext(chargesResponseResponse);
                        if(chargesResponseResponse.isSuccessful()){
                            SalesResponse salesResponse = chargesResponseResponse.body();
                            chargesView.showSales(salesResponse);
                        }
                    }
                });
    }
}