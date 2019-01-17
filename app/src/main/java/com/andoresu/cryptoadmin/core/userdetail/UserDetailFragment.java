package com.andoresu.cryptoadmin.core.userdetail;

import android.annotation.SuppressLint;
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
import com.andoresu.cryptoadmin.authorization.data.User;
import com.andoresu.cryptoadmin.client.ErrorResponse;
import com.andoresu.cryptoadmin.utils.BaseFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.andoresu.cryptoadmin.utils.MyUtils.phoneIntent;

public class UserDetailFragment extends BaseFragment implements UserDetailContract.View{

    String TAG = "CRYPTO_" + UserDetailFragment.class.getSimpleName();

    @BindView(R.id.userEmailTextView)
    TextView userEmailTextView;
    @BindView(R.id.userStateTextView)
    TextView userStateTextView;
    @BindView(R.id.personNameTextView)
    TextView personNameTextView;
    @BindView(R.id.personIdentificationTextView)
    TextView personIdentificationTextView;
    @BindView(R.id.personIdentificationTypeTextView)
    TextView personIdentificationTypeTextView;
    @BindView(R.id.personCountryTextView)
    TextView personCountryTextView;
    @BindView(R.id.personPhoneTextView)
    TextView personPhoneTextView;
    @BindView(R.id.personBalanceTextView)
    TextView personBalanceTextView;
    @BindView(R.id.personIdentificationFrontTextView)
    TextView personIdentificationFrontTextView;
    @BindView(R.id.personIdentificationBackTextView)
    TextView personIdentificationBackTextView;
    @BindView(R.id.personPublicReceiptTextView)
    TextView personPublicReceiptTextView;

    @BindView (R.id.personIdentificationFrontImageView)
    ImageView personIdentificationFrontImageView;
    @BindView (R.id.personIdentificationBackImageView)
    ImageView personIdentificationBackImageView;
    @BindView (R.id.personPublicReceiptImageView)
    ImageView personPublicReceiptImageView;

    @BindView(R.id.callButton)
    Button callButton;

    @BindView(R.id.changeStateButton)
    Button changeStateButton;

    @BindView(R.id.deleteUserButton)
    Button deleteUserButton;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.userDetailLayout)
    View userDetailLayout;

    private UserDetailContract.UserActionsListener actionsListener;

    private User user;

    public UserDetailFragment(){}

    public static UserDetailFragment newInstance(User user) {
        Bundle args = new Bundle();
        UserDetailFragment fragment = new UserDetailFragment();
        fragment.setArguments(args);
        fragment.setUser(user);
        fragment.setTitle("Detalle Usuario");
        return fragment;
    }

    private void setUser(User user) {
        this.user = user;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){}
        actionsListener = new UserDetailPresenter(this, getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_detail, container, false);
        setUnbinder(ButterKnife.bind(this, view));
        setData();
        return view;
    }

    @Override
    public void showProgressIndicator(boolean active) {
        if(active){
            progressBar.setVisibility(View.VISIBLE);
            userDetailLayout.setVisibility(View.GONE);
            changeStateButton.setEnabled(false);
        }else{
            progressBar.setVisibility(View.GONE);
            userDetailLayout.setVisibility(View.VISIBLE);
            changeStateButton.setEnabled(true);
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
        Person person = (Person) user.profile;
        userEmailTextView.setText(getText(R.string.email_label, user.email));
        userStateTextView.setText(getText(R.string.state_label, user.state));
        personNameTextView.setText(getText(R.string.name_label, person.fullName));
        personIdentificationTextView.setText(getText(R.string.identification_label, person.identification));
        personIdentificationTypeTextView.setText(getText(R.string.identification_type_label, person.documentType.name));
        personCountryTextView.setText(getText(R.string.country_label, person.country.name));
        personPhoneTextView.setText(getText(R.string.phone_label, person.getPhone()));
        personBalanceTextView.setText(getText(R.string.balance_label, person.getBalance()));

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.imagen_disponible);
        requestOptions.error(R.drawable.imagen_disponible);

        Glide.with(this)
                .load(person.identificationFront.url)
                .apply(requestOptions)
                .into(personIdentificationFrontImageView);

        Glide.with(this)
                .load(person.identificationBack.url)
                .apply(requestOptions)
                .into(personIdentificationBackImageView);

        Glide.with(this)
                .load(person.publicReceipt.url)
                .apply(requestOptions)
                .into(personPublicReceiptImageView);

        callButton.setOnClickListener(view -> startActivity(phoneIntent(person.getPhone())));
        changeStateButton.setTextColor(getResources().getColor(R.color.white));
        if(user.isActivated()){
            changeStateButton.setBackgroundColor(getResources().getColor(R.color.button_alert_background));
            changeStateButton.setText(R.string.deactivate_user);
            changeStateButton.setOnClickListener(view -> actionsListener.deActivateUser(user));
        }else{
            changeStateButton.setBackgroundColor(getResources().getColor(R.color.button_background));
            changeStateButton.setText(R.string.activate_user);
            changeStateButton.setOnClickListener(view -> actionsListener.activateUser(user));
        }
        deleteUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionsListener.deleteUser(user);
            }
        });

    }

    @Override
    public void showUser(User user) {
        this.user = user;
        setData();
    }

    @Override
    public void backToUsers() {
        if(getActivity() != null){
            getActivity().onBackPressed();
        }
    }
}
