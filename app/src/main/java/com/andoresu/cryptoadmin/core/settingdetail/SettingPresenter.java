

package com.andoresu.cryptoadmin.core.settingdetail;

import android.content.Context;

import com.andoresu.cryptoadmin.authorization.data.Country;
import com.andoresu.cryptoadmin.client.ObserverResponse;
import com.andoresu.cryptoadmin.client.ObserverResponseWhitError;
import com.andoresu.cryptoadmin.client.ServiceGenerator;
import com.andoresu.cryptoadmin.core.charges.data.SettingErrors;
import com.andoresu.cryptoadmin.core.charges.data.SettingErrorsResponse;
import com.andoresu.cryptoadmin.core.countries.CountriesService;
import com.andoresu.cryptoadmin.core.settingdetail.data.Setting;
import com.andoresu.cryptoadmin.security.SecureData;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.andoresu.cryptoadmin.client.GsonBuilderUtils.getUserGson;

public class SettingPresenter implements SettingContract.UserActionsListener{

    String TAG = "CRYPTO_" + SettingPresenter.class.getSimpleName();

    private final SettingContract.View settingView;

    private final Context context;

    private final SettingService settingService;
    private final CountriesService countriesService;

    public SettingPresenter(SettingContract.View settingView, Context context) {
        this.settingView = settingView;
        this.context = context;
        this.settingService = ServiceGenerator.createService(SettingService.class, SecureData.getToken(), getUserGson());
        this.countriesService = ServiceGenerator.createService(CountriesService.class, SecureData.getToken(), getUserGson());
    }

    @Override
    public void createSetting(Setting setting) {
        settingService.create(setting)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponseWhitError<Response<Setting>, SettingErrorsResponse>(settingView, SettingErrorsResponse.class){
                    @Override
                    public void onNext(Response<Setting> settingResponse) {
                        super.onNext(settingResponse);
                        if(settingResponse.isSuccessful()){
                            settingView.showSetting(settingResponse.body());
                            settingView.showSettingErrors(new SettingErrors());
                            settingView.settingCreatedSuccessfully();
                        }else{
                            settingView.showSettingErrors(getError().error);
                        }
                    }
                });
    }

    @Override
    public void getCountries() {
        countriesService.index()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponse<Response<List<Country>>>(settingView){
                    @Override
                    public void onNext(Response<List<Country>> listResponse) {
                        super.onNext(listResponse);
                        if(listResponse.isSuccessful()){
                            settingView.showCountries(listResponse.body());
                        }
                    }
                });
    }
}