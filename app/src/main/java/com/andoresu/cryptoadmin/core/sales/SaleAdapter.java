package com.andoresu.cryptoadmin.core.sales;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andoresu.cryptoadmin.R;
import com.andoresu.cryptoadmin.core.sales.data.Sale;
import com.andoresu.cryptoadmin.core.sales.data.SalesResponse;
import com.andoresu.cryptoadmin.utils.BaseRecyclerViewAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

import static com.andoresu.cryptoadmin.utils.MyUtils.HIDE_VIEW;
import static com.andoresu.cryptoadmin.utils.MyUtils.getText;

public class SaleAdapter extends BaseRecyclerViewAdapter<Sale> {

    public static final String TAG = "CRYPTO_" + SaleAdapter.class.getSimpleName();

    public SalesResponse salesResponse;

    public SaleAdapter(Context context, @NonNull OnItemClickListener<Sale> listener) {
        super(context, listener);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.item_sale;
    }

    @NonNull
    @Override
    public BaseViewHolder<Sale> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(getLayoutResId(), parent, false);
        return new SaleAdapter.SaleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<Sale> holder, int position) {
        super.onBindViewHolder(holder, position);
        SaleAdapter.SaleViewHolder viewHolder = (SaleAdapter.SaleViewHolder) holder;
        Sale sale = getItem(position);
        viewHolder.personNameTextView.setText(getText(context, R.string.name_label, sale.person.fullName));
        viewHolder.personCountryTextView.setText(getText(context, R.string.country_label , sale.country.name));
        viewHolder.saleDateTextView.setText(getText(context, R.string.date_label , sale.getCreatedAt()));
        viewHolder.saleValueTextView.setText(getText(context, R.string.amount_label , sale.getValue()));
        viewHolder.saleBtcTextView.setText(getText(context, R.string.btc_label, sale.btc + ""));
        viewHolder.saleStateTextView.setText(getText(context, R.string.state_label , sale.state));
    }

    public void setSalesResponse(SalesResponse salesResponse){
        this.salesResponse = salesResponse;
        addAll(salesResponse.sales);
    }

    public static class SaleViewHolder extends BaseViewHolder<Sale> {

        @BindViews({R.id.personIdentificationTextView, R.id.personIdentificationTypeTextView, R.id.personPhoneTextView,
                R.id.callButton, R.id.personBalanceTextView, R.id.personIdentificationFrontTextView, R.id.personIdentificationFrontImageView,
                R.id.personIdentificationBackTextView, R.id.personIdentificationBackImageView, R.id.personPublicReceiptTextView,
                R.id.personPublicReceiptImageView, R.id.saleDepositEvidenceImageView, R.id.saleDepositEvidenceTextView, R.id.saleTransferEvidenceImageView, R.id.saleTransferEvidenceTextView})
        List<View> viewsToHide;

        @BindView(R.id.personNameTextView)
        TextView personNameTextView;
        @BindView(R.id.personCountryTextView)
        TextView personCountryTextView;
        @BindView(R.id.saleDateTextView)
        TextView saleDateTextView;
        @BindView(R.id.saleValueTextView)
        TextView saleValueTextView;
        @BindView(R.id.saleBtcTextView)
        TextView saleBtcTextView;
        @BindView(R.id.saleStateTextView)
        TextView saleStateTextView;


        public SaleViewHolder(View v){
            super(v);
            ButterKnife.bind(this, v);
            ButterKnife.apply(viewsToHide, HIDE_VIEW);
        }

        public void bind(final Sale item, final OnItemClickListener<Sale> listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(item));
        }
    }
}
