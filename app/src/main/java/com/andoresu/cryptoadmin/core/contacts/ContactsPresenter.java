package com.andoresu.cryptoadmin.core.contacts;

import android.content.Context;

import com.andoresu.cryptoadmin.client.ObserverResponse;
import com.andoresu.cryptoadmin.client.ServiceGenerator;
import com.andoresu.cryptoadmin.core.contacts.data.ContactsResponse;
import com.andoresu.cryptoadmin.core.setting.data.SettingsResponse;
import com.andoresu.cryptoadmin.security.SecureData;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.andoresu.cryptoadmin.client.GsonBuilderUtils.getUserGson;

public class ContactsPresenter implements ContactsContract.UserActionsListener{

    private final ContactsContract.View view;

    private final Context context;

    private final ContactsService contactsService;

    public ContactsPresenter(ContactsContract.View view, Context context) {
        this.view = view;
        this.context = context;
        this.contactsService = ServiceGenerator.createService(ContactsService.class, SecureData.getToken(), getUserGson());
    }

    @Override
    public void getContacts(Map<String, String> options) {
        contactsService.index(options)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponse<Response<ContactsResponse>>(view){
                    @Override
                    public void onNext(Response<ContactsResponse> responseContactsResponse) {
                        super.onNext(responseContactsResponse);
                        if(responseContactsResponse.isSuccessful()){
                            ContactsResponse settingsResponse = responseContactsResponse.body();
                            view.showContacts(settingsResponse);
                        }
                    }
                });
    }
}
