package com.andoresu.cryptoadmin.authorization.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.Log;

import com.andoresu.cryptoadmin.R;
import com.andoresu.cryptoadmin.authorization.data.LoginRequest;
import com.andoresu.cryptoadmin.authorization.data.LoginUser;
import com.andoresu.cryptoadmin.authorization.data.User;
import com.andoresu.cryptoadmin.client.ObserverResponse;
import com.andoresu.cryptoadmin.client.ServiceGenerator;
import com.andoresu.cryptoadmin.security.SecureData;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.andoresu.cryptoadmin.client.GsonBuilderUtils.getUserGson;
import static com.andoresu.cryptoadmin.utils.MyUtils.checkNullEmpty;

@SuppressLint("LogNotTimber")
public class LoginPresenter implements LoginContract.UserActionsListener {

    private final static String TAG = "CRYPTO_" + LoginPresenter.class.getSimpleName();

    private final LoginContract.View loginView;

    private final Context context;

    private LoginService loginService;

    public LoginPresenter(@NonNull LoginContract.View loginView, Context context) {
        this.loginView = loginView;
        this.context = context;
        this.loginService = ServiceGenerator.createService(LoginService.class, getUserGson());
    }

    @Override
    public void sendLoginRequest(LoginUser loginUser) {

        boolean hasError = false;
        Resources res = null;
        if (null != context){
            res = context.getResources();
        }

        if (checkNullEmpty(loginUser.email)){
            hasError = true;
            if (null == res) {
                loginView.showUsernameError("");
            }
            else {
                loginView.showUsernameError(String.format(res.getString(R.string.error_blank), res.getString(R.string.input_user)));
            }
        }

        if (checkNullEmpty(loginUser.password)){
            hasError = true;
            if (null == res) {
                loginView.showPasswordError("");
            }
            else {
                loginView.showPasswordError(String.format(res.getString(R.string.error_blank), res.getString(R.string.input_password)));
            }
        }

        if (hasError){
            loginView.onLoginFinish();
            return;
        }

        
        loginService.login(new LoginRequest(loginUser))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponse<Response<User>>(loginView){
                    @Override
                    public void onNext(Response<User> userResponse) {
                        super.onNext(userResponse);
                        if(userResponse.isSuccessful()){
                            User user = userResponse.body();
                            if(null != user){
                                if(user.isAdmin()){
                                    SecureData.saveToken(user.token);
                                    loginView.onLoginSuccess(user);
                                }else {
                                    logout(user);
                                    loginView.onLoginFinish();
                                }
                            }else{ //user null can't deserialize
                                loginView.showLoginError();
                            }
                        }
                        loginView.onLoginFinish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        loginView.onLoginFinish();
                    }

                });

    }

    @Override
    public void logout(@NonNull User user) {
        SecureData.removeToken();
        LoginService loginService = ServiceGenerator.createService(LoginService.class, user.token);
        loginService.logout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponse<>(loginView));
    }

    @Override
    public void checkSession() {

        final String token = SecureData.getToken();
        if(checkNullEmpty(token)){
            return;
        }
        loginService.check(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponse<Response<User>>(loginView, false){

                    @Override
                    public void onNext(Response<User> userResponse) {
                        super.onNext(userResponse);
                        if(userResponse.isSuccessful()){
                            User user = userResponse.body();
                            if(user != null){
                                loginView.onLoginFinish();
                                loginView.onLoginSuccess(user);
                            }else{
                                SecureData.removeToken();
                            }
                        }else{
                            SecureData.removeToken();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        super.onError(e);
                        SecureData.removeToken();
                    }
                });
    }
}
