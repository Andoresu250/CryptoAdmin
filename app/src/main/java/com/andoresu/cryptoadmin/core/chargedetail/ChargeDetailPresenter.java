package com.andoresu.cryptoadmin.core.chargedetail;

import android.content.Context;

import com.andoresu.cryptoadmin.client.ObserverResponse;
import com.andoresu.cryptoadmin.client.ServiceGenerator;
import com.andoresu.cryptoadmin.core.charges.ChargesService;
import com.andoresu.cryptoadmin.core.charges.data.Charge;

import com.andoresu.cryptoadmin.security.SecureData;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.andoresu.cryptoadmin.client.GsonBuilderUtils.getUserGson;

public class ChargeDetailPresenter implements ChargeDetailContract.UserActionsListener{

    String TAG = "CRYPTO_" + ChargeDetailPresenter.class.getSimpleName();

    private final ChargeDetailContract.View chargeDetailView;

    private final Context context;

    private final ChargesService chargesService;

    public ChargeDetailPresenter(ChargeDetailContract.View chargeDetailView, Context context) {
        this.chargeDetailView = chargeDetailView;
        this.context = context;
        this.chargesService = ServiceGenerator.createService(ChargesService.class, SecureData.getToken(), getUserGson());
    }

    @Override
    public void approveCharge(Charge charge) {
        chargesService.approve(charge.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponse<Response<ResponseBody>>(chargeDetailView, true) {
                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {
                        super.onNext(responseBodyResponse);
                        if(responseBodyResponse.isSuccessful()){
                            charge.state = Charge.STATE_APPROVED;
                            chargeDetailView.showCharge(charge);
                        }
                    }
                });

    }

    @Override
    public void denyCharge(Charge charge) {
        chargesService.deny(charge.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponse<Response<ResponseBody>>(chargeDetailView, true) {
                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {
                        super.onNext(responseBodyResponse);
                        if(responseBodyResponse.isSuccessful()){
                            charge.state = Charge.STATE_DENIED;
                            chargeDetailView.showCharge(charge);
                        }
                    }
                });
    }
}