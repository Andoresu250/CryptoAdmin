package com.andoresu.cryptoadmin.core.chargepointdetail;

import android.content.Context;

import com.andoresu.cryptoadmin.authorization.data.Country;
import com.andoresu.cryptoadmin.core.chargepointdetail.data.ChargePointErrors;
import com.andoresu.cryptoadmin.core.chargepointdetail.data.ChargePointErrorsResponse;
import com.andoresu.cryptoadmin.client.ObserverResponse;
import com.andoresu.cryptoadmin.client.ObserverResponseWhitError;
import com.andoresu.cryptoadmin.client.ServiceGenerator;
import com.andoresu.cryptoadmin.core.chargepoints.ChargePointsService;
import com.andoresu.cryptoadmin.core.chargepoints.data.ChargePoint;
import com.andoresu.cryptoadmin.core.countries.CountriesService;
import com.andoresu.cryptoadmin.security.SecureData;

import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.andoresu.cryptoadmin.client.GsonBuilderUtils.getUserGson;

public class ChargePointDetailPresenter implements ChargePointDetailContrant.UserActionsListener{

    private final ChargePointDetailContrant.View view;

    private final Context context;

    private final ChargePointsService chargePointsService;

    private final CountriesService countriesService;

    public ChargePointDetailPresenter(ChargePointDetailContrant.View view, Context context) {
        this.view = view;
        this.context = context;
        this.chargePointsService = ServiceGenerator.createService(ChargePointsService.class, SecureData.getToken(), getUserGson());
        this.countriesService = ServiceGenerator.createService(CountriesService.class, SecureData.getToken(), getUserGson());
    }

    @Override
    public void createChargePoint(ChargePoint chargePoint) {
        HashMap<String, ChargePoint> hashMap = new HashMap<>();
        hashMap.put("chargePoint", chargePoint);
        chargePointsService.create(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observerResponseWhitError());
    }

    @Override
    public void updateChargePoint(ChargePoint chargePoint) {
        HashMap<String, ChargePoint> hashMap = new HashMap<>();
        hashMap.put("chargePoint", chargePoint);
        chargePointsService.update(chargePoint.id, hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observerResponseWhitError());
    }

    @Override
    public void getCountries() {
        countriesService.index()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponse<Response<List<Country>>>(view){
                    @Override
                    public void onNext(Response<List<Country>> listResponse) {
                        super.onNext(listResponse);
                        if(listResponse.isSuccessful()){
                            view.setCountries(listResponse.body());
                        }
                    }
                });
    }

    private ObserverResponseWhitError<Response<ChargePoint>,  ChargePointErrorsResponse> observerResponseWhitError(){
        return new ObserverResponseWhitError<Response<ChargePoint>, ChargePointErrorsResponse>(view, ChargePointErrorsResponse.class){
            @Override
            public void onNext(Response<ChargePoint> chargePointResponse) {
                super.onNext(chargePointResponse);
                if(chargePointResponse.isSuccessful()){
                    view.showChargePoint(chargePointResponse.body());
                    view.showChargePointErrors(new ChargePointErrors());
                    view.showMessage("Se ha guardado exitosamente");
                }else{
                    if(getError() != null){
                        view.showChargePointErrors(getError().errors);
                    }
                }
            }
        };
    }
}
