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

    private final SaleDetailContract.View saleDetailView;

    private final Context context;

    private final SalesService salesService;

    public SaleDetailPresenter(SaleDetailContract.View chargeDetailView, Context context) {
        this.saleDetailView = chargeDetailView;
        this.context = context;
        this.salesService = ServiceGenerator.createService(SalesService.class, SecureData.getToken(), getUserGson());
    }

    @Override
    public void approveSale(Sale sale, Uri fileUri) {


        MultipartBody.Part body = prepareFilePart("sale[deposit_evidence]", fileUri, context);

        salesService.approve(sale.id, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponse<Response<Sale>>(saleDetailView, true) {
                    @Override
                    public void onNext(Response<Sale> saleResponse) {
                        super.onNext(saleResponse);
                        if(saleResponse.isSuccessful()){
                            sale.state = Sale.STATE_APPROVED;
                            saleDetailView.showSale(saleResponse.body());
                            saleDetailView.showMessage("Venta aprobada exitosamente");
                        }
                    }
                });

    }

    @Override
    public void denySale(Sale sale) {
        salesService.deny(sale.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponse<Response<ResponseBody>>(saleDetailView, true) {
                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {
                        super.onNext(responseBodyResponse);
                        if(responseBodyResponse.isSuccessful()){
                            sale.state = Sale.STATE_DENIED;
                            saleDetailView.showSale(sale);
                            saleDetailView.showMessage("Venta rechazada exitosamente");
                        }
                    }
                });
    }
}