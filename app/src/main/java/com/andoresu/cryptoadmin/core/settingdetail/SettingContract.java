package com.andoresu.cryptoadmin.core.settingdetail;

import com.andoresu.cryptoadmin.authorization.data.Country;
import com.andoresu.cryptoadmin.core.charges.data.SettingErrors;
import com.andoresu.cryptoadmin.core.settingdetail.data.Setting;
import com.andoresu.cryptoadmin.utils.BaseView;

import java.util.List;

public interface SettingContract {
    interface View extends BaseView {
        void showSetting(Setting setting);

        void showSettingErrors(SettingErrors settingErrors);

        void settingCreatedSuccessfully();

        void showCountries(List<Country> countries);
    }

    interface  UserActionsListener {

        void createSetting(Setting setting);

        void getCountries();

    }

    interface InteractionListener {

    }
}
