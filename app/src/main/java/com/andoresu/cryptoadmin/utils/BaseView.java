package com.andoresu.cryptoadmin.utils;


import com.andoresu.cryptoadmin.client.ErrorResponse;

public interface BaseView {

    void showProgressIndicator(boolean active);

    void showGlobalError(ErrorResponse errorResponse);

    void onLogoutFinish();

}
