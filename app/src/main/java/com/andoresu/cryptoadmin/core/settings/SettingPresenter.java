

package com.andoresu.cryptoadmin.core.settings;

import android.content.Context;

import com.andoresu.cryptoadmin.client.ObserverResponse;
import com.andoresu.cryptoadmin.client.ObserverResponseWhitError;
import com.andoresu.cryptoadmin.client.ServiceGenerator;
import com.andoresu.cryptoadmin.core.charges.data.SettingErrors;
import com.andoresu.cryptoadmin.core.charges.data.SettingErrorsResponse;
import com.andoresu.cryptoadmin.core.settings.data.Setting;
import com.andoresu.cryptoadmin.security.SecureData;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.andoresu.cryptoadmin.client.GsonBuilderUtils.getUserGson;

public class SettingPresenter implements SettingContract.UserActionsListener{

    String TAG = "CRYPTO_" + SettingPresenter.class.getSimpleName();

    private final SettingContract.View settingView;

    private final Context context;

    private final SettingService settingService;

    public SettingPresenter(SettingContract.View settingView, Context context) {
        this.settingView = settingView;
        this.context = context;
        this.settingService = ServiceGenerator.createService(SettingService.class, SecureData.getToken(), getUserGson());
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
    public void getSetting() {
        settingService.get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponse<Response<Setting>>(settingView, true) {
                    @Override
                    public void onNext(Response<Setting> settingResponse) {
                        super.onNext(settingResponse);
                        if(settingResponse.isSuccessful()){
                            settingView.showSetting(settingResponse.body());
                        }
                    }
                });
    }
}