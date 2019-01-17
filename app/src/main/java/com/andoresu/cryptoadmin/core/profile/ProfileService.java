package com.andoresu.cryptoadmin.core.profile;


import com.andoresu.cryptoadmin.authorization.data.User;

import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProfileService {

    @GET("sessions/check")
    Observable<Response<User>> get();

    @PUT("users/{id}")
    Observable<Response<User>> update(@Path("id") String id, @Body HashMap<String, User> userHashMap);

    @PUT("users/{id}")
    Observable<Response<User>> changePassword(@Path("id") String id, @Body HashMap<String, User> userHashMap);
}
