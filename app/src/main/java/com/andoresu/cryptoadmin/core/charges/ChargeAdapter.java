package com.andoresu.cryptoadmin.core.charges;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andoresu.cryptoadmin.R;
import com.andoresu.cryptoadmin.core.charges.data.Charge;
import com.andoresu.cryptoadmin.core.charges.data.ChargesResponse;
import com.andoresu.cryptoadmin.utils.BaseRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

import static com.andoresu.cryptoadmin.utils.MyUtils.HIDE_VIEW;
import static com.andoresu.cryptoadmin.utils.MyUtils.getText;


public class ChargeAdapter extends BaseRecyclerViewAdapter<Charge> {

    public static final String TAG = "CRYPTO_" + ChargeAdapter.class.getSimpleName();

    public ChargesResponse chargesResponse;

    public ChargeAdapter(Context context, @NonNull OnItemClickListener<Charge> listener) {
        super(context, listener);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.item_charge;
    }

    @NonNull
    @Override
    public BaseViewHolder<Charge> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(getLayoutResId(), parent, false);
        return new ChargeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<Charge> holder, int position) {
        super.onBindViewHolder(holder, position);
        ChargeViewHolder viewHolder = (ChargeViewHolder) holder;
        Charge charge = getItem(position);
        viewHolder.personNameTextView.setText(getText(context, R.string.name_label, charge.person.fullName));
        viewHolder.personCountryTextView.setText(getText(context, R.string.country_label , charge.country.name));
        viewHolder.chargeDateTextView.setText(getText(context, R.string.date_label , charge.getCreatedAt()));
        viewHolder.chargeAmountTextView.setText(getText(context, R.string.amount_label , charge.getAmount()));
        viewHolder.chargeStateTextView.setText(getText(context, R.string.state_label , charge.state));
    }

    public void setChargesResponse(ChargesResponse chargesResponse){
        this.chargesResponse = chargesResponse;
        addAll(chargesResponse.charges);
    }

    public static class ChargeViewHolder extends BaseViewHolder<Charge> {

        @BindViews({R.id.personIdentificationTextView, R.id.personIdentificationTypeTextView, R.id.personPhoneTextView,
                R.id.callButton, R.id.personBalanceTextView, R.id.personIdentificationFrontTextView, R.id.personIdentificationFrontImageView,
                R.id.personIdentificationBackTextView, R.id.personIdentificationBackImageView, R.id.personPublicReceiptTextView,
                R.id.personPublicReceiptImageView, R.id.chargeEvidenceImageView, R.id.chargeEvidenceTextView})
        List<View> viewsToHide;

        @BindView(R.id.personNameTextView)
        TextView personNameTextView;
        @BindView(R.id.personCountryTextView)
        TextView personCountryTextView;
        @BindView(R.id.chargeDateTextView)
        TextView chargeDateTextView;
        @BindView(R.id.chargeAmountTextView)
        TextView chargeAmountTextView;
        @BindView(R.id.chargeStateTextView)
        TextView chargeStateTextView;


        public ChargeViewHolder(View v){
            super(v);
            ButterKnife.bind(this, v);
            ButterKnife.apply(viewsToHide, HIDE_VIEW);
        }

        public void bind(final Charge item, final OnItemClickListener<Charge> listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(item));
        }
    }
}
