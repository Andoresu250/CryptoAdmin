package com.andoresu.cryptoadmin.core.setting;

import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;

import com.andoresu.cryptoadmin.R;
import com.andoresu.cryptoadmin.core.settingdetail.data.Setting;
import com.andoresu.cryptoadmin.utils.BaseRecyclerViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingViewHolder extends BaseRecyclerViewAdapter.BaseViewHolder<Setting> {

    @BindView(R.id.countryTextInputLayout)
    public TextInputLayout countryTextInputLayout;
    @BindView(R.id.countryEditText)
    public EditText countryEditText;
    @BindView(R.id.purchasePercentageTextInputLayout)
    public TextInputLayout purchasePercentageTextInputLayout;
    @BindView(R.id.purchasePercentageEditText)
    public EditText purchasePercentageEditText;
    @BindView(R.id.salePercentageTextInputLayout)
    public TextInputLayout salePercentageTextInputLayout;
    @BindView(R.id.salePercentageEditText)
    public EditText salePercentageEditText;

    public SettingViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
