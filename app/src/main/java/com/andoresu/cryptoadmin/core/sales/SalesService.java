package com.andoresu.cryptoadmin.core.sales;



import com.andoresu.cryptoadmin.core.sales.data.Sale;
import com.andoresu.cryptoadmin.core.sales.data.SalesResponse;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface SalesService {

    @GET("sales/")
    Observable<Response<SalesResponse>> index(@QueryMap Map<String, String> options);

    @Multipart
    @PUT("sales/{id}/approve")
    Observable<Response<Sale>> approve(@Path("id") String id, @Part MultipartBody.Part evidence);

    @PUT("sales/{id}/deny")
    Observable<Response<ResponseBody>> deny(@Path("id") String id);
}
