package com.andoresu.cryptoadmin.core.btccharges;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andoresu.cryptoadmin.R;
import com.andoresu.cryptoadmin.core.btccharges.data.BtcCharge;
import com.andoresu.cryptoadmin.core.btccharges.data.BtcChargesResponse;
import com.andoresu.cryptoadmin.list.RecyclerViewAdapter;
import com.andoresu.cryptoadmin.utils.BaseRecyclerViewAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

import static com.andoresu.cryptoadmin.utils.MyUtils.HIDE_VIEW;
import static com.andoresu.cryptoadmin.utils.MyUtils.getText;


public class BtcChargeAdapter extends RecyclerViewAdapter<BtcCharge> {

    public static final String TAG = "CRYPTO_" + BtcChargeAdapter.class.getSimpleName();

    public BtcChargesResponse btcChargesResponse;

    public BtcChargeAdapter(Context context, @NonNull OnItemClickListener<BtcCharge> listener) {
        super(context, listener);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.item_charge;
    }

    @NonNull
    @Override
    public BaseViewHolder<BtcCharge> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(getLayoutResId(), parent, false);
        return new ChargeViewHolder(view);
    }

    @Override
    public void setData(BaseViewHolder<BtcCharge> holder, int position) {
        ChargeViewHolder viewHolder = (ChargeViewHolder) holder;
        BtcCharge btcCharge = getItem(position);
        viewHolder.personNameTextView.setText(getText(R.string.name_label, btcCharge.person.fullName));
        viewHolder.personCountryTextView.setText(getText(R.string.country_label , btcCharge.country.name));
        viewHolder.chargeDateTextView.setText(getText(R.string.date_label , btcCharge.getCreatedAt()));
        viewHolder.chargeAmountTextView.setText(getText(R.string.amount_label , btcCharge.getAmount()));
        viewHolder.chargeStateTextView.setText(getText(R.string.state_label , btcCharge.state));
    }

    public static class ChargeViewHolder extends BaseViewHolder<BtcCharge> {

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

        public void bind(final BtcCharge item, final OnItemClickListener<BtcCharge> listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(item));
        }
    }
}
