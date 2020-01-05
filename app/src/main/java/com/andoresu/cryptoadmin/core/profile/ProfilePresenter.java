package com.andoresu.cryptoadmin.core.profile;

import android.content.Context;

import com.andoresu.cryptoadmin.authorization.data.User;
import com.andoresu.cryptoadmin.client.ObserverResponse;
import com.andoresu.cryptoadmin.client.ObserverResponseWhitError;
import com.andoresu.cryptoadmin.client.ServiceGenerator;
import com.andoresu.cryptoadmin.core.profile.data.UserErrors;
import com.andoresu.cryptoadmin.core.profile.data.UserErrorsResponse;
import com.andoresu.cryptoadmin.security.SecureData;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.andoresu.cryptoadmin.client.GsonBuilderUtils.getUserGson;

public class ProfilePresenter implements ProfileContract.UserActionsListener{

    String TAG = "CRYPTO_" + ProfilePresenter.class.getSimpleName();

    private final ProfileContract.View profileView;

    private final Context context;

    private final ProfileService profileService;

    public ProfilePresenter(ProfileContract.View settingView, Context context) {
        this.profileView = settingView;
        this.context = context;
        this.profileService = ServiceGenerator.createService(ProfileService.class, SecureData.getToken(), getUserGson());
    }

    @Override
    public void updateUser(User user) {
        HashMap<String, User> hashMap = new HashMap<>();
        hashMap.put("user", user);
        profileService.update(user.id, hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponseWhitError<Response<User>, UserErrorsResponse>(profileView, UserErrorsResponse.class){
                    @Override
                    public void onNext(Response<User> userResponse) {
                        super.onNext(userResponse);
                        if(userResponse.isSuccessful()){
                            profileView.showUser(userResponse.body());
                            profileView.showUserErrors(new UserErrors());
                            profileView.userUpdatedSuccessfully();
                        }else{
                            profileView.showUserErrors(getError().error);
                        }
                    }
                });
    }

    @Override
    public void changeUserPassword(User user) {

        UserErrors userErrors = new UserErrors();
        if(user.password == null){
            userErrors.password.add("Este campo es requerido");
            profileView.showUserErrors(userErrors);
            return;
        }
        if(user.passwordConfirmation == null){
            userErrors.passwordConfirmation.add("Este campo es requerido");
            profileView.showUserErrors(userErrors);
            return;
        }
        if(!user.passwordConfirmation.equals(user.password)){
            userErrors.passwordConfirmation.add("La contraseña no coincide");
            profileView.showUserErrors(userErrors);
            return;
        }

        HashMap<String, User> hashMap = new HashMap<>();
        hashMap.put("user", user);
        profileService.update(user.id, hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponseWhitError<Response<User>, UserErrorsResponse>(profileView, UserErrorsResponse.class){
                    @Override
                    public void onNext(Response<User> userResponse) {
                        super.onNext(userResponse);
                        if(userResponse.isSuccessful()){
                            profileView.showUser(userResponse.body());
                            profileView.showUserErrors(new UserErrors());
                            profileView.userUpdatedSuccessfully();
                        }else{
                            profileView.showUserErrors(getError().error);
                        }
                    }
                });
    }

    @Override
    public void getUser() {
        profileService.get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponse<Response<User>>(profileView, true) {
                    @Override
                    public void onNext(Response<User> userResponse) {
                        super.onNext(userResponse);
                        if(userResponse.isSuccessful()){
                            profileView.showUser(userResponse.body());
                        }
                    }
                });
    }
}
