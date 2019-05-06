package com.andoresu.cryptoadmin.core;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andoresu.cryptoadmin.R;
import com.andoresu.cryptoadmin.authorization.data.User;
import com.andoresu.cryptoadmin.authorization.login.LoginActivity;
import com.andoresu.cryptoadmin.chargepointdetail.ChargePointDetailContrant;
import com.andoresu.cryptoadmin.chargepointdetail.ChargePointDetailFragment;
import com.andoresu.cryptoadmin.client.ErrorResponse;
import com.andoresu.cryptoadmin.core.btcchargedetail.BtcChargeDetailFragment;
import com.andoresu.cryptoadmin.core.btccharges.BtcChargesContract;
import com.andoresu.cryptoadmin.core.btccharges.BtcChargesFragment;
import com.andoresu.cryptoadmin.core.btccharges.data.BtcCharge;
import com.andoresu.cryptoadmin.core.chargepoints.ChargePointsContract;
import com.andoresu.cryptoadmin.core.chargepoints.ChargePointsFragment;
import com.andoresu.cryptoadmin.core.chargepoints.data.ChargePoint;
import com.andoresu.cryptoadmin.core.charges.ChargesContract;
import com.andoresu.cryptoadmin.core.charges.ChargesFragment;
import com.andoresu.cryptoadmin.core.chargedetail.ChargeDetailFragment;
import com.andoresu.cryptoadmin.core.charges.data.Charge;
import com.andoresu.cryptoadmin.core.charges.data.SettingErrors;
import com.andoresu.cryptoadmin.core.noticedetail.NoticeDetailFragment;
import com.andoresu.cryptoadmin.core.notices.NoticesContract;
import com.andoresu.cryptoadmin.core.notices.NoticesFragment;
import com.andoresu.cryptoadmin.core.notices.data.Notice;
import com.andoresu.cryptoadmin.core.profile.ProfileContract;
import com.andoresu.cryptoadmin.core.profile.ProfileFragment;
import com.andoresu.cryptoadmin.core.purchasedetail.PurchaseDetailFragment;
import com.andoresu.cryptoadmin.core.purchase.PurchaseContract;
import com.andoresu.cryptoadmin.core.purchase.PurchasesFragment;
import com.andoresu.cryptoadmin.core.purchase.data.Purchase;
import com.andoresu.cryptoadmin.core.sales.SaleContract;
import com.andoresu.cryptoadmin.core.sales.SalesFragment;
import com.andoresu.cryptoadmin.core.sales.data.Sale;
import com.andoresu.cryptoadmin.core.saledetail.SaleDetailFragment;
import com.andoresu.cryptoadmin.core.settings.SettingFragment;
import com.andoresu.cryptoadmin.core.userdetail.UserDetailFragment;
import com.andoresu.cryptoadmin.core.users.UsersContract;
import com.andoresu.cryptoadmin.core.users.UsersFragment;
import com.andoresu.cryptoadmin.security.SecureData;
import com.andoresu.cryptoadmin.utils.BaseActivity;
import com.andoresu.cryptoadmin.utils.BaseFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.andoresu.cryptoadmin.client.GsonBuilderUtils.getUserGson;

public class MainActivity extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        MainContract.View,
        UsersContract.InteractionListener,
        ChargesContract.InteractionListener,
        PurchaseContract.InteractionListener,
        SaleContract.InteractionListener,
        ProfileContract.InteractionListener, NoticesContract.InteractionListener, BtcChargesContract.InteractionListener, ChargePointsContract.InteractionListener {

    String TAG = "CRYPTO_" + MainActivity.class.getSimpleName();

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    private MainContract.UserActionsListener actionsListener;

    private HeaderViewHolder headerViewHolder;

    private User user;

    private BaseFragment currentFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        setTitle(R.string.app_name);

        user = (User) getIntent().getSerializableExtra(User.NAME);

        headerViewHolder = new HeaderViewHolder(navigationView.getHeaderView(0));
        setUserNameToMenu();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        actionsListener = new MainPresenter(this, this, SecureData.getToken());

        setChargePointsFragment();

    }

    private void setUserNameToMenu(){
        headerViewHolder.navHeaderTextView.setText(user.profile.fullName);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.clear();

        menu.add(MENU_GROUP_ID, ITEM_MENU_LOGOUT_ID, Menu.NONE, getText(R.string.logout));

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case ITEM_MENU_LOGOUT_ID:
                actionsListener.logout();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.navUsers:
                setUsersFragment();
                break;
            case R.id.navCharges:
                setChargesFragment();
                break;
            case R.id.navBtcCharges:
                setBtcChargesFragment();
                break;
            case R.id.navSale:
                setSalesFragment();
                break;
            case R.id.navPurchase:
                setPurchasesFragment();
                break;
            case R.id.navNotices:
                setNoticesFragment();
                break;
            case R.id.navSettings:
                setSettingFragment();
                break;
            case R.id.navChargePoints:
                setChargePointsFragment();
                break;
            case R.id.navProfile:
                setProfileFragment();
                break;
            case R.id.navLogout:
                actionsListener.logout();
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void showProgressIndicator(boolean active) {
        progressBar.setVisibility( active ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showGlobalError(ErrorResponse errorResponse) {

    }

    @Override
    public void onLogoutFinish() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void changeFragment(BaseFragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainFragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        setTitle(fragment.getTitle());
        currentFragment = fragment;
    }

    private void setUsersFragment(){
        UsersFragment usersFragment = UsersFragment.newInstance(this);
        changeFragment(usersFragment);
    }

    private void setUserDetailFragment(User user){
        UserDetailFragment userDetailFragment = UserDetailFragment.newInstance(user);
        changeFragment(userDetailFragment);
    }

    private void setChargesFragment(){
        ChargesFragment chargesFragment = ChargesFragment.newInstance(this);
        changeFragment(chargesFragment);
    }

    private void setChargeDetailFragment(Charge charge){
        ChargeDetailFragment chargeDetailFragment = ChargeDetailFragment.newInstance(charge);
        changeFragment(chargeDetailFragment);
    }

    private void setChargePointsFragment(){
        ChargePointsFragment fragment = ChargePointsFragment.newInstance(this);
        changeFragment(fragment);
    }

    private void setChargePointDetailFragment(ChargePoint chargePoint){
        ChargePointDetailFragment fragment = ChargePointDetailFragment.newInstance(chargePoint);
        changeFragment(fragment);
    }

    private void setBtcChargesFragment(){
        BtcChargesFragment chargesFragment = BtcChargesFragment.newInstance(this);
        changeFragment(chargesFragment);
    }

    private void setBtcChargeDetailFragment(BtcCharge charge){
        BtcChargeDetailFragment chargeDetailFragment = BtcChargeDetailFragment.newInstance(charge);
        changeFragment(chargeDetailFragment);
    }

    private void setPurchasesFragment(){
        PurchasesFragment purchasesFragment = PurchasesFragment.newInstance(this);
        changeFragment(purchasesFragment);
    }

    private void setNoticesFragment(){
        NoticesFragment noticesFragment = NoticesFragment.newInstance(this);
        changeFragment(noticesFragment);
    }

    private void setPurchaseDetailFragment(Purchase purchase){
        PurchaseDetailFragment purchaseDetailFragment = PurchaseDetailFragment.newInstance(purchase);
        changeFragment(purchaseDetailFragment);
    }

    private void setSalesFragment(){
        SalesFragment purchasesFragment = SalesFragment.newInstance(this);
        changeFragment(purchasesFragment);
    }

    private void setSaleDetailFragment(Sale sale){
        SaleDetailFragment saleDetailFragment = SaleDetailFragment.newInstance(sale);
        changeFragment(saleDetailFragment);
    }

    private void setNoticeDetailFragment(Notice notice){
        NoticeDetailFragment noticeDetailFragment = NoticeDetailFragment.newInstance(notice);
        changeFragment(noticeDetailFragment);
    }

    private void setSettingFragment(){
        SettingFragment settingFragment = SettingFragment.newInstance();
        changeFragment(settingFragment);
    }

    private void setProfileFragment(){
        ProfileFragment profileFragment = ProfileFragment.newInstance(this);
        changeFragment(profileFragment);
    }

    @Override
    public void goToUserDetail(User user) {
        setUserDetailFragment(user);
    }

    @Override
    public void goToChargeDetail(Charge charge) {
        setChargeDetailFragment(charge);
    }

    @Override
    public void goToPurchaseDetail(Purchase purchase) {
        setPurchaseDetailFragment(purchase);
    }

    @Override
    public void goToSaleDetail(Sale sale) {
        setSaleDetailFragment(sale);
    }

    @Override
    public void updateUserName(User user) {
        this.user = user;
        setUserNameToMenu();
    }

    @Override
    public void goToNoticeDetail(Notice notice) {
        setNoticeDetailFragment(notice);
    }

    @Override
    public void goToChargeDetail(BtcCharge btcCharge) {
        setBtcChargeDetailFragment(btcCharge);
    }

    @Override
    public void goToChargePoint(ChargePoint chargePoint) {
        setChargePointDetailFragment(chargePoint);
    }


    protected static class HeaderViewHolder {

        @BindView(R.id.navHeaderTextView)
        TextView navHeaderTextView;

        HeaderViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}
