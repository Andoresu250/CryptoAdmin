package com.andoresu.cryptoadmin.core.charges;

import com.andoresu.cryptoadmin.authorization.login.data.UsersResponse;
import com.andoresu.cryptoadmin.core.charges.data.ChargesResponse;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface ChargesService {

    @GET("charges/")
    Observable<Response<ChargesResponse>> index(@QueryMap Map<String, String> options);

    @PUT("charges/{id}/approve")
    Observable<Response<ResponseBody>> approve(@Path("id") String id);

    @PUT("charges/{id}/deny")
    Observable<Response<ResponseBody>> deny(@Path("id") String id);

}
