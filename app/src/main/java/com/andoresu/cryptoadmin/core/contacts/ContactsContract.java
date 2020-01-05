package com.andoresu.cryptoadmin.core.contacts;

import com.andoresu.cryptoadmin.core.contacts.data.ContactsResponse;
import com.andoresu.cryptoadmin.core.setting.data.SettingsResponse;
import com.andoresu.cryptoadmin.core.settingdetail.data.Setting;
import com.andoresu.cryptoadmin.utils.BaseView;

import java.util.Map;

public interface ContactsContract {
    interface View extends BaseView {

        void showContacts(ContactsResponse contactsResponse);

    }

    interface  UserActionsListener {

        void getContacts(Map<String, String> options);

    }

    interface InteractionListener {

    }
}
