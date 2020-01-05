package com.andoresu.cryptoadmin.core.saledetail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.CircularProgressDrawable;
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
import com.andoresu.cryptoadmin.core.sales.data.Sale;
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
import static com.andoresu.cryptoadmin.utils.MyUtils.getPathFromURI;
import static com.andoresu.cryptoadmin.utils.MyUtils.glideRequestOptions;

public class SaleDetailFragment extends BaseFragment implements SaleDetailContract.View{

    String TAG = "CRYPTO_" + SaleDetailFragment.class.getSimpleName();

    private static final int IMAGE_PICKER_SELECT = 999;

    @BindView(R.id.personNameTextView)
    TextView personNameTextView;
    @BindView(R.id.personIdentificationTextView)
    TextView personIdentificationTextView;
    @BindView(R.id.personIdentificationTypeTextView)
    TextView personIdentificationTypeTextView;
    @BindView(R.id.personCountryTextView)
    TextView personCountryTextView;

    @BindView(R.id.bankNameTextView)
    TextView bankNameTextView;
    @BindView(R.id.bankNumberTextView)
    TextView bankNumberTextView;
    @BindView(R.id.bankOwnerNameTextView)
    TextView bankOwnerNameTextView;
    @BindView(R.id.bankOwnerIdentificationTextView)
    TextView bankOwnerIdentificationTextView;
    @BindView(R.id.bankOwnerDocumentTypeTextView)
    TextView bankOwnerDocumentTypeTextView;

    @BindView(R.id.saleDateTextView)
    TextView saleDateTextView;
    @BindView(R.id.saleValueTextView)
    TextView saleValueTextView;
    @BindView(R.id.saleBtcTextView)
    TextView saleBtcTextView;
    @BindView(R.id.saleStateTextView)
    TextView saleStateTextView;
    @BindView (R.id.saleTransferEvidenceImageView)
    ImageView saleTransferEvidenceImageView;
    @BindView (R.id.saleDepositEvidenceImageView)
    ImageView saleDepositEvidenceImageView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.saleDetailLayout)
    View saleDetailLayout;

    @BindView(R.id.selectSaleEvidenceButton)
    Button selectSaleEvidenceButton;
    @BindView(R.id.saleApproveButton)
    Button saleApproveButton;
    @BindView(R.id.saleDenyButton)
    Button saleDenyButton;

    @BindViews({R.id.personPhoneTextView, R.id.callButton, R.id.personBalanceTextView, R.id.personIdentificationFrontTextView, R.id.personIdentificationFrontImageView,
            R.id.personIdentificationBackTextView, R.id.personIdentificationBackImageView, R.id.personPublicReceiptTextView,
            R.id.personPublicReceiptImageView})
    List<View> viewsToHide;

    private SaleDetailContract.UserActionsListener actionsListener;

    private Sale sale;

    private Uri selectedImageUri;

    public SaleDetailFragment(){}

    public static SaleDetailFragment newInstance(Sale sale) {
        Bundle args = new Bundle();
        SaleDetailFragment fragment = new SaleDetailFragment();
        fragment.setArguments(args);
        fragment.setSale(sale);
        fragment.setTitle("Detalle Venta");
        return fragment;
    }

    private void setSale(Sale sale) {
        this.sale = sale;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){}
        actionsListener = new SaleDetailPresenter(this, getContext());
    }

    @Override
    public void handleView() {
        ButterKnife.apply(viewsToHide, HIDE_VIEW);
        setData();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_sale_detail;
    }

    @Override
    public void showProgressIndicator(boolean active) {
        saleApproveButton.setEnabled(!active);
        saleDenyButton.setEnabled(!active);
        if(active){
            progressBar.setVisibility(View.VISIBLE);
            saleDetailLayout.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            saleDetailLayout.setVisibility(View.VISIBLE);
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
        Person person = sale.person;
        saleStateTextView.setText(getText(R.string.state_label, sale.state));
        personNameTextView.setText(getText(R.string.name_label, person.fullName));
        personIdentificationTextView.setText(getText(R.string.identification_label, person.identification));
        personIdentificationTypeTextView.setText(getText(R.string.identification_type_label, person.documentType.name));
        personCountryTextView.setText(getText(R.string.country_label, sale.country.name));

        bankNameTextView.setText(getText(R.string.bank_label, sale.bankAccount.bank));
        bankNumberTextView.setText(getText(R.string.bank_number_label, sale.bankAccount.number));
        bankOwnerNameTextView.setText(getText(R.string.bank_owner_name, sale.bankAccount.ownerName));
        bankOwnerIdentificationTextView.setText(getText(R.string.bank_owner_identification_label, sale.bankAccount.identification));
        bankOwnerDocumentTypeTextView.setText(getText(R.string.bank_owner_document_type_label, sale.bankAccount.documentType.name));

        saleDateTextView.setText(getText(R.string.date_label, sale.getCreatedAt()));
        saleValueTextView.setText(getText(R.string.value_label, sale.getValue()));
        saleBtcTextView.setText(getText(R.string.btc_label, sale.btc ));
        saleDenyButton.setOnClickListener(view -> actionsListener.denySale(sale));

        selectSaleEvidenceButton.setOnClickListener(view -> selectEvidenceFile());

        if(sale.isPending()){
            saleApproveButton.setVisibility(View.VISIBLE);
            saleDenyButton.setVisibility(View.VISIBLE);
            selectSaleEvidenceButton.setVisibility(View.VISIBLE);
        }else{
            saleApproveButton.setVisibility(View.GONE);
            saleDenyButton.setVisibility(View.GONE);
            selectSaleEvidenceButton.setVisibility(View.GONE);
        }

        Glide.with(this)
                .load(sale.transferEvidence.url)
                .apply(glideRequestOptions(getContext()))
                .into(saleTransferEvidenceImageView);

        Glide.with(this)
                .load(sale.depositEvidence.url)
                .apply(glideRequestOptions(getContext()))
                .into(saleDepositEvidenceImageView);

        saleApproveButton.setOnClickListener(view -> {
            if(selectedImageUri != null){
                actionsListener.approveSale(sale, selectedImageUri);
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
                        .into(saleDepositEvidenceImageView);
            }
        }

    }

    @Override
    public void showSale(Sale sale) {
        this.sale = sale;
        setData();
    }
}

