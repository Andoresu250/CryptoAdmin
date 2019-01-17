package com.andoresu.cryptoadmin.core.purchase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andoresu.cryptoadmin.R;
import com.andoresu.cryptoadmin.core.purchase.data.Purchase;
import com.andoresu.cryptoadmin.core.purchase.data.PurchasesResponse;
import com.andoresu.cryptoadmin.utils.BaseRecyclerViewAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

import static com.andoresu.cryptoadmin.utils.MyUtils.HIDE_VIEW;
import static com.andoresu.cryptoadmin.utils.MyUtils.getText;

public class PurchaseAdapter extends BaseRecyclerViewAdapter<Purchase> {

    public static final String TAG = "CRYPTO_" + PurchaseAdapter.class.getSimpleName();

    public PurchasesResponse purchasesResponse;

    public PurchaseAdapter(Context context, @NonNull OnItemClickListener<Purchase> listener) {
        super(context, listener);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.item_purchase;
    }

    @NonNull
    @Override
    public BaseViewHolder<Purchase> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(getLayoutResId(), parent, false);
        return new PurchaseAdapter.PurchaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<Purchase> holder, int position) {
        super.onBindViewHolder(holder, position);
        PurchaseAdapter.PurchaseViewHolder viewHolder = (PurchaseAdapter.PurchaseViewHolder) holder;
        Purchase purchase = getItem(position);
        viewHolder.personNameTextView.setText(getText(context, R.string.name_label, purchase.person.fullName));
        viewHolder.personCountryTextView.setText(getText(context, R.string.country_label , purchase.country.name));
        viewHolder.purchaseDateTextView.setText(getText(context, R.string.date_label , purchase.getCreatedAt()));
        viewHolder.purchaseValueTextView.setText(getText(context, R.string.amount_label , purchase.getValue()));
        viewHolder.purchaseBtcTextView.setText(getText(context, R.string.btc_label, purchase.btc + ""));
        viewHolder.purchaseStateTextView.setText(getText(context, R.string.state_label , purchase.state));
    }

    public void setPurchasesResponse(PurchasesResponse purchasesResponse){
        this.purchasesResponse = purchasesResponse;
        addAll(purchasesResponse.purchases);
    }

    public static class PurchaseViewHolder extends BaseViewHolder<Purchase> {

        @BindViews({R.id.personIdentificationTextView, R.id.personIdentificationTypeTextView, R.id.personPhoneTextView,
                R.id.callButton, R.id.personBalanceTextView, R.id.personIdentificationFrontTextView, R.id.personIdentificationFrontImageView,
                R.id.personIdentificationBackTextView, R.id.personIdentificationBackImageView, R.id.personPublicReceiptTextView,
                R.id.personPublicReceiptImageView, R.id.purchaseEvidenceImageView, R.id.purchaseEvidenceTextView, R.id.purchaseWalletUrlTextView})
        List<View> viewsToHide;

        @BindView(R.id.personNameTextView)
        TextView personNameTextView;
        @BindView(R.id.personCountryTextView)
        TextView personCountryTextView;
        @BindView(R.id.purchaseDateTextView)
        TextView purchaseDateTextView;
        @BindView(R.id.purchaseValueTextView)
        TextView purchaseValueTextView;
        @BindView(R.id.purchaseBtcTextView)
        TextView purchaseBtcTextView;
        @BindView(R.id.purchaseStateTextView)
        TextView purchaseStateTextView;


        public PurchaseViewHolder(View v){
            super(v);
            ButterKnife.bind(this, v);
            ButterKnife.apply(viewsToHide, HIDE_VIEW);
        }

        public void bind(final Purchase item, final OnItemClickListener<Purchase> listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(item));
        }
    }
}
