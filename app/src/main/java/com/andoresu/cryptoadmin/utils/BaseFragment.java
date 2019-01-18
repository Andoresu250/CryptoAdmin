package com.andoresu.cryptoadmin.utils;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannedString;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.andoresu.cryptoadmin.R;
import com.andoresu.cryptoadmin.client.ErrorResponse;

import butterknife.Unbinder;

import static com.andoresu.cryptoadmin.utils.MyUtils.removeTrailingLineFeed;

public class BaseFragment extends Fragment implements BaseView{

    String TAG = "CRYPTO_" + BaseFragment.class.getSimpleName();

//    @BindView(R.id.baseFragment)
    public View baseFragment;

    private int viewPagerPosition = 0;

    private Unbinder unbinder;

    private Bundle fragmentBundle;

    private boolean isFragmentCreated;

    private String title = "Sin Titulo";

    public int getViewPagerPosition() {
        return viewPagerPosition;
    }

    public void setViewPagerPosition(int viewPagerPosition) {
        this.viewPagerPosition = viewPagerPosition;
    }

    public Unbinder getUnbinder() {
        return unbinder;
    }

    public void setUnbinder(Unbinder unbinder) {
        this.unbinder = unbinder;
//        setTouchListener();
    }

    public Bundle getFragmentBundle() {
        return fragmentBundle;
    }

    public void setFragmentBundle(Bundle fragmentBundle) {
        this.fragmentBundle = fragmentBundle;
    }

    public boolean isFragmentCreated() {
        return isFragmentCreated;
    }

    public void setFragmentCreated(boolean fragmentCreated) {
        isFragmentCreated = fragmentCreated;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void onResume() {
        super.onResume();
        setFragmentCreated(true);
        if(getActivity() != null){
            getActivity().setTitle(getTitle());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if(getActivity() != null){
            getActivity().setTitle(R.string.app_name);
        }
    }

    public CharSequence getText(int id, Object... args) {
        for(int i = 0; i < args.length; ++i)
            args[i] = args[i] instanceof String? TextUtils.htmlEncode((String)args[i]) : args[i];
        return removeTrailingLineFeed(Html.fromHtml(String.format(Html.toHtml(new SpannedString(getText(id))), args)));
    }

    public Bundle getInstanceBundle(){
        Bundle bundle = new Bundle();
        return bundle;
    }

    @Override
    public void showProgressIndicator(boolean active) {

    }

    @Override
    public void showGlobalError(ErrorResponse errorResponse) {

    }

    @Override
    public void onLogoutFinish() {

    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}