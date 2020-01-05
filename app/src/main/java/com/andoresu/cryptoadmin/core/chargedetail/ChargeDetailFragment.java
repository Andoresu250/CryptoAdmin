package com.andoresu.cryptoadmin.core.chargedetail;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andoresu.cryptoadmin.R;
import com.andoresu.cryptoadmin.authorization.data.Person;
import com.andoresu.cryptoadmin.client.ErrorResponse;
import com.andoresu.cryptoadmin.core.charges.data.Charge;
import com.andoresu.cryptoadmin.utils.BaseFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

import static com.andoresu.cryptoadmin.utils.MyUtils.HIDE_VIEW;
import static com.andoresu.cryptoadmin.utils.MyUtils.glideRequestOptions;
import static com.andoresu.cryptoadmin.utils.MyUtils.phoneIntent;

public class ChargeDetailFragment extends BaseFragment implements ChargeDetailContract.View{

    String TAG = "CRYPTO_" + ChargeDetailFragment.class.getSimpleName();

    @BindView(R.id.personNameTextView)
    TextView personNameTextView;
    @BindView(R.id.personIdentificationTextView)
    TextView personIdentificationTextView;
    @BindView(R.id.personIdentificationTypeTextView)
    TextView personIdentificationTypeTextView;
    @BindView(R.id.personCountryTextView)
    TextView personCountryTextView;

    @BindView(R.id.chargeDateTextView)
    TextView chargeDateTextView;
    @BindView(R.id.chargeAmountTextView)
    TextView chargeAmountTextView;
    @BindView(R.id.chargeStateTextView)
    TextView chargeStateTextView;
    @BindView (R.id.chargeEvidenceImageView)
    ImageView chargeEvidenceImageView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.chargeDetailLayout)
    View chargeDetailLayout;

    @BindView(R.id.chargeApproveButton)
    Button chargeApproveButton;
    @BindView(R.id.chargeDenyButton)
    Button chargeDenyButton;

    @BindViews({R.id.personPhoneTextView, R.id.callButton, R.id.personBalanceTextView, R.id.personIdentificationFrontTextView, R.id.personIdentificationFrontImageView,
            R.id.personIdentificationBackTextView, R.id.personIdentificationBackImageView, R.id.personPublicReceiptTextView,
            R.id.personPublicReceiptImageView})
    List<View> viewsToHide;

    private ChargeDetailContract.UserActionsListener actionsListener;

    private Charge charge;

    public ChargeDetailFragment(){

    }

    public static ChargeDetailFragment newInstance(Charge charge) {
        Bundle args = new Bundle();
        ChargeDetailFragment fragment = new ChargeDetailFragment();
        fragment.setArguments(args);
        fragment.setCharge(charge);
        fragment.setTitle("Detalle Recarga");
        return fragment;
    }

    private void setCharge(Charge charge) {
        this.charge = charge;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){}
        actionsListener = new ChargeDetailPresenter(this, getContext());
    }

    @Override
    public void handleView() {
        ButterKnife.apply(viewsToHide, HIDE_VIEW);
        setData();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_charge_detail;
    }

    @Override
    public void showProgressIndicator(boolean active) {
        chargeApproveButton.setEnabled(!active);
        chargeDenyButton.setEnabled(!active);
        if(active){
            progressBar.setVisibility(View.VISIBLE);
            chargeDetailLayout.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            chargeDetailLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showGlobalError(ErrorResponse errorResponse) {
        Toast.makeText(getContext(), errorResponse.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLogoutFinish() {

    }

    @SuppressLint("CheckResult")
    private void setData(){
        Person person = charge.person;
        chargeStateTextView.setText(getText(R.string.state_label, charge.state));
        personNameTextView.setText(getText(R.string.name_label, person.fullName));
        personIdentificationTextView.setText(getText(R.string.identification_label, person.identification));
        personIdentificationTypeTextView.setText(getText(R.string.identification_type_label, person.documentType.name));
        personCountryTextView.setText(getText(R.string.country_label, charge.country.name));

        chargeDateTextView.setText(getText(R.string.date_label, charge.getCreatedAt()));
        chargeAmountTextView.setText(getText(R.string.amount_label, charge.getAmount()));

        chargeApproveButton.setOnClickListener(view -> actionsListener.approveCharge(charge));
        chargeDenyButton.setOnClickListener(view -> actionsListener.denyCharge(charge));

        if(charge.isPending()){
            chargeApproveButton.setVisibility(View.VISIBLE);
            chargeDenyButton.setVisibility(View.VISIBLE);
        }else{
            chargeApproveButton.setVisibility(View.GONE);
            chargeDenyButton.setVisibility(View.GONE);
        }

        Glide.with(this)
                .load(charge.evidence.url)
                .apply(glideRequestOptions(getContext()))
                .into(chargeEvidenceImageView);

    }

    @Override
    public void showCharge(Charge charge) {
        this.charge = charge;
        setData();
    }

}
