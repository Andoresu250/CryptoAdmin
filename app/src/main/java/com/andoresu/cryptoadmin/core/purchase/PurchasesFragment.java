package com.andoresu.cryptoadmin.core.purchase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andoresu.cryptoadmin.R;
import com.andoresu.cryptoadmin.client.ErrorResponse;
import com.andoresu.cryptoadmin.core.purchase.PurchaseAdapter;
import com.andoresu.cryptoadmin.core.purchase.PurchaseContract;
import com.andoresu.cryptoadmin.core.purchase.PurchasesFragment;
import com.andoresu.cryptoadmin.core.purchase.PurchasesPresenter;
import com.andoresu.cryptoadmin.core.purchase.data.Purchase;
import com.andoresu.cryptoadmin.core.purchase.data.PurchasesResponse;
import com.andoresu.cryptoadmin.utils.BaseFragment;
import com.andoresu.cryptoadmin.utils.PaginationScrollListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PurchasesFragment extends BaseFragment implements PurchaseContract.View, SwipeRefreshLayout.OnRefreshListener{

    String TAG = "CRYPTO_" + PurchasesFragment.class.getSimpleName();

    private static final int PAGE_START = 1;
    private boolean isLoading = false;

    private static final String ALL_PURCHASE = "Todas las Compras";
    private static final String PENDING_PURCHASE = "Compras Pendientes";
    private static final String APPROVED_PURCHASE = "Compras Aprobadas";
    private static final String DENIED_PURCHASE = "Compras Rechazadas";

    @BindView(R.id.purchaseStateSpinner)
    MaterialSpinner purchaseStateSpinner;

    @BindView(R.id.purchasesRecyclerView)
    RecyclerView purchasesRecyclerView;

    @BindView(R.id.purchasesSwipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private PurchaseContract.UserActionsListener actionsListener;

    private PurchaseContract.InteractionListener interactionListener;

    private String selectedItem = ALL_PURCHASE;

    private PurchaseAdapter purchaseAdapter;

    private LinearLayoutManager linearLayoutManager;

    private int currentPage = PAGE_START;

    public PurchasesFragment(){

    }

    public static PurchasesFragment newInstance(PurchaseContract.InteractionListener interactionListener) {
        Bundle args = new Bundle();
        PurchasesFragment fragment = new PurchasesFragment();
        fragment.setArguments(args);
        fragment.setInteractionListener(interactionListener);
        fragment.setTitle("Compras");
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){}
        actionsListener = new PurchasesPresenter(this, getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchases, container, false);
        setUnbinder(ButterKnife.bind(this, view));

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(this);
        linearLayoutManager = new LinearLayoutManager(this.getContext(), 1, false);
        purchasesRecyclerView.setLayoutManager(linearLayoutManager);
        purchaseAdapter = new PurchaseAdapter(getContext(), item -> interactionListener.goToPurchaseDetail(item));
        purchasesRecyclerView.setAdapter(purchaseAdapter);
        purchasesRecyclerView.addOnScrollListener(getPaginationScrollListener());

        actionsListener.getPurchases(getPurchasesOptions());
        purchaseStateSpinner.setItems(ALL_PURCHASE, PENDING_PURCHASE, APPROVED_PURCHASE, DENIED_PURCHASE);
        purchaseStateSpinner.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view1, position, id, item) -> {
            Snackbar.make(view1, "Cargando " + item, Snackbar.LENGTH_LONG).show();
            selectedItem = item;
            actionsListener.getPurchases(getPurchasesOptions());
        });
        return view;
    }

    @Override
    public void showPurchases(PurchasesResponse purchasesResponse) {
        this.purchaseAdapter.setPurchasesResponse(purchasesResponse);
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

    private Map<String, String> getPurchasesOptions(){
        Map<String, String> options = new HashMap<>();
        options.put("page", currentPage + "");
        switch (selectedItem){
            case PENDING_PURCHASE:
                options.put("by_state", Purchase.STATE_PENDING);
                break;
            case APPROVED_PURCHASE:
                options.put("by_state", Purchase.STATE_APPROVED);
                break;
            case DENIED_PURCHASE:
                options.put("by_state", Purchase.STATE_DENIED);
                break;
        }
        return options;
    }

    @Override
    public void onRefresh() {
        actionsListener.getPurchases(getPurchasesOptions());
    }

    public void setInteractionListener(PurchaseContract.InteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }

    private PaginationScrollListener getPaginationScrollListener(){
        return new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                actionsListener.getPurchases(getPurchasesOptions());
            }

            @Override
            public int getTotalPageCount() {
                return purchaseAdapter.purchasesResponse.getTotalPage();
            }

            @Override
            public boolean isLastPage() {
                return currentPage >= purchaseAdapter.purchasesResponse.getTotalPage();
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        };
    }
}