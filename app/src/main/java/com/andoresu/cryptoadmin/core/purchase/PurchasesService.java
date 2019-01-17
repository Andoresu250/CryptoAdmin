package com.andoresu.cryptoadmin.core.purchase;



import com.andoresu.cryptoadmin.core.purchase.data.PurchasesResponse;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface PurchasesService {

    @GET("purchases/")
    Observable<Response<PurchasesResponse>> index(@QueryMap Map<String, String> options);

    @Multipart
    @PUT("purchases/{id}/approve")
    Observable<Response<ResponseBody>> approve(@Path("id") String id, @Part MultipartBody.Part evidence);

    @PUT("purchases/{id}/deny")
    Observable<Response<ResponseBody>> deny(@Path("id") String id);
}
