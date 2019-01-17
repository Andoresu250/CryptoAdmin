package com.andoresu.cryptoadmin.core.userdetail;

import com.andoresu.cryptoadmin.authorization.data.User;
import com.andoresu.cryptoadmin.authorization.login.data.UsersResponse;
import com.andoresu.cryptoadmin.utils.BaseView;

import java.util.Map;

public interface UserDetailContract {

    interface View extends BaseView {

        void showUser(User user);
    }

    interface  UserActionsListener {

        void activateUser(User user);

        void deActivateUser(User user);

    }
}
