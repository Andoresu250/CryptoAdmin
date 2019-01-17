package com.andoresu.cryptoadmin.core.purchasedetail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import com.andoresu.cryptoadmin.core.purchase.data.Purchase;
import com.andoresu.cryptoadmin.utils.BaseFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

import static com.andoresu.cryptoadmin.utils.MyUtils.HIDE_VIEW;
import static com.andoresu.cryptoadmin.utils.MyUtils.galleryIntent;
import static com.andoresu.cryptoadmin.utils.MyUtils.getBitmapFromCameraData;
import static com.andoresu.cryptoadmin.utils.MyUtils.getPathFromURI;

public class PurchaseDetailFragment extends BaseFragment implements PurchaseDetailContract.View{

    String TAG = "CRYPTO_" + PurchaseDetailFragment.class.getSimpleName();

    private static final int IMAGE_PICKER_SELECT = 999;

    @BindView(R.id.personNameTextView)
    TextView personNameTextView;
    @BindView(R.id.personIdentificationTextView)
    TextView personIdentificationTextView;
    @BindView(R.id.personIdentificationTypeTextView)
    TextView personIdentificationTypeTextView;
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
    @BindView(R.id.purchaseWalletUrlTextView)
    TextView purchaseWalletUrlTextView;
    @BindView (R.id.purchaseEvidenceImageView)
    ImageView purchaseEvidenceImageView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.purchaseDetailLayout)
    View purchaseDetailLayout;

    @BindView(R.id.selectPurchaseEvidenceButton)
    Button selectPurchaseEvidenceButton;
    @BindView(R.id.purchaseApproveButton)
    Button purchaseApproveButton;
    @BindView(R.id.purchaseDenyButton)
    Button purchaseDenyButton;

    @BindViews({R.id.personPhoneTextView, R.id.callButton, R.id.personBalanceTextView, R.id.personIdentificationFrontTextView, R.id.personIdentificationFrontImageView,
            R.id.personIdentificationBackTextView, R.id.personIdentificationBackImageView, R.id.personPublicReceiptTextView,
            R.id.personPublicReceiptImageView})
    List<View> viewsToHide;

    private PurchaseDetailContract.UserActionsListener actionsListener;

    private Purchase purchase;

    private Uri selectedImageUri;

    public PurchaseDetailFragment(){}

    public static PurchaseDetailFragment newInstance(Purchase purchase) {
        Bundle args = new Bundle();
        PurchaseDetailFragment fragment = new PurchaseDetailFragment();
        fragment.setArguments(args);
        fragment.setPurchase(purchase);
        fragment.setTitle("Detalle Compra");
        return fragment;
    }

    private void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){}
        actionsListener = new PurchaseDetailPresenter(this, getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase_detail, container, false);
        setUnbinder(ButterKnife.bind(this, view));
        ButterKnife.apply(viewsToHide, HIDE_VIEW);
        setData();
        return view;
    }

    @Override
    public void showProgressIndicator(boolean active) {
        purchaseApproveButton.setEnabled(!active);
        purchaseDenyButton.setEnabled(!active);
        if(active){
            progressBar.setVisibility(View.VISIBLE);
            purchaseDetailLayout.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            purchaseDetailLayout.setVisibility(View.VISIBLE);
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
        Person person = purchase.person;

        personNameTextView.setText(getText(R.string.name_label, person.fullName));
        personIdentificationTextView.setText(getText(R.string.identification_label, person.identification));
        personIdentificationTypeTextView.setText(getText(R.string.identification_type_label, person.documentType.name));
        personCountryTextView.setText(getText(R.string.country_label, purchase.country.name));

        purchaseStateTextView.setText(getText(R.string.state_label, purchase.state));
        purchaseWalletUrlTextView.setText(getText(R.string.wallet_url_label, purchase.walletUrl));
        purchaseDateTextView.setText(getText(R.string.date_label, purchase.getCreatedAt()));
        purchaseValueTextView.setText(getText(R.string.value_label, purchase.getValue()));
        purchaseBtcTextView.setText(getText(R.string.btc_label, purchase.btc ));
        purchaseDenyButton.setOnClickListener(view -> actionsListener.denyPurchase(purchase));

        selectPurchaseEvidenceButton.setOnClickListener(view -> selectEvidenceFile());

        if(purchase.isPending()){
            purchaseApproveButton.setVisibility(View.VISIBLE);
            purchaseDenyButton.setVisibility(View.VISIBLE);
            selectPurchaseEvidenceButton.setVisibility(View.VISIBLE);
        }else{
            purchaseApproveButton.setVisibility(View.GONE);
            purchaseDenyButton.setVisibility(View.GONE);
            selectPurchaseEvidenceButton.setVisibility(View.GONE);
        }

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.imagen_disponible);
        requestOptions.error(R.drawable.imagen_disponible);

        Glide.with(this)
                .load(purchase.evidence)
                .apply(requestOptions)
                .into(purchaseEvidenceImageView);

        purchaseApproveButton.setOnClickListener(view -> {
            if(selectedImageUri != null){
                actionsListener.approvePurchase(purchase, selectedImageUri);
            }else{
                Toast.makeText(getContext(), "Debe debe adjutar la evidencia para poder aprobar", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void selectEvidenceFile(){
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
                        .into(purchaseEvidenceImageView);
            }
        }

    }

    @Override
    public void showPurchase(Purchase purchase) {
        this.purchase = purchase;
        setData();
    }
}

