package com.andoresu.cryptoadmin.core.saledetail;

import android.content.Context;
import android.net.Uri;

import com.andoresu.cryptoadmin.client.ObserverResponse;
import com.andoresu.cryptoadmin.client.ServiceGenerator;
import com.andoresu.cryptoadmin.core.sales.SalesService;
import com.andoresu.cryptoadmin.core.sales.data.Sale;
import com.andoresu.cryptoadmin.security.SecureData;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.andoresu.cryptoadmin.client.GsonBuilderUtils.getUserGson;
import static com.andoresu.cryptoadmin.utils.MyUtils.prepareFilePart;

public class SaleDetailPresenter implements SaleDetailContract.UserActionsListener{

    String TAG = "CRYPTO_" + SaleDetailPresenter.class.getSimpleName();

    private final SaleDetailContract.View chargeDetailView;

    private final Context context;

    private final SalesService chargesService;

    public SaleDetailPresenter(SaleDetailContract.View chargeDetailView, Context context) {
        this.chargeDetailView = chargeDetailView;
        this.context = context;
        this.chargesService = ServiceGenerator.createService(SalesService.class, SecureData.getToken(), getUserGson());
    }

    @Override
    public void approveSale(Sale charge, Uri fileUri) {


        MultipartBody.Part body = prepareFilePart("sale[evidence]", fileUri, context);

        chargesService.approve(charge.id, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponse<Response<ResponseBody>>(chargeDetailView, true) {
                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {
                        super.onNext(responseBodyResponse);
                        if(responseBodyResponse.isSuccessful()){
                            charge.state = Sale.STATE_APPROVED;
                            chargeDetailView.showSale(charge);
                        }
                    }
                });

    }

    @Override
    public void denySale(Sale charge) {
        chargesService.deny(charge.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponse<Response<ResponseBody>>(chargeDetailView, true) {
                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {
                        super.onNext(responseBodyResponse);
                        if(responseBodyResponse.isSuccessful()){
                            charge.state = Sale.STATE_DENIED;
                            chargeDetailView.showSale(charge);
                        }
                    }
                });
    }
}