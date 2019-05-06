package com.andoresu.cryptoadmin.core.btcchargedetail;

import android.content.Context;
import android.net.Uri;

import com.andoresu.cryptoadmin.client.ObserverResponse;
import com.andoresu.cryptoadmin.client.ServiceGenerator;
import com.andoresu.cryptoadmin.core.btccharges.BtcChargesService;
import com.andoresu.cryptoadmin.core.btccharges.data.BtcCharge;
import com.andoresu.cryptoadmin.core.charges.ChargesService;
import com.andoresu.cryptoadmin.core.charges.data.Charge;
import com.andoresu.cryptoadmin.security.SecureData;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.andoresu.cryptoadmin.client.GsonBuilderUtils.getUserGson;
import static com.andoresu.cryptoadmin.utils.MyUtils.prepareFilePart;

public class BtcChargeDetailPresenter implements BtcChargeDetailContract.UserActionsListener{

    String TAG = "CRYPTO_" + BtcChargeDetailPresenter.class.getSimpleName();

    private final BtcChargeDetailContract.View chargeDetailView;

    private final Context context;

    private final BtcChargesService chargesService;

    public BtcChargeDetailPresenter(BtcChargeDetailContract.View chargeDetailView, Context context) {
        this.chargeDetailView = chargeDetailView;
        this.context = context;
        this.chargesService = ServiceGenerator.createService(BtcChargesService.class, SecureData.getToken(), getUserGson());
    }

    @Override
    public void approveCharge(BtcCharge charge, Uri fileUri) {

        MultipartBody.Part body = prepareFilePart("charge[qr]", fileUri, context);

        chargesService.approve(charge.id, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponse<Response<BtcCharge>>(chargeDetailView, true) {
                    @Override
                    public void onNext(Response<BtcCharge> responseBodyResponse) {
                        super.onNext(responseBodyResponse);
                        if(responseBodyResponse.isSuccessful()) {
                            if(responseBodyResponse.body() != null){
                                chargeDetailView.showCharge(responseBodyResponse.body());
                            }else{
                                charge.state = BtcCharge.STATE_ACCEPTED;
                                chargeDetailView.showCharge(charge);
                            }
                            chargeDetailView.showMessage("Recarga aprobada exitosamente");
                        }
                    }
                });

    }

    @Override
    public void denyCharge(BtcCharge charge) {
        chargesService.deny(charge.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponse<Response<BtcCharge>>(chargeDetailView, true) {
                    @Override
                    public void onNext(Response<BtcCharge> responseBodyResponse) {
                        super.onNext(responseBodyResponse);
                        if(responseBodyResponse.isSuccessful()) {
                            if(responseBodyResponse.body() != null){
                                chargeDetailView.showCharge(responseBodyResponse.body());
                            }else{
                                charge.state = BtcCharge.STATE_DENIED;
                                chargeDetailView.showCharge(charge);
                            }
                            chargeDetailView.showMessage("Recarga rechazada exitosamente");
                        }
                    }
                });
    }

    @Override
    public void completeCharge(BtcCharge charge) {

        chargesService.successful(charge.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponse<Response<BtcCharge>>(chargeDetailView, true) {
                    @Override
                    public void onNext(Response<BtcCharge> responseBodyResponse) {
                        super.onNext(responseBodyResponse);
                        if(responseBodyResponse.isSuccessful()) {
                            if(responseBodyResponse.body() != null){
                                chargeDetailView.showCharge(responseBodyResponse.body());
                            }else{
                                charge.state = BtcCharge.STATE_SUCCESSFUL;
                                chargeDetailView.showCharge(charge);
                            }
                            chargeDetailView.showMessage("Recarga completada exitosamente");
                        }
                    }
                });
    }
}