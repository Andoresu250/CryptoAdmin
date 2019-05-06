package com.andoresu.cryptoadmin.core.sales;

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
import com.andoresu.cryptoadmin.core.sales.data.Sale;
import com.andoresu.cryptoadmin.core.sales.data.SalesResponse;
import com.andoresu.cryptoadmin.utils.BaseFragment;
import com.andoresu.cryptoadmin.utils.PaginationScrollListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SalesFragment extends BaseFragment implements SaleContract.View, SwipeRefreshLayout.OnRefreshListener{

    String TAG = "CRYPTO_" + SalesFragment.class.getSimpleName();

    private static final int PAGE_START = 1;
    private boolean isLoading = false;

    private static final String ALL_SALES = "Todas las Ventas";
    private static final String PENDING_SALES = "Ventas Pendientes";
    private static final String APPROVED_SALES = "Ventas Aprobadas";
    private static final String DENIED_SALES = "Ventas Rechazadas";

    @BindView(R.id.saleStateSpinner)
    MaterialSpinner saleStateSpinner;

    @BindView(R.id.salesRecyclerView)
    RecyclerView salesRecyclerView;

    @BindView(R.id.salesSwipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private SaleContract.UserActionsListener actionsListener;

    private SaleContract.InteractionListener interactionListener;

    private String selectedItem = ALL_SALES;

    private SaleAdapter saleAdapter;

    private LinearLayoutManager linearLayoutManager;

    private int currentPage = PAGE_START;

    public SalesFragment(){
        actionsListener = new SalesPresenter(this, getContext());
    }

    public static SalesFragment newInstance(SaleContract.InteractionListener interactionListener) {
        Bundle args = new Bundle();
        SalesFragment fragment = new SalesFragment();
        fragment.setArguments(args);
        fragment.setInteractionListener(interactionListener);
        fragment.setTitle("Ventas");
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
        View view = inflater.inflate(R.layout.fragment_sales, container, false);
        setUnbinder(ButterKnife.bind(this, view));

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(this);
        linearLayoutManager = new LinearLayoutManager(this.getContext(), 1, false);
        salesRecyclerView.setLayoutManager(linearLayoutManager);
        saleAdapter = new SaleAdapter(getContext(), item -> interactionListener.goToSaleDetail(item));
        salesRecyclerView.setAdapter(saleAdapter);
        salesRecyclerView.addOnScrollListener(getPaginationScrollListener());

        actionsListener.getSales(getSalesOptions());
        saleStateSpinner.setItems(ALL_SALES, PENDING_SALES, APPROVED_SALES, DENIED_SALES);
        saleStateSpinner.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view1, position, id, item) -> {
            currentPage = 1;
            saleAdapter.set(new ArrayList<>());
            Snackbar.make(view1, "Cargando " + item, Snackbar.LENGTH_LONG).show();
            selectedItem = item;
            actionsListener.getSales(getSalesOptions());
        });
        return view;
    }

    @Override
    public void showSales(SalesResponse salesResponse) {
        this.saleAdapter.setSalesResponse(salesResponse);
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

    private Map<String, String> getSalesOptions(){
        Map<String, String> options = new HashMap<>();
        options.put("page", currentPage + "");
        switch (selectedItem){
            case PENDING_SALES:
                options.put("by_state", Sale.STATE_PENDING);
                break;
            case APPROVED_SALES:
                options.put("by_state", Sale.STATE_APPROVED);
                break;
            case DENIED_SALES:
                options.put("by_state", Sale.STATE_DENIED);
                break;
        }
        return options;
    }

    @Override
    public void onRefresh() {
        actionsListener.getSales(getSalesOptions());
    }

    public void setInteractionListener(SaleContract.InteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }

    private PaginationScrollListener getPaginationScrollListener(){
        return new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                actionsListener.getSales(getSalesOptions());
            }

            @Override
            public int getTotalPageCount() {
                return saleAdapter.salesResponse.getTotalPage();
            }

            @Override
            public boolean isLastPage() {
                return currentPage >= saleAdapter.salesResponse.getTotalPage();
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        };
    }
}