package com.andoresu.cryptoadmin.core.notices;

import android.content.Context;

import com.andoresu.cryptoadmin.client.ObserverResponse;
import com.andoresu.cryptoadmin.client.ServiceGenerator;
import com.andoresu.cryptoadmin.core.notices.data.Notice;
import com.andoresu.cryptoadmin.core.notices.data.NoticesResponse;
import com.andoresu.cryptoadmin.security.SecureData;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

import static com.andoresu.cryptoadmin.client.GsonBuilderUtils.getUserGson;

public class NoticesPresenter implements NoticesContract.UserActionsListener{

    private final NoticesContract.View view;

    private final Context context;

    private final NoticesService noticesService;

    public NoticesPresenter(NoticesContract.View view, Context context) {
        this.view = view;
        this.context = context;
        this.noticesService = ServiceGenerator.createService(NoticesService.class, SecureData.getToken(), getUserGson());
    }

    @Override
    public void getNotices(Map<String, String> options) {
        noticesService.index(options)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponse<Response<NoticesResponse>>(view){
                    @Override
                    public void onNext(Response<NoticesResponse> noticesResponseResponse) {
                        super.onNext(noticesResponseResponse);
                        if(noticesResponseResponse.isSuccessful()){
                            NoticesResponse noticesResponse = noticesResponseResponse.body();
                            view.showNotices(noticesResponse);
                        }
                    }
                });
    }

    @Override
    public void createNotice(Notice notice) {

    }

    @Override
    public void updateNotice(Notice notice) {

    }
}
