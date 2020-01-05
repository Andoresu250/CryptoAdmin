package com.andoresu.cryptoadmin.core.setting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andoresu.cryptoadmin.R;
import com.andoresu.cryptoadmin.core.settingdetail.data.Setting;
import com.andoresu.cryptoadmin.list.RecyclerViewAdapter;

import java.util.ArrayList;

public class SettingAdapter extends RecyclerViewAdapter<Setting> {

    public SettingAdapter(Context context, OnItemClickListener<Setting> listener) {
        super(context, listener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setData(BaseViewHolder<Setting> holder, int position) {
        Setting setting = get(position);
        SettingViewHolder viewHolder = (SettingViewHolder) holder;
        if(setting.country != null){
            viewHolder.countryEditText.setText(setting.country.name);
        }
        viewHolder.salePercentageEditText.setText(setting.getSalePercentage());
        viewHolder.purchasePercentageEditText.setText(setting.getPurchasePercentage());
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.item_setting;
    }

    @NonNull
    @Override
    public BaseViewHolder<Setting> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(getLayoutResId(), parent, false);
        return new SettingViewHolder(view);
    }
}
