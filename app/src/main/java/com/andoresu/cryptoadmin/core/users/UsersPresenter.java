package com.andoresu.cryptoadmin.core.users;

import android.content.Context;
import android.util.Log;

import com.andoresu.cryptoadmin.authorization.login.data.UsersResponse;
import com.andoresu.cryptoadmin.client.ObserverResponse;
import com.andoresu.cryptoadmin.client.ServiceGenerator;
import com.andoresu.cryptoadmin.security.SecureData;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.andoresu.cryptoadmin.client.GsonBuilderUtils.getUserGson;

public class UsersPresenter implements UsersContract.UserActionsListener{

    String TAG = "CRYPTO_" + UsersPresenter.class.getSimpleName();

    private final UsersContract.View usersView;

    private final Context context;

    private final UsersService usersService;

    public UsersPresenter(UsersContract.View usersView, Context context) {
        this.usersView = usersView;
        this.context = context;
        this.usersService = ServiceGenerator.createService(UsersService.class, SecureData.getToken(), getUserGson());
    }

    @Override
    public void getUsers(Map<String, String> options) {
        Log.i(TAG, "getUsers: ");
        usersService.index(options)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponse<Response<UsersResponse>>(usersView){
                    @Override
                    public void onNext(Response<UsersResponse> usersResponseResponse) {
                        super.onNext(usersResponseResponse);
                        if(usersResponseResponse.isSuccessful()){
                            Log.i(TAG, "onNext: succesfull");
                            UsersResponse usersResponse = usersResponseResponse.body();
                            usersView.showUsers(usersResponse);
                        }
                    }
                });
    }
}
