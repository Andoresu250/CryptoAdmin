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
import com.andoresu.cryptoadmin.list.RecyclerViewFragment;
import com.andoresu.cryptoadmin.utils.BaseFragment;
import com.andoresu.cryptoadmin.utils.PaginationScrollListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PurchasesFragment extends RecyclerViewFragment<Purchase> implements PurchaseContract.View{

    private static final String ALL_PURCHASE = "Todas las Compras";
    private static final String PENDING_PURCHASE = "Compras Pendientes";
    private static final String APPROVED_PURCHASE = "Compras Aprobadas";
    private static final String DENIED_PURCHASE = "Compras Rechazadas";

    @BindView(R.id.purchaseStateSpinner)
    MaterialSpinner purchaseStateSpinner;

    private PurchaseContract.UserActionsListener actionsListener;

    private PurchaseContract.InteractionListener interactionListener;

    private String selectedItem = ALL_PURCHASE;

    private PurchasesResponse purchasesResponse;

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

    @Override
    public void handleView() {
        super.handleView();

        viewAdapter = new PurchaseAdapter(getContext(), item -> interactionListener.goToPurchaseDetail(item));
        listRecyclerView.setAdapter(viewAdapter);
        onRefresh();
        actionsListener.getPurchases(getPurchasesOptions());
        purchaseStateSpinner.setItems(ALL_PURCHASE, PENDING_PURCHASE, APPROVED_PURCHASE, DENIED_PURCHASE);
        purchaseStateSpinner.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view1, position, id, item) -> {
            selectedItem = item;
            onRefresh(true);
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_purchases;
    }

    @Override
    public void showPurchases(PurchasesResponse purchasesResponse) {
        this.purchasesResponse = purchasesResponse;
        viewAdapter.addAll(purchasesResponse.purchases);
        isEmpty();
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
    public int getTotalItems() {
        return purchasesResponse.totalCount;
    }

    @Override
    public void onRefresh(boolean clear) {
        super.onRefresh(clear);
        if(clear){
            viewAdapter.set(new ArrayList<>());
        }
        actionsListener.getPurchases(getPurchasesOptions());
    }


    public void setInteractionListener(PurchaseContract.InteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }

}