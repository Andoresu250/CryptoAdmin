package com.andoresu.cryptoadmin.core.users;

import com.andoresu.cryptoadmin.authorization.data.User;
import com.andoresu.cryptoadmin.authorization.login.data.UsersResponse;
import com.andoresu.cryptoadmin.utils.BaseView;

import java.util.List;
import java.util.Map;

public interface UsersContract {

    interface View extends BaseView {

        void showUsers(UsersResponse usersResponse);

    }

    interface  UserActionsListener {

        void getUsers(Map<String, String> options);

    }

    interface InteractionListener {

        void goToUserDetail(User user);

    }

}
