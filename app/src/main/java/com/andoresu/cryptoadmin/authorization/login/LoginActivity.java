package com.andoresu.cryptoadmin.authorization.login;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andoresu.cryptoadmin.R;
import com.andoresu.cryptoadmin.authorization.data.LoginUser;
import com.andoresu.cryptoadmin.authorization.data.User;
import com.andoresu.cryptoadmin.client.ErrorResponse;
import com.andoresu.cryptoadmin.core.MainActivity;
import com.andoresu.cryptoadmin.utils.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

import static com.andoresu.cryptoadmin.utils.MyUtils.checkNullEmpty;
import static com.andoresu.cryptoadmin.utils.MyUtils.closeKeyboard;
import static com.andoresu.cryptoadmin.utils.MyUtils.showErrorDialog;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

@SuppressLint("LogNotTimber")
public class LoginActivity extends BaseActivity
        implements LoginContract.View, View.OnClickListener, TextView.OnEditorActionListener
        {

    private final static String TAG = "CRYPTO_" + LoginActivity.class.getSimpleName();

    //View Objects
    @BindView(R.id.userNameEditText)
    EditText usernameEditText;
    @BindView(R.id.passwordEditText)
    EditText passwordEditText;
    @BindView(R.id.userTextInputLayout)
    TextInputLayout usernameTextInputLayout;
    @BindView(R.id.passwordTextInputLayout)
    TextInputLayout passwordTextInputLayout;
    @BindView(R.id.loginBtn)
    Button loginButton;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.loginLayout)
    View loginLayout;

    private LoginContract.UserActionsListener actionsListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        actionsListener = new LoginPresenter(this, this);

        loginButton.setOnClickListener(this);

        passwordEditText.setOnEditorActionListener(this);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            usernameEditText.setText(bundle.getString("usernameEditText", usernameEditText.getText().toString()));
            passwordEditText.setText(bundle.getString("passwordEditText", passwordEditText.getText().toString()));
            User user = (User) bundle.getSerializable("user");
            boolean logout = bundle.getBoolean("logout");
            if(user != null && logout){
                actionsListener.logout(user);
            }
            showIntentError(bundle);
        }

        actionsListener.checkSession();
    }

    private void showIntentError(@NonNull Bundle bundle){
        String msg = bundle.getString("intentError", null);
        if(!checkNullEmpty(msg)){
           showErrorDialog(this, msg);
        }
    }

    @Override
    public void showProgressIndicator(boolean active) {
        if(active){
            progressBar.setVisibility(View.VISIBLE);
            loginLayout.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            loginLayout.setVisibility(View.VISIBLE);
            loginButton.setEnabled(true);
        }
    }

    @Override
    public void showUsernameError(String error) {
        usernameTextInputLayout.setError(error);
    }

    @Override
    public void showPasswordError(String error) {
        passwordTextInputLayout.setError(error);
    }

    @Override
    public void showUserTypeError() {
        showErrorDialog(this, getString(R.string.error_user_type));
    }

    @Override
    public void showUserSavedError() {
        showErrorDialog(this, getString(R.string.error_user_saved));
    }

    @Override
    public void showTokenSaveError() {
        showErrorDialog(this, getString(R.string.error_token_save));
    }

    @Override
    public void showLoginError() {
        showErrorDialog(this, getString(R.string.error_user));
    }

    @Override
    public void onLoginFinish() {
        loginButton.setEnabled(true);
    }

    @Override
    public void onLoginSuccess(User user) {
        nextActivity(user);
    }

    @Override
    public void showGlobalError(ErrorResponse errorResponse){
        showErrorDialog(this, errorResponse);
    }

    @Override
    public void onLogoutFinish() {

    }

    @OnClick({R.id.loginBtn})
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.loginBtn:
                closeKeyboard(view);
                sendLoginRequest();
                break;
        }
    }

    @OnEditorAction({R.id.passwordEditText})
    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

        switch (textView.getId()){
            case R.id.passwordEditText:
                //close keyboard
                closeKeyboard(textView);
                sendLoginRequest();
                break;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    private void sendLoginRequest(){
        usernameTextInputLayout.setError(null);
        passwordTextInputLayout.setError(null);
        loginButton.setEnabled(false);
        LoginUser loginUser =
                new LoginUser(usernameEditText.getText().toString(), passwordEditText.getText().toString());
        actionsListener.sendLoginRequest(loginUser);
    }

    private Bundle getInstanceBundle(){
        Bundle bundle = new Bundle();
        bundle.putString("usernameEditText", usernameEditText.getText().toString());
        bundle.putString("passwordEditText", passwordEditText.getText().toString());
        return bundle;
    }

    private void nextActivity(@NonNull User user){
        Intent intent = null;
        Log.i(TAG, "nextActivity: user.profileType " + user.profileType);
        switch (user.profileType){
            case User.TYPE_ADMIN:
                intent = new Intent(this, MainActivity.class);
                break;
            default:
                actionsListener.logout(user);
                break;
        }
        if(intent != null){
            Bundle bundle = new Bundle();
            bundle.putSerializable(User.NAME, user);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
    }
}
