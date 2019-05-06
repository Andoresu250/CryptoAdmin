package com.andoresu.cryptoadmin.core.btccharges;

import android.content.Context;
import android.util.Log;

import com.andoresu.cryptoadmin.client.ObserverResponse;
import com.andoresu.cryptoadmin.client.ServiceGenerator;
import com.andoresu.cryptoadmin.core.btccharges.data.BtcChargesResponse;
import com.andoresu.cryptoadmin.security.SecureData;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.andoresu.cryptoadmin.client.GsonBuilderUtils.getUserGson;

public class BtcChargesPresenter implements BtcChargesContract.UserActionsListener{

    String TAG = "CRYPTO_" + BtcChargesPresenter.class.getSimpleName();

    private final BtcChargesContract.View chargesView;

    private final Context context;

    private final BtcChargesService btcChargesService;

    public BtcChargesPresenter(BtcChargesContract.View chargesView, Context context) {
        this.chargesView = chargesView;
        this.context = context;
        this.btcChargesService = ServiceGenerator.createService(BtcChargesService.class, SecureData.getToken(), getUserGson());
    }

    @Override
    public void getCharges(Map<String, String> options) {
        Log.i(TAG, "getCharges: ");
        btcChargesService.index(options)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponse<Response<BtcChargesResponse>>(chargesView){
                    @Override
                    public void onNext(Response<BtcChargesResponse> chargesResponseResponse) {
                        super.onNext(chargesResponseResponse);
                        Log.i(TAG, "onNext: ");
                        if(chargesResponseResponse.isSuccessful()){
                            Log.i(TAG, "onNext: successful");
                            BtcChargesResponse usersResponse = chargesResponseResponse.body();
                            chargesView.showCharges(usersResponse);
                        }else{
                            Log.i(TAG, "onNext: failed");
                        }
                    }
                });
    }
}