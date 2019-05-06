package com.andoresu.cryptoadmin.core.btcchargedetail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.andoresu.cryptoadmin.core.btccharges.data.BtcCharge;
import com.andoresu.cryptoadmin.utils.BaseFragment;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

import static com.andoresu.cryptoadmin.utils.MyUtils.HIDE_VIEW;
import static com.andoresu.cryptoadmin.utils.MyUtils.galleryIntent;
import static com.andoresu.cryptoadmin.utils.MyUtils.getPathFromURI;
import static com.andoresu.cryptoadmin.utils.MyUtils.glideRequestOptions;

public class BtcChargeDetailFragment extends BaseFragment implements BtcChargeDetailContract.View{

    String TAG = "CRYPTO_" + BtcChargeDetailFragment.class.getSimpleName();

    private static final int IMAGE_PICKER_SELECT = 999;

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
    @BindView(R.id.chargeQRImageView)
    ImageView chargeQRImageView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.chargeDetailLayout)
    View chargeDetailLayout;

    @BindView(R.id.chargeApproveButton)
    Button chargeApproveButton;
    @BindView(R.id.chargeDenyButton)
    Button chargeDenyButton;
    @BindView(R.id.chargeCompleteButton)
    Button chargeCompleteButton;
    @BindView(R.id.addQRButton)
    Button addQRButton;

    @BindViews({R.id.personPhoneTextView, R.id.callButton, R.id.personBalanceTextView, R.id.personIdentificationFrontTextView, R.id.personIdentificationFrontImageView,
            R.id.personIdentificationBackTextView, R.id.personIdentificationBackImageView, R.id.personPublicReceiptTextView,
            R.id.personPublicReceiptImageView})
    List<View> viewsToHide;

    private BtcChargeDetailContract.UserActionsListener actionsListener;

    private BtcCharge charge;

    private Uri selectedImageUri;

    public BtcChargeDetailFragment(){

    }

    public static BtcChargeDetailFragment newInstance(BtcCharge charge) {
        Bundle args = new Bundle();
        BtcChargeDetailFragment fragment = new BtcChargeDetailFragment();
        fragment.setArguments(args);
        fragment.setCharge(charge);
        fragment.setTitle("Detalle Recarga BTC");
        return fragment;
    }

    private void setCharge(BtcCharge charge) {
        this.charge = charge;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){}
        actionsListener = new BtcChargeDetailPresenter(this, getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_btc_charge_detail, container, false);
        setUnbinder(ButterKnife.bind(this, view));
        ButterKnife.apply(viewsToHide, HIDE_VIEW);
        setData();
        return view;
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

        chargeApproveButton.setOnClickListener(view -> {
            if(selectedImageUri != null){
                actionsListener.approveCharge(charge, selectedImageUri);
            }else{
                Toast.makeText(getContext(), "Debe debe adjutar codigo QR para poder aceptar", Toast.LENGTH_SHORT).show();
            }
        });
        chargeDenyButton.setOnClickListener(view -> actionsListener.denyCharge(charge));
        addQRButton.setOnClickListener(view -> selectQRFile());
        chargeCompleteButton.setOnClickListener(view -> actionsListener.completeCharge(charge));

        chargeApproveButton.setVisibility(charge.canApprove() ? View.VISIBLE : View.GONE);
        chargeDenyButton.setVisibility(charge.canDeny() ? View.VISIBLE : View.GONE);
        chargeCompleteButton.setVisibility(charge.canComplete() ? View.VISIBLE : View.GONE);
        addQRButton.setVisibility(charge.canApprove() ? View.VISIBLE : View.GONE);

        Glide.with(this)
                .load(charge.evidence.url)
                .apply(glideRequestOptions(getContext()))
                .into(chargeEvidenceImageView);

        Glide.with(this)
                .load(charge.qr.url)
                .apply(glideRequestOptions(getContext()))
                .into(chargeQRImageView);

    }

    private void selectQRFile(){
        startActivityForResult(galleryIntent(), IMAGE_PICKER_SELECT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICKER_SELECT  && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data.getData();
            final String path = getPathFromURI(selectedImageUri, getContext());
            if (path != null) {
                File file = new File(path);
                Glide.with(this)
                        .load(Uri.fromFile(file))
                        .into(chargeQRImageView);
            }
        }

    }

    @Override
    public void showCharge(BtcCharge charge) {
        this.charge = charge;
        setData();
    }

}
