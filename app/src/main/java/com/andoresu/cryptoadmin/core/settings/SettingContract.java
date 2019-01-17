package com.andoresu.cryptoadmin.core.settings;

import com.andoresu.cryptoadmin.core.charges.data.Charge;
import com.andoresu.cryptoadmin.core.charges.data.ChargesResponse;
import com.andoresu.cryptoadmin.core.charges.data.SettingErrors;
import com.andoresu.cryptoadmin.core.settings.data.Setting;
import com.andoresu.cryptoadmin.utils.BaseView;

import java.util.Map;

public interface SettingContract {
    interface View extends BaseView {
        void showSetting(Setting setting);

        void showSettingErrors(SettingErrors settingErrors);

        void settingCreatedSuccessfully();
    }

    interface  UserActionsListener {

        void createSetting(Setting setting);

        void getSetting();

    }

    interface InteractionListener {

    }
}
