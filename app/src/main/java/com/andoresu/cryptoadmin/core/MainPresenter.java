package com.andoresu.cryptoadmin.core;

import android.content.Context;
import android.support.annotation.NonNull;

import com.andoresu.cryptoadmin.authorization.login.LoginService;
import com.andoresu.cryptoadmin.client.ObserverResponse;
import com.andoresu.cryptoadmin.client.ServiceGenerator;
import com.andoresu.cryptoadmin.security.SecureData;
import com.andoresu.cryptoadmin.utils.SimpleResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class MainPresenter implements MainContract.UserActionsListener{


    private String TAG = "CRYPTO_" + MainPresenter.class.getSimpleName();

    private final MainContract.View mainView;

    private final Context context;

    private final LoginService loginService;

    public MainPresenter(@NonNull MainContract.View mainView, @NonNull Context context, String authToken){
        this.mainView = mainView;
        this.context = context;
        this.loginService = ServiceGenerator.createService(LoginService.class, authToken);

    }

    @Override
    public void logout() {
        loginService.logout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponse<Response<SimpleResponse>>(mainView){
                    @Override
                    public void onNext(Response<SimpleResponse> simpleResponseResponse) {
                        super.onNext(simpleResponseResponse);
                        SecureData.removeToken();
                        mainView.onLogoutFinish();
                    }
                });
    }

}
