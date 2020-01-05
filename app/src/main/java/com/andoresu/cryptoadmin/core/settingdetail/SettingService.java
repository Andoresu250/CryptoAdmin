package com.andoresu.cryptoadmin.core.settingdetail;

import com.andoresu.cryptoadmin.core.setting.data.SettingsResponse;
import com.andoresu.cryptoadmin.core.settingdetail.data.Setting;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface SettingService {

//    @GET("settings?pretty=false")
//    Observable<Response<Setting>> get();

    @GET("settings?pretty=false&index=true")
    Observable<Response<SettingsResponse>> index(@QueryMap Map<String, String> options);

    @POST("settings/")
    Observable<Response<Setting>> create(@Body Setting setting);

}
