package com.andoresu.cryptoadmin.core.userdetail;

import android.content.Context;

import com.andoresu.cryptoadmin.authorization.data.User;
import com.andoresu.cryptoadmin.client.ObserverResponse;
import com.andoresu.cryptoadmin.client.ServiceGenerator;
import com.andoresu.cryptoadmin.core.users.UsersService;
import com.andoresu.cryptoadmin.security.SecureData;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static com.andoresu.cryptoadmin.client.GsonBuilderUtils.getUserGson;

public class UserDetailPresenter implements UserDetailContract.UserActionsListener{

    String TAG = "CRYPTO_" + UserDetailPresenter.class.getSimpleName();

    private final UserDetailContract.View userDetailView;

    private final Context context;

    private final UsersService usersService;

    public UserDetailPresenter(UserDetailContract.View userDetailView, Context context) {
        this.userDetailView = userDetailView;
        this.context = context;
        this.usersService = ServiceGenerator.createService(UsersService.class, SecureData.getToken(), getUserGson());
    }

    @Override
    public void activateUser(User user) {
        usersService.activate(user.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponse<Response<ResponseBody>>(userDetailView, true) {
                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {
                        super.onNext(responseBodyResponse);
                        if(responseBodyResponse.isSuccessful()){
                            user.state = User.STATE_ACTIVATED;
                            userDetailView.showUser(user);
                        }
                    }
                });

    }

    @Override
    public void deActivateUser(User user) {
        usersService.deactivate(user.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponse<Response<ResponseBody>>(userDetailView, true) {
                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {
                        super.onNext(responseBodyResponse);
                        if(responseBodyResponse.isSuccessful()){
                            user.state = User.STATE_DEACTIVATED;
                            userDetailView.showUser(user);
                        }
                    }
                });
    }
}
