package com.andoresu.cryptoadmin.core.chargepoints;

import com.andoresu.cryptoadmin.core.chargepoints.data.ChargePoint;
import com.andoresu.cryptoadmin.core.charges.data.ChargesResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface ChargePointsService {

    @GET("charge_points/")
    Observable<Response<List<ChargePoint>>> index(@QueryMap Map<String, String> options);

    @GET("charge_points/{id}")
    Observable<Response<ChargePoint>> get(@Path("id") String id);

    @POST("charge_points/")
    Observable<Response<ChargePoint>> create(@Body HashMap<String, ChargePoint> chargePointHashMap);

    @PUT("charge_points/{id}")
    Observable<Response<ChargePoint>> update(@Path("id") String id, @Body HashMap<String, ChargePoint> chargePointHashMap);

}
