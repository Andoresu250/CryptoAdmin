package com.andoresu.cryptoadmin.core.countries;

import com.andoresu.cryptoadmin.authorization.data.Country;
import com.andoresu.cryptoadmin.core.chargepoints.data.ChargePoint;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface CountriesService {

    @GET("countries/")
    Observable<Response<List<Country>>> index();

}
