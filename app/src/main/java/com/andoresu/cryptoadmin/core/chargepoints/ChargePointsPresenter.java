package com.andoresu.cryptoadmin.core.chargepoints;

import android.content.Context;

import com.andoresu.cryptoadmin.authorization.data.Country;
import com.andoresu.cryptoadmin.client.ObserverResponse;
import com.andoresu.cryptoadmin.client.ServiceGenerator;
import com.andoresu.cryptoadmin.core.chargepoints.data.ChargePoint;
import com.andoresu.cryptoadmin.core.countries.CountriesService;
import com.andoresu.cryptoadmin.security.SecureData;

import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.andoresu.cryptoadmin.client.GsonBuilderUtils.getUserGson;

public class ChargePointsPresenter implements ChargePointsContract.UserActionsListener {

    private final ChargePointsContract.View view;

    private final Context context;

    private final ChargePointsService chargePointsService;

    private final CountriesService countriesService;

    public ChargePointsPresenter(ChargePointsContract.View view, Context context) {
        this.view = view;
        this.context = context;
        this.chargePointsService = ServiceGenerator.createService(ChargePointsService.class, SecureData.getToken(), getUserGson());
        this.countriesService = ServiceGenerator.createService(CountriesService.class, SecureData.getToken(), getUserGson());
    }


    @Override
    public void getChargePoints(Map<String, String> options) {
        chargePointsService.index(options)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponse<Response<List<ChargePoint>>>(view){
                    @Override
                    public void onNext(Response<List<ChargePoint>> listResponse) {
                        super.onNext(listResponse);
                        if(listResponse.isSuccessful()){
                            view.showChargePoints(listResponse.body());
                        }
                    }
                });
    }

    @Override
    public void getCountries() {
        countriesService.index()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponse<Response<List<Country>>>(view){
                    @Override
                    public void onNext(Response<List<Country>> listResponse) {
                        if(listResponse.isSuccessful()){
                            view.setCountries(listResponse.body());
                        }
                    }

                    @Override
                    public void onComplete() {}
                });
    }

}
