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
import com.andoresu.cryptoadmin.list.RecyclerViewFragment;
import com.andoresu.cryptoadmin.utils.BaseFragment;
import com.andoresu.cryptoadmin.utils.BaseRecyclerViewAdapter;
import com.andoresu.cryptoadmin.utils.PaginationScrollListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChargesFragment extends RecyclerViewFragment<Charge> implements ChargesContract.View{

    private static final String ALL_CHARGES = "Todas las Recargas";
    private static final String PENDING_CHARGES = "Recargas Pendientes";
    private static final String APPROVED_CHARGES = "Recargas Aprobadas";
    private static final String DENIED_CHARGES = "Recargas Rechazadas";

    @BindView(R.id.chargeStateSpinner)
    MaterialSpinner chargeStateSpinner;

    private ChargesContract.UserActionsListener actionsListener;

    private ChargesContract.InteractionListener interactionListener;

    private String selectedItem = ALL_CHARGES;
    private ChargesResponse chargesResponse;

    public ChargesFragment(){}

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
        actionsListener = new ChargesPresenter(this, getContext());
    }

    @Override
    public void handleView() {
        super.handleView();
        viewAdapter = new ChargeAdapter(getContext(), item -> interactionListener.goToChargeDetail(item));
        listRecyclerView.setAdapter(viewAdapter);
        onRefresh();
        chargeStateSpinner.setItems(ALL_CHARGES, PENDING_CHARGES, APPROVED_CHARGES, DENIED_CHARGES);
        chargeStateSpinner.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view1, position, id, item) -> {
            selectedItem = item;
            onRefresh(true);
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_charges;
    }

    @Override
    public void showCharges(ChargesResponse chargesResponse) {
        this.chargesResponse = chargesResponse;
        viewAdapter.addAll(chargesResponse.charges);
        isEmpty();
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
    public void onRefresh(boolean clear) {
        super.onRefresh(clear);
        if(clear){
            viewAdapter.set(new ArrayList<>());
        }
        actionsListener.getCharges(getChargesOptions());
    }

    @Override
    public int getTotalItems() {
        return chargesResponse.totalCount;
    }

    public void setInteractionListener(ChargesContract.InteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }
}