package com.andoresu.cryptoadmin.core.btccharges;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andoresu.cryptoadmin.R;
import com.andoresu.cryptoadmin.client.ErrorResponse;
import com.andoresu.cryptoadmin.core.btccharges.data.BtcCharge;
import com.andoresu.cryptoadmin.core.btccharges.data.BtcChargesResponse;
import com.andoresu.cryptoadmin.core.settingdetail.data.Setting;
import com.andoresu.cryptoadmin.list.RecyclerViewFragment;
import com.andoresu.cryptoadmin.utils.BaseFragment;
import com.andoresu.cryptoadmin.utils.PaginationScrollListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BtcChargesFragment  extends RecyclerViewFragment<BtcCharge> implements BtcChargesContract.View{

    String TAG = "CRYPTO_" + BtcChargesFragment.class.getSimpleName();

    private static final String ALL_CHARGES = "Todas las Recargas";
    private static final String PENDING_CHARGES = "Recargas Pendientes";
    private static final String ACCEPTED_CHARGES = "Recargas Aceptadas";
    private static final String SUCCESSFUL_CHARGES = "Recargas Exitosas";
    private static final String TO_VALIDATE_CHARGES = "Recargas Por Validar";
    private static final String DENIED_CHARGES = "Recargas Rechazadas";

    @BindView(R.id.chargeStateSpinner)
    MaterialSpinner chargeStateSpinner;

    private BtcChargesContract.UserActionsListener actionsListener;

    private BtcChargesContract.InteractionListener interactionListener;

    private String selectedItem = ALL_CHARGES;
    private BtcChargesResponse btcChargesResponse;

    public BtcChargesFragment(){
        actionsListener = new BtcChargesPresenter(this, getContext());
    }

    public static BtcChargesFragment newInstance(BtcChargesContract.InteractionListener interactionListener) {
        Bundle args = new Bundle();
        BtcChargesFragment fragment = new BtcChargesFragment();
        fragment.setArguments(args);
        fragment.setInteractionListener(interactionListener);
        fragment.setTitle("Recargas BTC");
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){}
    }

    @Override
    public void handleView() {
        super.handleView();
        viewAdapter = new BtcChargeAdapter(getContext(), item -> interactionListener.goToChargeDetail(item));
        listRecyclerView.setAdapter(viewAdapter);
        onRefresh();
        chargeStateSpinner.setItems(ALL_CHARGES, PENDING_CHARGES, ACCEPTED_CHARGES, SUCCESSFUL_CHARGES, TO_VALIDATE_CHARGES, DENIED_CHARGES);
        chargeStateSpinner.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view1, position, id, item) -> {
            selectedItem = item;
            onRefresh(true);
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_btc_charges;
    }

    @Override
    public void showCharges(BtcChargesResponse btcChargesResponse) {
        this.btcChargesResponse = btcChargesResponse;
        viewAdapter.addAll(btcChargesResponse.charges);
        isEmpty();
    }

    @Override
    public void onRefresh(boolean clear) {
        super.onRefresh(clear);
        if(clear){
            viewAdapter.set(new ArrayList<>());
        }
        actionsListener.getCharges(getOptions());
    }

    @Override
    public int getTotalItems() {
        return btcChargesResponse.totalCount;
    }

    @Override
    public void showProgressIndicator(boolean active) {
        showLoading(active);
    }

    private Map<String, String> getOptions(){
        Map<String, String> options = new HashMap<>();
        options.put("page", currentPage + "");
        switch (selectedItem){

            case PENDING_CHARGES:
                options.put("by_state", BtcCharge.STATE_PENDING);
                break;
            case ACCEPTED_CHARGES:
                options.put("by_state", BtcCharge.STATE_ACCEPTED);
                break;
            case SUCCESSFUL_CHARGES:
                options.put("by_state", BtcCharge.STATE_SUCCESSFUL);
                break;
            case TO_VALIDATE_CHARGES:
                options.put("by_state", BtcCharge.STATE_TO_VALIDATE);
                break;
            case DENIED_CHARGES:
                options.put("by_state", BtcCharge.STATE_DENIED);
                break;
        }
        return options;
    }

    public void setInteractionListener(BtcChargesContract.InteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }
}