package com.andoresu.cryptoadmin.core.settingdetail;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.andoresu.cryptoadmin.R;
import com.andoresu.cryptoadmin.authorization.data.Country;
import com.andoresu.cryptoadmin.client.ErrorResponse;
import com.andoresu.cryptoadmin.core.chargepoints.CountryAdapter;
import com.andoresu.cryptoadmin.core.charges.data.Charge;
import com.andoresu.cryptoadmin.core.charges.data.SettingErrors;
import com.andoresu.cryptoadmin.core.settingdetail.data.Setting;
import com.andoresu.cryptoadmin.utils.BaseFragment;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

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
    @BindView(R.id.marketCapTextInputLayout)
    TextInputLayout marketCapTextInputLayout;
    @BindView(R.id.dailyTransactionsTextInputLayout)
    TextInputLayout dailyTransactionsTextInputLayout;
    @BindView(R.id.activeAccountsTextInputLayout)
    TextInputLayout activeAccountsTextInputLayout;
    @BindView(R.id.supportedCountriesTextInputLayout)
    TextInputLayout supportedCountriesTextInputLayout;

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
    @BindView(R.id.marketCapEditText)
    EditText marketCapEditText;
    @BindView(R.id.dailyTransactionsEditText)
    EditText dailyTransactionsEditText;
    @BindView(R.id.activeAccountsEditText)
    EditText activeAccountsEditText;
    @BindView(R.id.supportedCountriesEditText)
    EditText supportedCountriesEditText;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.settingLayout)
    View settingLayout;

    @BindView(R.id.saveSettingButton)
    Button saveSettingButton;

    @BindView(R.id.countrySpinner)
    MaterialSpinner countrySpinner;

    CountryAdapter countryAdapter;

    private SettingContract.UserActionsListener actionsListener;

    public Setting setting;

    public SettingFragment(){}

    public static SettingFragment newInstance(Setting setting) {
        Bundle args = new Bundle();
        SettingFragment fragment = new SettingFragment();
        fragment.setArguments(args);
        fragment.setting = setting;
        if(setting == null){
            fragment.setTitle("Nueva variable");
            fragment.setting = new Setting();
        }else{
            fragment.setTitle("Editar Variable");
        }

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){}
        actionsListener = new SettingPresenter(this, getContext());
    }

    @Override
    public void handleView() {
        showSetting(setting);
        saveSettingButton.setOnClickListener(view1 -> actionsListener.createSetting(buildSetting()));
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_setting;
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
        setting.setData(lastTradePriceEditText.getText().toString(), purchasePercentageEditText.getText().toString(),
                salePercentageEditText.getText().toString(), hourVolumeEditText.getText().toString(),
                activeTradersEditText.getText().toString(), marketCapEditText.getText().toString(), dailyTransactionsEditText.getText().toString(),
                activeAccountsEditText.getText().toString(), supportedCountriesEditText.getText().toString());
        if(setting.id == null){
            setting.setCountry(countryAdapter.get(countrySpinner.getSelectedIndex()));
        }
        return setting;
    }

    @Override
    public void showGlobalError(ErrorResponse errorResponse) {}

    @Override
    public void onLogoutFinish() {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void showSetting(Setting setting) {
        if(setting.id != null && setting.country != null){
            List<Country> countries = new ArrayList<>();
            countries.add(setting.country);
            countryAdapter = new CountryAdapter(getContext(), countries);
            countrySpinner.setAdapter(countryAdapter);
            countrySpinner.setSelectedIndex(0);
            countrySpinner.setEnabled(false);
        }else{
            actionsListener.getCountries();
        }
        if(setting.lastTradePrice != null) {
            lastTradePriceEditText.setText(setting.lastTradePrice.toString());
        }
        if(setting.purchasePercentage != null) {
            purchasePercentageEditText.setText(setting.purchasePercentage.toString());
        }
        if(setting.salePercentage != null) {
            salePercentageEditText.setText(setting.salePercentage.toString());
        }
        if(setting.hourVolume != null) {
            hourVolumeEditText.setText(setting.hourVolume.toString());
        }
        if(setting.activeTraders != null) {
            activeTradersEditText.setText(setting.activeTraders.toString());
        }
        if(setting.marketCap != null) {
            marketCapEditText.setText(setting.marketCap.toString());
        }
        if(setting.dailyTransactions != null) {
            dailyTransactionsEditText.setText(setting.dailyTransactions.toString());
        }
        if(setting.activeAccounts != null) {
            activeAccountsEditText.setText(setting.activeAccounts.toString());
        }
        if(setting.supportedCountries != null) {
            supportedCountriesEditText.setText(setting.supportedCountries.toString());
        }
    }

    @Override
    public void showSettingErrors(SettingErrors settingErrors) {
        lastTradePriceTextInputLayout.setError(getFirst(settingErrors.lastTradePrice));
        purchasePercentageTextInputLayout.setError(getFirst(settingErrors.purchasePercentage));
        salePercentageTextInputLayout.setError(getFirst(settingErrors.salePercentage));
        hourVolumeTextInputLayout.setError(getFirst(settingErrors.hourVolume));
        activeTradersTextInputLayout.setError(getFirst(settingErrors.activeTraders));
        marketCapTextInputLayout.setError(getFirst(settingErrors.marketCap));
        dailyTransactionsTextInputLayout.setError(getFirst(settingErrors.dailyTransactions));
        activeAccountsTextInputLayout.setError(getFirst(settingErrors.activeAccounts));
        supportedCountriesTextInputLayout.setError(getFirst(settingErrors.supportedCountries));
    }

    @Override
    public void settingCreatedSuccessfully() {
        Toast.makeText(getContext(), "Configuracion guardada correctamente", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showCountries(List<Country> countries) {
        countryAdapter = new CountryAdapter(getContext(), countries);
        countrySpinner.setAdapter(countryAdapter);
    }
}