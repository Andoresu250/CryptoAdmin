package com.andoresu.cryptoadmin.utils;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannedString;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.andoresu.cryptoadmin.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.andoresu.cryptoadmin.utils.MyUtils.isNightMode;
import static com.andoresu.cryptoadmin.utils.MyUtils.removeTrailingLineFeed;

@SuppressLint("LogNotTimber")
public abstract class BaseDialogFragment extends DialogFragment {

    public static final String TAG = BaseFragment.BASE_TAG + BaseDialogFragment.class.getSimpleName();

    public Integer titleId;

    public String title;

    public Unbinder unbinder;

    public View view;

    public Button positiveBtn;

    public Button negativeBtn;

    public boolean cancelable = true;

    public AlertDialog.Builder builder;

    public Dialog dialog;

    public Unbinder getUnbinder() {
        return unbinder;
    }

    public void setUnbinder(Unbinder unbinder) {
        this.unbinder = unbinder;
    }

    public Integer getTitleId() {
        return titleId;
    }

    public void setTitleId(Integer titleId) {
        this.titleId = titleId;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStart() {
        super.onStart();
        positiveBtn = ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE);
        negativeBtn = ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_NEGATIVE);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(getLayoutId(), null);
        setView(view);
        setUnbinder(ButterKnife.bind(this, view));
        builder = getBuilder();
        builder.setView(view);
        handleView();
        dialog = dialog == null ? builder.create() : dialog;
        return dialog;
    }

    @LayoutRes
    public abstract int getLayoutId();

    public abstract void handleView();

    public AlertDialog.Builder getBuilder(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if(titleId == null && title == null){
            return builder;
        }
        if(titleId != null){
            builder.setTitle(titleId);
        }
        if(title != null){
            builder.setTitle(title);
        }

        return builder;
    }

    public void show(FragmentTransaction fragmentTransaction){
        try{
            show(fragmentTransaction, TAG);
        }catch (Exception e){e.printStackTrace();}
    }


    public void show(AppCompatActivity activity){
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        removeIfOpened(fragmentManager, fragmentTransaction);
        fragmentTransaction.addToBackStack(null);
        show(fragmentTransaction, TAG);
    }

    public void show(Fragment fragment){
        FragmentManager fragmentManager = fragment.getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        removeIfOpened(fragmentManager, fragmentTransaction);
        fragmentTransaction.addToBackStack(null);
        show(fragmentTransaction, TAG);
    }

    public void show(DialogFragment fragment){
        FragmentManager fragmentManager = fragment.getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        removeIfOpened(fragmentManager, fragmentTransaction);
        fragmentTransaction.addToBackStack(null);
        show(fragmentTransaction, TAG);
    }

    public static Fragment getInstancedFragment(FragmentManager fragmentManager, String tag){
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if(fragment == null){
            Log.e(TAG, "getInstancedFragment: fragment with tag: " + tag + " not found");
        }
        return fragment;
    }

    public void removeIfOpened(FragmentManager fragmentManager, FragmentTransaction fragmentTransaction){
        Fragment prev = getInstancedFragment(fragmentManager, this.getClass().getName());
        if (prev != null) {
            fragmentTransaction.remove(prev);
        }
    }

    public void showButtons(){
        if(positiveBtn != null){
            positiveBtn.setVisibility(View.VISIBLE);
        }
        if(negativeBtn != null){
            negativeBtn.setVisibility(View.VISIBLE);
        }
    }

    public void hideButtons(){
        if(positiveBtn != null){
            positiveBtn.setVisibility(View.GONE);
        }
        if(negativeBtn != null){
            negativeBtn.setVisibility(View.GONE);
        }
    }

    public void setDialogSize(Double widthPercentage, Double heightPercentage){
        setDialogSize(widthPercentage, heightPercentage, dialog);
    }

    public void setDialogSize(Double widthPercentage, Double heightPercentage, Dialog dialog){

        Window window = dialog.getWindow();
        Point size = new Point();

        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);

        int width = widthPercentage != null ? (int) (size.x * widthPercentage) : WindowManager.LayoutParams.WRAP_CONTENT;
        int height = heightPercentage != null ? (int) (size.y * heightPercentage) : WindowManager.LayoutParams.WRAP_CONTENT;

        window.setLayout(width, height);
        window.setGravity(Gravity.CENTER);

    }

    /*@Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
        int height = getResources().getDimensionPixelSize(R.dimen.popup_height);
        getDialog().getWindow().setLayout(width, height);
    }*/

    public CharSequence getText(int id, Object... args) {
        for(int i = 0; i < args.length; ++i)
            args[i] = args[i] instanceof String? TextUtils.htmlEncode((String)args[i]) : args[i];
        return removeTrailingLineFeed(Html.fromHtml(String.format(Html.toHtml(new SpannedString(getText(id))), args)));
    }
}