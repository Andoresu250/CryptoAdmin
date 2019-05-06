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
import com.andoresu.cryptoadmin.utils.BaseFragment;
import com.andoresu.cryptoadmin.utils.PaginationScrollListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BtcChargesFragment extends BaseFragment implements BtcChargesContract.View, SwipeRefreshLayout.OnRefreshListener{

    String TAG = "CRYPTO_" + BtcChargesFragment.class.getSimpleName();

    private static final int PAGE_START = 1;
    private boolean isLoading = false;

    private static final String ALL_CHARGES = "Todas las Recargas";
    private static final String PENDING_CHARGES = "Recargas Pendientes";
    private static final String ACCEPTED_CHARGES = "Recargas Aceptadas";
    private static final String SUCCESSFUL_CHARGES = "Recargas Exitosas";
    private static final String TO_VALIDATE_CHARGES = "Recargas Por Validar";
    private static final String DENIED_CHARGES = "Recargas Rechazadas";

    @BindView(R.id.chargeStateSpinner)
    MaterialSpinner chargeStateSpinner;

    @BindView(R.id.chargesRecyclerView)
    RecyclerView chargesRecyclerView;

    @BindView(R.id.chargesSwipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private BtcChargesContract.UserActionsListener actionsListener;

    private BtcChargesContract.InteractionListener interactionListener;

    private String selectedItem = ALL_CHARGES;

    private BtcChargeAdapter btcChargeAdapter;

    private LinearLayoutManager linearLayoutManager;

    private int currentPage = PAGE_START;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_btc_charges, container, false);
        setUnbinder(ButterKnife.bind(this, view));

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(this);
        linearLayoutManager = new LinearLayoutManager(this.getContext(), 1, false);
        chargesRecyclerView.setLayoutManager(linearLayoutManager);
        btcChargeAdapter = new BtcChargeAdapter(getContext(), item -> interactionListener.goToChargeDetail(item));
        chargesRecyclerView.setAdapter(btcChargeAdapter);
        chargesRecyclerView.addOnScrollListener(getPaginationScrollListener());

        actionsListener.getCharges(getChargesOptions());
        chargeStateSpinner.setItems(ALL_CHARGES, PENDING_CHARGES, ACCEPTED_CHARGES, SUCCESSFUL_CHARGES, TO_VALIDATE_CHARGES, DENIED_CHARGES);
        chargeStateSpinner.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view1, position, id, item) -> {
            currentPage = 1;
            btcChargeAdapter.set(new ArrayList<>());
            Snackbar.make(view1, "Cargando " + item, Snackbar.LENGTH_LONG).show();
            selectedItem = item;
            actionsListener.getCharges(getChargesOptions());
        });
        return view;
    }

    @Override
    public void showCharges(BtcChargesResponse btcChargesResponse) {
        for (BtcCharge charge : btcChargesResponse.charges){
            Log.i(TAG, "showCharges: " + charge.id);
        }
        this.btcChargeAdapter.setBtcChargesResponse(btcChargesResponse);
    }

    @Override
    public void showProgressIndicator(boolean active) {
        swipeRefreshLayout.setRefreshing(active);
        isLoading = active;
    }

    @Override
    public void showGlobalError(ErrorResponse errorResponse) {

    }

    @Override
    public void onLogoutFinish() {

    }

    private Map<String, String> getChargesOptions(){
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

    @Override
    public void onRefresh() {
        actionsListener.getCharges(getChargesOptions());
    }

    public void setInteractionListener(BtcChargesContract.InteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }

    private PaginationScrollListener getPaginationScrollListener(){
        return new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                actionsListener.getCharges(getChargesOptions());
            }

            @Override
            public int getTotalPageCount() {
                return btcChargeAdapter.btcChargesResponse.getTotalPage();
            }

            @Override
            public boolean isLastPage() {
                return currentPage >= btcChargeAdapter.btcChargesResponse.getTotalPage();
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        };
    }
}