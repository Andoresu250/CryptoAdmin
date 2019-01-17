package com.andoresu.cryptoadmin.authorization.login;


import com.andoresu.cryptoadmin.authorization.data.LoginRequest;
import com.andoresu.cryptoadmin.authorization.data.User;
import com.andoresu.cryptoadmin.utils.SimpleResponse;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface LoginService {

    @POST("sessions")
    Observable<Response<User>> login(@Body LoginRequest loginRequest);

    @DELETE("sessions/logout")
    Observable<Response<SimpleResponse>> logout();

    @GET("sessions/check")
    Observable<Response<User>> check(@Header("Authorization") String token);
}
