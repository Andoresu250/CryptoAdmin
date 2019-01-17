package com.andoresu.cryptoadmin.core.users;

import com.andoresu.cryptoadmin.authorization.data.User;
import com.andoresu.cryptoadmin.authorization.login.data.UsersResponse;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface UsersService {

    @GET("users/")
    Observable<Response<UsersResponse>> index(@QueryMap Map<String, String> options);

    @PUT("users/{id}/activate")
    Observable<Response<ResponseBody>> activate(@Path("id") String id);

    @PUT("users/{id}/deactivate")
    Observable<Response<ResponseBody>> deactivate(@Path("id") String id);

    @DELETE("users/{id}")
    Observable<Response<ResponseBody>> delete(@Path("id") String id);

}
