package com.andoresu.cryptoadmin.core.profile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.andoresu.cryptoadmin.R;
import com.andoresu.cryptoadmin.authorization.data.Admin;
import com.andoresu.cryptoadmin.authorization.data.User;
import com.andoresu.cryptoadmin.client.ErrorResponse;
import com.andoresu.cryptoadmin.core.charges.data.Charge;
import com.andoresu.cryptoadmin.core.profile.data.UserErrors;
import com.andoresu.cryptoadmin.utils.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.andoresu.cryptoadmin.utils.MyUtils.getFirst;

public class ProfileFragment extends BaseFragment implements ProfileContract.View{

    String TAG = "CRYPTO_" + ProfileFragment.class.getSimpleName();

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.userLayout)
    View userLayout;

    @BindView(R.id.userEmailTextInputLayout)
    TextInputLayout userEmailTextInputLayout;
    @BindView(R.id.adminNameTextInputLayout)
    TextInputLayout adminNameTextInputLayout;

    @BindView(R.id.userEmailEditText)
    EditText userEmailEditText;
    @BindView(R.id.adminNameEditText)
    EditText adminNameEditText;

    @BindView(R.id.userPasswordTextInputLayout)
    TextInputLayout userPasswordTextInputLayout;
    @BindView(R.id.userPasswordConfirmationTextInputLayout)
    TextInputLayout userPasswordConfirmationTextInputLayout;

    @BindView(R.id.userPasswordTextInputEditText)
    TextInputEditText userPasswordTextInputEditText;
    @BindView(R.id.userPasswordConfirmationTextInputEditText)
    TextInputEditText userPasswordConfirmationTextInputEditText;

    @BindView(R.id.saveUserButton)
    Button saveUserButton;
    @BindView(R.id.changeUserPasswordButton)
    Button changeUserPasswordButton;

    private ProfileContract.UserActionsListener actionsListener;

    private ProfileContract.InteractionListener interactionListener;

    private User user;

    public ProfileFragment(){}

    public static ProfileFragment newInstance(@NonNull ProfileContract.InteractionListener interactionListener) {
        Bundle args = new Bundle();
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        fragment.setTitle("Perfil");
        fragment.setInteractionListener(interactionListener);
        return fragment;
    }

    private void setInteractionListener(ProfileContract.InteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){}
        actionsListener = new ProfilePresenter(this, getContext());
    }

    @Override
    public void handleView() {
        actionsListener.getUser();
        saveUserButton.setOnClickListener(view1 -> actionsListener.updateUser(buildUser()));
        changeUserPasswordButton.setOnClickListener(view2 -> actionsListener.changeUserPassword(buildUser()));
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    public void showProgressIndicator(boolean active) {
        if(active){
            progressBar.setVisibility(View.VISIBLE);
            userLayout.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            userLayout.setVisibility(View.VISIBLE);
        }
    }

    private User buildUser(){
        user.email = userEmailEditText.getText().toString();
        user.profileType = User.TYPE_ADMIN;
        user.profile.type = User.TYPE_ADMIN;
        ((Admin)user.profile).name = adminNameEditText.getText().toString();
        user.password = userPasswordTextInputEditText.getText().toString();
        user.passwordConfirmation = userPasswordConfirmationTextInputEditText.getText().toString();
        return this.user;
    }

    @Override
    public void showGlobalError(ErrorResponse errorResponse) {}

    @Override
    public void onLogoutFinish() {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void showUser(User user) {
        this.user = user;
        userEmailEditText.setText(user.email);
        adminNameEditText.setText(user.getAdmin().name);
        userPasswordTextInputEditText.setText(user.password);
        userPasswordConfirmationTextInputEditText.setText(user.passwordConfirmation);
        interactionListener.updateUserName(user);
    }

    @Override
    public void showUserErrors(UserErrors userErrors) {
        userEmailTextInputLayout.setError(getFirst(userErrors.email));
        adminNameTextInputLayout.setError(getFirst(userErrors.profile.name));
        userPasswordTextInputLayout.setError(getFirst(userErrors.password));
        userPasswordConfirmationTextInputLayout.setError(getFirst(userErrors.passwordConfirmation));
    }

    @Override
    public void userUpdatedSuccessfully() {
        Toast.makeText(getContext(), "Perfil guardado correctamente", Toast.LENGTH_SHORT).show();
    }
}