package com.andoresu.cryptoadmin.core.settings;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.andoresu.cryptoadmin.R;
import com.andoresu.cryptoadmin.client.ErrorResponse;
import com.andoresu.cryptoadmin.core.charges.data.Charge;
import com.andoresu.cryptoadmin.core.charges.data.SettingErrors;
import com.andoresu.cryptoadmin.core.settings.data.Setting;
import com.andoresu.cryptoadmin.utils.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.andoresu.cryptoadmin.utils.MyUtils.getFirst;

public class SettingFragment extends BaseFragment implements SettingContract.View{

    String TAG = "CRYPTO_" + SettingFragment.class.getSimpleName();

    @BindView(R.id.lastTradePriceTextInputLayout)
    TextInputLayout lastTradePriceTextInputLayout;
    @BindView(R.id.purchasePercentageTextInputLayout)
    TextInputLayout purchasePercentageTextInputLayout;
    @BindView(R.id.salePercentageTextInputLayout)
    TextInputLayout salePercentageTextInputLayout;
    @BindView(R.id.hourVolumeTextInputLayout)
    TextInputLayout hourVolumeTextInputLayout;
    @BindView(R.id.activeTradersTextInputLayout)
    TextInputLayout activeTradersTextInputLayout;

    @BindView(R.id.lastTradePriceEditText)
    EditText lastTradePriceEditText;
    @BindView(R.id.purchasePercentageEditText)
    EditText purchasePercentageEditText;
    @BindView(R.id.salePercentageEditText)
    EditText salePercentageEditText;
    @BindView(R.id.hourVolumeEditText)
    EditText hourVolumeEditText;
    @BindView(R.id.activeTradersEditText)
    EditText activeTradersEditText;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.settingLayout)
    View settingLayout;

    @BindView(R.id.saveSettingButton)
    Button saveSettingButton;

    private SettingContract.UserActionsListener actionsListener;

    private Charge charge;

    public SettingFragment(){}

    public static SettingFragment newInstance() {
        Bundle args = new Bundle();
        SettingFragment fragment = new SettingFragment();
        fragment.setArguments(args);
        fragment.setTitle("Configurar Variables");
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){}
        actionsListener = new SettingPresenter(this, getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        setUnbinder(ButterKnife.bind(this, view));
        actionsListener.getSetting();
        saveSettingButton.setOnClickListener(view1 -> actionsListener.createSetting(buildSetting()));
        return view;
    }

    @Override
    public void showProgressIndicator(boolean active) {
        if(active){
            progressBar.setVisibility(View.VISIBLE);
            settingLayout.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            settingLayout.setVisibility(View.VISIBLE);
        }
    }

    private Setting buildSetting(){
        return new Setting(lastTradePriceEditText.getText().toString(), purchasePercentageEditText.getText().toString(), salePercentageEditText.getText().toString(), hourVolumeEditText.getText().toString(), activeTradersEditText.getText().toString());
    }

    @Override
    public void showGlobalError(ErrorResponse errorResponse) {}

    @Override
    public void onLogoutFinish() {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void showSetting(Setting setting) {
        lastTradePriceEditText.setText(setting.lastTradePrice.toString());
        purchasePercentageEditText.setText(setting.purchasePercentage.toString());
        salePercentageEditText.setText(setting.salePercentage.toString());
        hourVolumeEditText.setText(setting.hourVolume.toString());
        activeTradersEditText.setText(setting.activeTraders.toString());
    }

    @Override
    public void showSettingErrors(SettingErrors settingErrors) {
        lastTradePriceTextInputLayout.setError(getFirst(settingErrors.lastTradePrice));
        purchasePercentageTextInputLayout.setError(getFirst(settingErrors.purchasePercentage));
        salePercentageTextInputLayout.setError(getFirst(settingErrors.salePercentage));
        hourVolumeTextInputLayout.setError(getFirst(settingErrors.hourVolume));
        activeTradersTextInputLayout.setError(getFirst(settingErrors.activeTraders));
    }

    @Override
    public void settingCreatedSuccessfully() {
        Toast.makeText(getContext(), "Configuracion guardada correctamente", Toast.LENGTH_SHORT).show();
    }
}