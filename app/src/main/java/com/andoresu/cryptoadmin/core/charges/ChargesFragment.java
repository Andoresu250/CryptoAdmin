package com.andoresu.cryptoadmin.core.charges;

import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.andoresu.cryptoadmin.core.charges.data.Charge;
import com.andoresu.cryptoadmin.core.charges.data.ChargesResponse;
import com.andoresu.cryptoadmin.core.users.UserAdapter;
import com.andoresu.cryptoadmin.utils.BaseFragment;
import com.andoresu.cryptoadmin.utils.BaseRecyclerViewAdapter;
import com.andoresu.cryptoadmin.utils.PaginationScrollListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChargesFragment extends BaseFragment implements ChargesContract.View, SwipeRefreshLayout.OnRefreshListener{

    String TAG = "CRYPTO_" + ChargesFragment.class.getSimpleName();

    private static final int PAGE_START = 1;
    private boolean isLoading = false;

    private static final String ALL_CHARGES = "Todas las Recargas";
    private static final String PENDING_CHARGES = "Recargas Pendientes";
    private static final String APPROVED_CHARGES = "Recargas Aprobadas";
    private static final String DENIED_CHARGES = "Recargas Rechazadas";

    @BindView(R.id.chargeStateSpinner)
    MaterialSpinner chargeStateSpinner;

    @BindView(R.id.chargesRecyclerView)
    RecyclerView chargesRecyclerView;

    @BindView(R.id.chargesSwipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private ChargesContract.UserActionsListener actionsListener;

    private ChargesContract.InteractionListener interactionListener;

    private String selectedItem = ALL_CHARGES;

    private ChargeAdapter chargeAdapter;

    private LinearLayoutManager linearLayoutManager;

    private int currentPage = PAGE_START;

    public ChargesFragment(){
        actionsListener = new ChargesPresenter(this, getContext());
    }

    public static ChargesFragment newInstance(ChargesContract.InteractionListener interactionListener) {
        Bundle args = new Bundle();
        ChargesFragment fragment = new ChargesFragment();
        fragment.setArguments(args);
        fragment.setInteractionListener(interactionListener);
        fragment.setTitle("Recargas");
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
        View view = inflater.inflate(R.layout.fragment_charges, container, false);
        setUnbinder(ButterKnife.bind(this, view));

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(this);
        linearLayoutManager = new LinearLayoutManager(this.getContext(), 1, false);
        chargesRecyclerView.setLayoutManager(linearLayoutManager);
        chargeAdapter = new ChargeAdapter(getContext(), item -> interactionListener.goToChargeDetail(item));
        chargesRecyclerView.setAdapter(chargeAdapter);
        chargesRecyclerView.addOnScrollListener(getPaginationScrollListener());

        actionsListener.getCharges(getChargesOptions());
        chargeStateSpinner.setItems(ALL_CHARGES, PENDING_CHARGES, APPROVED_CHARGES, DENIED_CHARGES);
        chargeStateSpinner.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view1, position, id, item) -> {
            Snackbar.make(view1, "Cargando " + item, Snackbar.LENGTH_LONG).show();
            selectedItem = item;
            actionsListener.getCharges(getChargesOptions());
        });
        return view;
    }

    @Override
    public void showCharges(ChargesResponse chargesResponse) {
        this.chargeAdapter.clear();
        this.chargeAdapter.setChargesResponse(chargesResponse);
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
                options.put("by_state", Charge.STATE_PENDING);
                break;
            case APPROVED_CHARGES:
                options.put("by_state", Charge.STATE_APPROVED);
                break;
            case DENIED_CHARGES:
                options.put("by_state", Charge.STATE_DENIED);
                break;
        }
        return options;
    }

    @Override
    public void onRefresh() {
        actionsListener.getCharges(getChargesOptions());
    }

    public void setInteractionListener(ChargesContract.InteractionListener interactionListener) {
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
                return chargeAdapter.chargesResponse.getTotalPage();
            }

            @Override
            public boolean isLastPage() {
                return currentPage >= chargeAdapter.chargesResponse.getTotalPage();
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        };
    }
}