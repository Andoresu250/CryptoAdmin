package com.andoresu.cryptoadmin.core.profile;

import com.andoresu.cryptoadmin.authorization.data.User;
import com.andoresu.cryptoadmin.core.profile.data.UserErrors;
import com.andoresu.cryptoadmin.utils.BaseView;

public interface ProfileContract {
    interface View extends BaseView {
        void showUser(User user);

        void showUserErrors(UserErrors userErrors);

        void userUpdatedSuccessfully();
    }

    interface  UserActionsListener {

        void updateUser(User user);

        void changeUserPassword(User user);

        void getUser();

    }

    interface InteractionListener {

        void updateUserName(User user);

    }
}
