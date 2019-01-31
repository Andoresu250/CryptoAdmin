package com.andoresu.cryptoadmin.core.notices;

import com.andoresu.cryptoadmin.core.notices.data.Notice;
import com.andoresu.cryptoadmin.core.notices.data.NoticesResponse;
import com.andoresu.cryptoadmin.core.purchase.data.Purchase;
import com.andoresu.cryptoadmin.core.purchase.data.PurchasesResponse;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface NoticesService {

    @GET("blogs/")
    Observable<Response<NoticesResponse>> index(@QueryMap Map<String, String> options);

    @Multipart
    @POST("blogs")
    Observable<Response<Notice>> create(@PartMap HashMap<String, RequestBody> partMap, @Part MultipartBody.Part image);

    @Multipart
    @PUT("blogs/{id}")
    Observable<Response<Notice>> update(@Path("id") String id, @PartMap HashMap<String, RequestBody> partMap, @Part MultipartBody.Part image);

}
