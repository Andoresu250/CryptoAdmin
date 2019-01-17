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
import com.andoresu.cryptoadmin.client.ErrorResponse;
import com.andoresu.cryptoadmin.core.charges.ChargesContract;
import com.andoresu.cryptoadmin.core.charges.ChargesFragment;
import com.andoresu.cryptoadmin.core.chargedetail.ChargeDetailFragment;
import com.andoresu.cryptoadmin.core.charges.data.Charge;
import com.andoresu.cryptoadmin.core.saledetail.SaleDetailFragment;
import com.andoresu.cryptoadmin.core.sales.SalesContract;
import com.andoresu.cryptoadmin.core.sales.SalesFragment;
import com.andoresu.cryptoadmin.core.sales.data.Sale;
import com.andoresu.cryptoadmin.core.userdetail.UserDetailFragment;
import com.andoresu.cryptoadmin.core.users.UsersContract;
import com.andoresu.cryptoadmin.core.users.UsersFragment;
import com.andoresu.cryptoadmin.security.SecureData;
import com.andoresu.cryptoadmin.utils.BaseActivity;
import com.andoresu.cryptoadmin.utils.BaseFragment;

import javax.security.auth.login.LoginException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        MainContract.View,
        UsersContract.InteractionListener,
        ChargesContract.InteractionListener,
        SalesContract.InteractionListener {

    String TAG = "CRYPTO_" + MainActivity.class.getSimpleName();

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

//    @BindView(R.id.fab)
//    FloatingActionButton fab;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    private MainContract.UserActionsListener actionsListener;

    private HeaderViewHolder headerViewHolder;

    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        setTitle(R.string.app_name);

        user = (User) getIntent().getSerializableExtra(User.NAME);

        headerViewHolder = new HeaderViewHolder(navigationView.getHeaderView(0));
        headerViewHolder.navHeaderTextView.setText(user.profile.fullName);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        actionsListener = new MainPresenter(this, this, SecureData.getToken());

        setSalesFragment();
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
            case R.id.navSales:
                setSalesFragment();
                break;
            case R.id.navPurchases:
//                TODO: go to purchases
                Toast.makeText(this, "Funcion en progreso", Toast.LENGTH_SHORT).show();
                break;
            case R.id.navSettings:
//                TODO: go to settings
                Toast.makeText(this, "Funcion en progreso", Toast.LENGTH_SHORT).show();
                break;
            case R.id.navProfile:
//                TODO: go to profile
                Toast.makeText(this, "Funcion en progreso", Toast.LENGTH_SHORT).show();
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
    }

    private void setUsersFragment(){
        setTitle("Usuarios");
        UsersFragment usersFragment = UsersFragment.newInstance(this);
        changeFragment(usersFragment);
    }

    private void setUserDetailFragment(User user){
        setTitle("Detalle Usuario");
        UserDetailFragment userDetailFragment = UserDetailFragment.newInstance(user);
        changeFragment(userDetailFragment);
    }

    private void setChargesFragment(){
        setTitle("Recargas");
        ChargesFragment chargesFragment = ChargesFragment.newInstance(this);
        changeFragment(chargesFragment);
    }

    private void setChargeDetailFragment(Charge charge){
        setTitle("Detalle Recarga");
        ChargeDetailFragment chargeDetailFragment = ChargeDetailFragment.newInstance(charge);
        changeFragment(chargeDetailFragment);
    }

    private void setSalesFragment(){
        setTitle("Ventas");
        SalesFragment salesFragment = SalesFragment.newInstance(this);
        changeFragment(salesFragment);
    }

    private void setSaleDetailFragment(Sale charge){
        setTitle("Detalle Venta");
        SaleDetailFragment chargeDetailFragment = SaleDetailFragment.newInstance(charge);
        changeFragment(chargeDetailFragment);
    }

    @Override
    public void goToUserDetail(User user) {
        setUserDetailFragment(user);
    }

    @Override
    public void goToChargeDetail(Charge charge) {
        Log.e(TAG, "goToChargeDetail: puta madre");
        setChargeDetailFragment(charge);
    }

    @Override
    public void goToSaleDetail(Sale sale) {
        setSaleDetailFragment(sale);
    }

    protected static class HeaderViewHolder {

        @BindView(R.id.navHeaderTextView)
        TextView navHeaderTextView;

        HeaderViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}
