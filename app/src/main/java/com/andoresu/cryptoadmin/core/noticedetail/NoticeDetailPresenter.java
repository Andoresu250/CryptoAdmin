package com.andoresu.cryptoadmin.core.noticedetail;

import android.content.Context;
import android.net.Uri;


import com.andoresu.cryptoadmin.client.ObserverResponseWhitError;
import com.andoresu.cryptoadmin.client.ServiceGenerator;
import com.andoresu.cryptoadmin.core.charges.ChargesService;
import com.andoresu.cryptoadmin.core.notices.NoticesService;
import com.andoresu.cryptoadmin.core.notices.data.Notice;
import com.andoresu.cryptoadmin.core.notices.data.NoticeErrorResponse;
import com.andoresu.cryptoadmin.security.SecureData;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

import static com.andoresu.cryptoadmin.client.GsonBuilderUtils.getUserGson;
import static com.andoresu.cryptoadmin.utils.MyUtils.createPartFromString;
import static com.andoresu.cryptoadmin.utils.MyUtils.prepareFilePart;

public class NoticeDetailPresenter implements NoticeDetailContract.UserActionsListener{

    private final NoticeDetailContract.View view;

    private final Context context;

    private final NoticesService noticesService;

    public NoticeDetailPresenter(NoticeDetailContract.View view, Context context) {
        this.view = view;
        this.context = context;
        this.noticesService = ServiceGenerator.createService(NoticesService.class, SecureData.getToken(), getUserGson());
    }

    @Override
    public void createNotice(Notice notice, Uri fileUri) {
        MultipartBody.Part image = prepareFilePart("blog[image]", fileUri, context);
        RequestBody title = createPartFromString(notice.title);
        RequestBody body = createPartFromString(notice.body);
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("blog[title]", title);
        map.put("blog[body]", body);
        noticesService.create(map, image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponseWhitError<Response<Notice>, NoticeErrorResponse>(view, NoticeErrorResponse.class){
                    @Override
                    public void onNext(Response<Notice> noticeResponse) {
                        super.onNext(noticeResponse);
                        if(noticeResponse.isSuccessful()){
                            view.showMessage("Noticia creada exitosamente");
                        }else{
                            view.showNoticeErrors(getError().error);
                        }
                    }
                });
    }

    @Override
    public void updateNotice(Notice notice, Uri fileUri) {
        MultipartBody.Part image = fileUri == null ? null : prepareFilePart("blog[image]", fileUri, context);
        RequestBody title = createPartFromString(notice.title);
        RequestBody body = createPartFromString(notice.body);
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("blog[title]", title);
        map.put("blog[body]", body);
        noticesService.update(notice.id, map, image)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverResponseWhitError<Response<Notice>, NoticeErrorResponse>(view, NoticeErrorResponse.class){
                    @Override
                    public void onNext(Response<Notice> noticeResponse) {
                        super.onNext(noticeResponse);
                        if(noticeResponse.isSuccessful()){
                            view.showMessage("Noticia actualizada exitosamente");
                        }else{
                            view.showNoticeErrors(getError().error);
                        }
                    }
                });
    }

    @Override
    public void deleteNotice(Notice notice) {

    }


}
