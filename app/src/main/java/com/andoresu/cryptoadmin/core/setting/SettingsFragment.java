package com.andoresu.cryptoadmin.core.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.andoresu.cryptoadmin.R;
import com.andoresu.cryptoadmin.core.setting.data.SettingsResponse;
import com.andoresu.cryptoadmin.core.settingdetail.data.Setting;
import com.andoresu.cryptoadmin.list.RecyclerViewFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.OnClick;

public class SettingsFragment extends RecyclerViewFragment<Setting> implements SettingsContract.View{

    public SettingsContract.InteractionListener interactionListener;
    public SettingsContract.UserActionsListener actionsListener;
    private SettingsResponse settingsResponse;

    public static SettingsFragment newInstance(@NonNull SettingsContract.InteractionListener interactionListener) {
        Bundle args = new Bundle();
        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        fragment.interactionListener = interactionListener;
        fragment.setCustomTitle("Configuraciones");
        fragment.addSearch = true;
        fragment.setHasOptionsMenu(true);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){}
        actionsListener = new SettingsPresenter(this, getContext());
    }

    @Override
    public void handleView() {
        super.handleView();
        viewAdapter = new SettingAdapter(getContext(), item -> interactionListener.goToSettingDetail(item));
        listRecyclerView.setAdapter(viewAdapter);
        onRefresh();
    }

    private Map<String, String> getOptions(){
        Map<String, String> options = new HashMap<>();
        options.put("page", currentPage + "");
        if(searchQuery != null){
            options.put("search", searchQuery);
        }
        return options;
    }



    @Override
    public void showSettings(SettingsResponse settingsResponse) {
        this.settingsResponse = settingsResponse;
        viewAdapter.addAll(settingsResponse.settings);
        isEmpty();
    }

    @Override
    public void onRefresh(boolean clear) {
        super.onRefresh(clear);
        if(clear){
            viewAdapter.set(new ArrayList<>());
        }
        actionsListener.getSettings(getOptions());
    }

    @Override
    public int getTotalItems() {
        return settingsResponse.totalCount;
    }

    @Override
    public void showProgressIndicator(boolean active) {
        showLoading(active);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_settings;
    }

    @OnClick(R.id.newSettingFloatingActionButton)
    public void goToSetting(){
        interactionListener.goToSettingDetail(null);
    }

}
