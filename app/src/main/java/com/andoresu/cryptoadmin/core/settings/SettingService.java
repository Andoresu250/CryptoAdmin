package com.andoresu.cryptoadmin.core.settings;

import com.andoresu.cryptoadmin.core.settings.data.Setting;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface SettingService {

    @GET("settings?pretty=false")
    Observable<Response<Setting>> get();

    @POST("settings/")
    Observable<Response<Setting>> create(@Body Setting setting);

}
