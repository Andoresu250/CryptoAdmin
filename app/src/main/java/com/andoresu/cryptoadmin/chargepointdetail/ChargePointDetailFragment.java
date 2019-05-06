package com.andoresu.cryptoadmin.chargepointdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andoresu.cryptoadmin.R;
import com.andoresu.cryptoadmin.authorization.data.Country;
import com.andoresu.cryptoadmin.chargepointdetail.data.ChargePointErrors;
import com.andoresu.cryptoadmin.core.btcchargedetail.BtcChargeDetailPresenter;
import com.andoresu.cryptoadmin.core.chargepoints.CountryAdapter;
import com.andoresu.cryptoadmin.core.chargepoints.data.ChargePoint;
import com.andoresu.cryptoadmin.utils.BaseFragment;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.andoresu.cryptoadmin.utils.MyUtils.HIDE_VIEW;
import static com.andoresu.cryptoadmin.utils.MyUtils.getFirst;

public class ChargePointDetailFragment extends BaseFragment implements ChargePointDetailContrant.View {

    @BindView(R.id.accountOwnerTextInputLayout)
    public TextInputLayout accountOwnerTextInputLayout;

    @BindView(R.id.accountOwnerEditText)
    public EditText accountOwnerEditText;

    @BindView(R.id.accountBankTextInputLayout)
    public TextInputLayout accountBankTextInputLayout;

    @BindView(R.id.accountBankEditText)
    public EditText accountBankEditText;

    @BindView(R.id.accountNumberTextInputLayout)
    public TextInputLayout accountNumberTextInputLayout;

    @BindView(R.id.accountNumberEditText)
    public EditText accountNumberEditText;

    @BindView(R.id.countryLabelTextView)
    public TextView countryLabelTextView;

    @BindView(R.id.countrySpinner)
    public MaterialSpinner countrySpinner;

    @BindView(R.id.accountOwnerIdentificationTextInputLayout)
    public TextInputLayout accountOwnerIdentificationTextInputLayout;
    @BindView(R.id.accountOwnerIdentificationEditText)
    public EditText accountOwnerIdentificationEditText;
    @BindView(R.id.accountTypeTextInputLayout)
    public TextInputLayout accountTypeTextInputLayout;
    @BindView(R.id.accountTypeEditText)
    public EditText accountTypeEditText;
    @BindView(R.id.accountIbanTextInputLayout)
    public TextInputLayout accountIbanTextInputLayout;
    @BindView(R.id.accountIbanEditText)
    public EditText accountIbanEditText;

    @BindView(R.id.saveChargePointButton)
    public Button saveChargePointButton;

    @BindView(R.id.progressBar)
    public ProgressBar progressBar;

    @BindView(R.id.chargePointDetailLayout)
    public View chargePointDetailLayout;

    private ChargePointDetailContrant.UserActionsListener actionsListener;

    public ChargePoint chargePoint;

    private CountryAdapter countryAdapter;

    public ChargePointDetailFragment(){}

    public static ChargePointDetailFragment newInstance(ChargePoint chargePoint) {
        Bundle args = new Bundle();
        ChargePointDetailFragment fragment = new ChargePointDetailFragment();
        fragment.setArguments(args);
        fragment.chargePoint = chargePoint;
        if(chargePoint == null || chargePoint.id == null){
            fragment.setTitle("Nuevo Punto de carga");
        }else{
            fragment.setTitle("Detalle Punto de Carga");
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){}
        actionsListener = new ChargePointDetailPresenter(this, getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_charge_point_detail, container, false);
        setUnbinder(ButterKnife.bind(this, view));
        actionsListener.getCountries();
        saveChargePointButton.setOnClickListener(view1 -> {
            setChargePointData();
            if(chargePoint.id == null){
                actionsListener.createChargePoint(chargePoint);
            }else{
                actionsListener.updateChargePoint(chargePoint);
            }
        });
        setData();
        return view;
    }

    private void setData() {
        accountOwnerEditText.setText(chargePoint.owner);
        accountOwnerIdentificationEditText.setText(chargePoint.ownerIdentification);
        accountBankEditText.setText(chargePoint.bank);
        accountNumberEditText.setText(chargePoint.number);
        accountTypeEditText.setText(chargePoint.accountType);
        accountIbanEditText.setText(chargePoint.iban);
        if(countryAdapter != null && chargePoint.country != null){
            int index = countryAdapter.getItems().indexOf(chargePoint.country);
            if(index >= 0){
                countrySpinner.setSelectedIndex(index);
            }
        }
    }

    private void setChargePointData(){
        chargePoint.owner = accountOwnerEditText.getText().toString();
        chargePoint.ownerIdentification = accountOwnerIdentificationEditText.getText().toString();
        chargePoint.bank = accountBankEditText.getText().toString();
        chargePoint.number = accountNumberEditText.getText().toString();
        chargePoint.accountType = accountTypeEditText.getText().toString();
        chargePoint.iban = accountIbanEditText.getText().toString();
    }

    @Override
    public void showProgressIndicator(boolean active) {
        if(active){
            progressBar.setVisibility(View.VISIBLE);
            chargePointDetailLayout.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            chargePointDetailLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showChargePoint(ChargePoint chargePoint) {
        this.chargePoint = chargePoint;
        setTitle("Detalle Punto de Carga");
        setData();
    }

    @Override
    public void setCountries(List<Country> countries) {
        Country emptyCountry = new Country();
        emptyCountry.name = "Seleccione un pais";
        emptyCountry.id = null;
        countries.add(0, emptyCountry);
        countryAdapter = new CountryAdapter(getContext(), countries);
        countrySpinner.setAdapter(countryAdapter);
        countrySpinner.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<Country>) (view, position, id, item) -> {
            if(item.id != null){
                chargePoint.country = item;
                chargePoint.countryId = item.id;
            }
        });
        setData();
    }

    @Override
    public void showChargePointErrors(ChargePointErrors chargePointErrors) {
        if(chargePointErrors == null){
            return;
        }
        accountOwnerTextInputLayout.setError(getFirst(chargePointErrors.owner));
        accountBankTextInputLayout.setError(getFirst(chargePointErrors.bank));
        accountNumberTextInputLayout.setError(getFirst(chargePointErrors.number));
        accountOwnerIdentificationTextInputLayout.setError(getFirst(chargePointErrors.ownerIdentification));
        accountTypeTextInputLayout.setError(getFirst(chargePointErrors.accountType));
        accountIbanTextInputLayout.setError(getFirst(chargePointErrors.iban));
        countrySpinner.setError(getFirst(chargePointErrors.country));
    }
}
