package com.andoresu.cryptoadmin.core.setting;

import android.content.Context;

import com.andoresu.cryptoadmin.client.ObserverResponse;
import com.andoresu.cryptoadmin.client.ServiceGenerator;
import com.andoresu.cryptoadmin.core.sales.SaleContract;
import com.andoresu.cryptoadmin.core.sales.SalesPresenter;
import com.andoresu.cryptoadmin.core.sales.SalesService;
import com.andoresu.cryptoadmin.core.sales.data.SalesResponse;
import com.andoresu.cryptoadmin.core.setting.data.SettingsResponse;
import com.andoresu.cryptoadmin.core.settingdetail.SettingService;
import com.andoresu.cryptoadmin.security.SecureData;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.andoresu.cryptoadmin.client.GsonBuilderUtils.getUserGson;

public class SettingsPresenter implements SettingsContract.UserActionsListener{

    private final SettingsContract.View view;

    private final Context context;

    private final SettingService settingService;

    public SettingsPresenter(SettingsContract.View view, Context context) {
        this.view = view;
        this.context = context;
        this.settingService = ServiceGenerator.createService(SettingService.class, SecureData.getToken(), getUserGson());
    }

    @Override
    public void getSettings(Map<String, String> options) {
        settingService.index(options)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponse<Response<SettingsResponse>>(view){
                    @Override
                    public void onNext(Response<SettingsResponse> settingsResponseResponse) {
                        super.onNext(settingsResponseResponse);
                        if(settingsResponseResponse.isSuccessful()){
                            SettingsResponse settingsResponse = settingsResponseResponse.body();
                            view.showSettings(settingsResponse);
                        }
                    }
                });
    }
}
