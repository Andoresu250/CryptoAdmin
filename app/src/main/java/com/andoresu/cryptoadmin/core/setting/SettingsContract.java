package com.andoresu.cryptoadmin.core.setting;

import com.andoresu.cryptoadmin.core.setting.data.SettingsResponse;
import com.andoresu.cryptoadmin.core.settingdetail.data.Setting;
import com.andoresu.cryptoadmin.utils.BaseView;

import java.util.Map;

public interface SettingsContract {
    interface View extends BaseView {

        void showSettings(SettingsResponse settingsResponse);

    }

    interface  UserActionsListener {

        void getSettings(Map<String, String> options);

    }

    interface InteractionListener {

        void goToSettingDetail(Setting setting);

    }
}
