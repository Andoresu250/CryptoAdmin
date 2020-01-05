package com.andoresu.cryptoadmin.core.contacts;

import com.andoresu.cryptoadmin.core.contacts.data.ContactsResponse;
import com.andoresu.cryptoadmin.core.setting.data.SettingsResponse;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface ContactsService {

    @GET("contacts")
    Observable<Response<ContactsResponse>> index(@QueryMap Map<String, String> options);
    
}
