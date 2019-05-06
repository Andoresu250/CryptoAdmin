package com.andoresu.cryptoadmin.core.chargepoints;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.andoresu.cryptoadmin.R;
import com.andoresu.cryptoadmin.authorization.data.Country;
import com.andoresu.cryptoadmin.core.chargepoints.data.ChargePoint;
import com.andoresu.cryptoadmin.core.charges.ChargeAdapter;
import com.andoresu.cryptoadmin.core.charges.data.Charge;
import com.andoresu.cryptoadmin.utils.BaseRecyclerViewAdapter;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChargePointAdapter extends BaseRecyclerViewAdapter<ChargePoint> {

    public ChargePointAdapter(Context context, @NonNull ArrayList<ChargePoint> items, OnItemClickListener<ChargePoint> listener) {
        super(context, items, listener);
    }

    public ChargePointAdapter(Context context, OnItemClickListener<ChargePoint> listener) {
        super(context, listener);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.item_charge_point;
    }

    @NonNull
    @Override
    public BaseViewHolder<ChargePoint> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(getLayoutResId(), parent, false);
        return new ChargePointViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<ChargePoint> holder, int position) {
        super.onBindViewHolder(holder, position);
        ChargePointViewHolder viewHolder = (ChargePointViewHolder) holder;
        ChargePoint chargePoint = getItem(position);
        viewHolder.detailChargePointButton.setOnClickListener(view -> listener.onItemClick(chargePoint));
        viewHolder.accountOwnerEditText.setText(chargePoint.owner);
        viewHolder.accountBankEditText.setText(chargePoint.bank);
        viewHolder.accountNumberEditText.setText(chargePoint.number);
        List<Country> countries = new ArrayList<>();
        countries.add(chargePoint.country);
        viewHolder.countrySpinner.setAdapter(new CountryAdapter(context, countries));
        viewHolder.countrySpinner.setSelectedIndex(0);
    }

    public static class ChargePointViewHolder extends BaseViewHolder<ChargePoint> {

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
        public View accountOwnerIdentificationEditText;
        @BindView(R.id.accountTypeTextInputLayout)
        public TextInputLayout accountTypeTextInputLayout;
        @BindView(R.id.accountTypeEditText)
        public View accountTypeEditText;
        @BindView(R.id.accountIbanTextInputLayout)
        public TextInputLayout accountIbanTextInputLayout;
        @BindView(R.id.accountIbanEditText)
        public EditText accountIbanEditText;

        @BindView(R.id.detailChargePointButton)
        public Button detailChargePointButton;



        public ChargePointViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            accountOwnerTextInputLayout.setEnabled(false);
            accountOwnerTextInputLayout.setFocusable(false);
            accountOwnerEditText.setEnabled(false);
            accountOwnerEditText.setFocusable(false);
            accountBankTextInputLayout.setEnabled(false);
            accountBankTextInputLayout.setFocusable(false);
            accountBankEditText.setEnabled(false);
            accountBankEditText.setFocusable(false);
            accountNumberTextInputLayout.setEnabled(false);
            accountNumberTextInputLayout.setFocusable(false);
            accountNumberEditText.setEnabled(false);
            accountNumberEditText.setFocusable(false);
            countrySpinner.setEnabled(false);
            countrySpinner.setFocusable(false);
            accountOwnerIdentificationTextInputLayout.setVisibility(View.GONE);
            accountOwnerIdentificationEditText.setVisibility(View.GONE);
            accountTypeTextInputLayout.setVisibility(View.GONE);
            accountTypeEditText.setVisibility(View.GONE);
            accountIbanTextInputLayout.setVisibility(View.GONE);
            accountIbanEditText.setVisibility(View.GONE);
        }
    }
}
