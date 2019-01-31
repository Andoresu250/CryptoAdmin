package com.andoresu.cryptoadmin.core.noticedetail;

import android.net.Uri;

import com.andoresu.cryptoadmin.core.charges.data.Charge;
import com.andoresu.cryptoadmin.core.notices.data.Notice;
import com.andoresu.cryptoadmin.core.notices.data.NoticeErrors;
import com.andoresu.cryptoadmin.utils.BaseView;

public interface NoticeDetailContract {
    interface View extends BaseView {

        void showNotice(Notice notice);

        void showNoticeErrors(NoticeErrors errors);

    }

    interface  UserActionsListener {

        void createNotice(Notice notice, Uri fileUri);

        void updateNotice(Notice notice, Uri fileUri);

        void deleteNotice(Notice notice);

    }
}
