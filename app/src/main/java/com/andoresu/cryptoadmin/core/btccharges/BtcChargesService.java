package com.andoresu.cryptoadmin.core.btccharges;

import com.andoresu.cryptoadmin.core.btccharges.data.BtcCharge;
import com.andoresu.cryptoadmin.core.btccharges.data.BtcChargesResponse;
import com.andoresu.cryptoadmin.core.purchase.data.Purchase;

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

public interface BtcChargesService {

    @GET("btc_charges/")
    Observable<Response<BtcChargesResponse>> index(@QueryMap Map<String, String> options);

    @Multipart
    @PUT("btc_charges/{id}/approve")
    Observable<Response<BtcCharge>> approve(@Path("id") String id, @Part MultipartBody.Part qr);

    @PUT("btc_charges/{id}/successful")
    Observable<Response<BtcCharge>> successful(@Path("id") String id);

    @PUT("btc_charges/{id}/deny")
    Observable<Response<BtcCharge>> deny(@Path("id") String id);

}
