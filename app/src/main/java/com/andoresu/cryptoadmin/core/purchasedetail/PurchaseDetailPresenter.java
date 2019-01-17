package com.andoresu.cryptoadmin.core.purchasedetail;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.andoresu.cryptoadmin.client.ObserverResponse;
import com.andoresu.cryptoadmin.client.ServiceGenerator;
import com.andoresu.cryptoadmin.core.purchase.PurchasesService;
import com.andoresu.cryptoadmin.core.purchase.data.Purchase;
import com.andoresu.cryptoadmin.security.SecureData;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.HashMap;

import javax.security.auth.login.LoginException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.andoresu.cryptoadmin.client.GsonBuilderUtils.getUserGson;
import static com.andoresu.cryptoadmin.utils.MyUtils.getPathFromURI;
import static com.andoresu.cryptoadmin.utils.MyUtils.prepareFilePart;
import static com.andoresu.cryptoadmin.utils.MyUtils.requestBodyFile;

public class PurchaseDetailPresenter implements PurchaseDetailContract.UserActionsListener{

    String TAG = "CRYPTO_" + PurchaseDetailPresenter.class.getSimpleName();

    private final PurchaseDetailContract.View chargeDetailView;

    private final Context context;

    private final PurchasesService chargesService;

    public PurchaseDetailPresenter(PurchaseDetailContract.View chargeDetailView, Context context) {
        this.chargeDetailView = chargeDetailView;
        this.context = context;
        this.chargesService = ServiceGenerator.createService(PurchasesService.class, SecureData.getToken(), getUserGson());
    }

    @Override
    public void approvePurchase(Purchase charge, Uri fileUri) {


        MultipartBody.Part body = prepareFilePart("purchase[evidence]", fileUri, context);

        chargesService.approve(charge.id, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponse<Response<ResponseBody>>(chargeDetailView, true) {
                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {
                        super.onNext(responseBodyResponse);
                        if(responseBodyResponse.isSuccessful()){
                            charge.state = Purchase.STATE_APPROVED;
                            chargeDetailView.showPurchase(charge);
                        }
                    }
                });

    }

    @Override
    public void denyPurchase(Purchase charge) {
        chargesService.deny(charge.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponse<Response<ResponseBody>>(chargeDetailView, true) {
                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {
                        super.onNext(responseBodyResponse);
                        if(responseBodyResponse.isSuccessful()){
                            charge.state = Purchase.STATE_DENIED;
                            chargeDetailView.showPurchase(charge);
                        }
                    }
                });
    }
}