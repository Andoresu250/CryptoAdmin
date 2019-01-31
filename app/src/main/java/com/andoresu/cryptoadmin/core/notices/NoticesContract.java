package com.andoresu.cryptoadmin.core.notices;

import com.andoresu.cryptoadmin.core.notices.data.Notice;
import com.andoresu.cryptoadmin.core.notices.data.NoticesResponse;
import com.andoresu.cryptoadmin.core.purchase.data.Purchase;
import com.andoresu.cryptoadmin.utils.BaseView;

import java.util.Map;

public interface NoticesContract {
    interface View extends BaseView {

        void showNotices(NoticesResponse noticesResponse);

    }

    interface  UserActionsListener {

        void getNotices(Map<String, String> options);

        void createNotice(Notice notice);

        void updateNotice(Notice notice);

    }

    interface InteractionListener {

        void goToNoticeDetail(Notice notice);

    }
}
