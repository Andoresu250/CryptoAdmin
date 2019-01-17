package com.andoresu.cryptoadmin.authorization.login;

import com.andoresu.cryptoadmin.authorization.data.LoginUser;
import com.andoresu.cryptoadmin.authorization.data.User;
import com.andoresu.cryptoadmin.utils.BaseView;


public interface LoginContract {

    interface View extends BaseView {

        void showUsernameError(String error);

        void showPasswordError(String error);

        void showUserTypeError();

        void showUserSavedError();

        void showTokenSaveError();

        void showLoginError();

        void onLoginFinish();

        void onLoginSuccess(User user);

    }

    interface UserActionsListener{

        void sendLoginRequest(LoginUser loginUser);

        void logout(User user);

        void checkSession();
    }

}
