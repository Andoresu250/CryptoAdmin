package com.andoresu.cryptoadmin.core.charges;

import android.content.Context;
import android.util.Log;


import com.andoresu.cryptoadmin.client.ObserverResponse;
import com.andoresu.cryptoadmin.client.ServiceGenerator;
import com.andoresu.cryptoadmin.core.charges.data.ChargesResponse;
import com.andoresu.cryptoadmin.security.SecureData;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.andoresu.cryptoadmin.client.GsonBuilderUtils.getUserGson;

public class ChargesPresenter implements ChargesContract.UserActionsListener{

    String TAG = "CRYPTO_" + ChargesPresenter.class.getSimpleName();

    private final ChargesContract.View chargesView;

    private final Context context;

    private final ChargesService chargesService;

    public ChargesPresenter(ChargesContract.View chargesView, Context context) {
        this.chargesView = chargesView;
        this.context = context;
        this.chargesService = ServiceGenerator.createService(ChargesService.class, SecureData.getToken(), getUserGson());
    }

    @Override
    public void getCharges(Map<String, String> options) {
        Log.i(TAG, "getCharges: ");
        chargesService.index(options)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponse<Response<ChargesResponse>>(chargesView){
                    @Override
                    public void onNext(Response<ChargesResponse> chargesResponseResponse) {
                        super.onNext(chargesResponseResponse);
                        if(chargesResponseResponse.isSuccessful()){
                            ChargesResponse usersResponse = chargesResponseResponse.body();
                            chargesView.showCharges(usersResponse);
                        }
                    }
                });
    }
}