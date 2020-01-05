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
import com.andoresu.cryptoadmin.list.RecyclerViewFragment;
import com.andoresu.cryptoadmin.utils.BaseFragment;
import com.andoresu.cryptoadmin.utils.PaginationScrollListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SalesFragment extends RecyclerViewFragment<Sale> implements SaleContract.View, SwipeRefreshLayout.OnRefreshListener{

    String TAG = "CRYPTO_" + SalesFragment.class.getSimpleName();

    private static final String ALL_SALES = "Todas las Ventas";
    private static final String PENDING_SALES = "Ventas Pendientes";
    private static final String APPROVED_SALES = "Ventas Aprobadas";
    private static final String DENIED_SALES = "Ventas Rechazadas";

    @BindView(R.id.saleStateSpinner)
    MaterialSpinner saleStateSpinner;

    private SaleContract.UserActionsListener actionsListener;

    private SaleContract.InteractionListener interactionListener;

    private String selectedItem = ALL_SALES;

    private SalesResponse salesResponse;

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

    @Override
    public void handleView() {
        super.handleView();
        viewAdapter = new SaleAdapter(getContext(), item -> interactionListener.goToSaleDetail(item));
        listRecyclerView.setAdapter(viewAdapter);
        onRefresh();
        actionsListener.getSales(getSalesOptions());
        saleStateSpinner.setItems(ALL_SALES, PENDING_SALES, APPROVED_SALES, DENIED_SALES);
        saleStateSpinner.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view1, position, id, item) -> {
            selectedItem = item;
            onRefresh(true);
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_sales;
    }

    @Override
    public void showSales(SalesResponse salesResponse) {
        this.salesResponse = salesResponse;
        viewAdapter.addAll(salesResponse.sales);
        isEmpty();
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
    public void onRefresh(boolean clear) {
        super.onRefresh(clear);
        if(clear){
            viewAdapter.set(new ArrayList<>());
        }
        actionsListener.getSales(getSalesOptions());
    }

    @Override
    public int getTotalItems() {
        return salesResponse.totalCount;
    }

    public void setInteractionListener(SaleContract.InteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }
}