package com.andoresu.cryptoadmin.core.chargepoints;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andoresu.cryptoadmin.R;
import com.andoresu.cryptoadmin.authorization.data.Country;
import com.andoresu.cryptoadmin.authorization.data.Person;
import com.andoresu.cryptoadmin.core.chargepoints.data.ChargePoint;
import com.andoresu.cryptoadmin.utils.BaseFragment;
import com.andoresu.cryptoadmin.utils.PaginationScrollListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChargePointsFragment extends BaseFragment implements ChargePointsContract.View,  SwipeRefreshLayout.OnRefreshListener{

    String TAG = "CRYPTO_" + ChargePointsFragment.class.getSimpleName();

    private static final int PAGE_START = 1;
    private boolean isLoading = false;

    @BindView(R.id.countrySpinner)
    MaterialSpinner countrySpinner;

    @BindView(R.id.chargePointsRecyclerView)
    RecyclerView chargePointsRecyclerView;

    @BindView(R.id.chargePointsSwipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private ChargePointsContract.UserActionsListener actionsListener;

    private ChargePointsContract.InteractionListener interactionListener;

    private ChargePointAdapter chargePointAdapter;

    private LinearLayoutManager linearLayoutManager;

    private int currentPage = PAGE_START;

    private CountryAdapter countryAdapter;

    private Country selectedCountry;

    public static ChargePointsFragment newInstance(ChargePointsContract.InteractionListener interactionListener) {
        Bundle args = new Bundle();

        ChargePointsFragment fragment = new ChargePointsFragment();
        fragment.setArguments(args);
        fragment.setTitle("Puntos de Recarga");
        fragment.setInteractionListener(interactionListener);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){}
        actionsListener = new ChargePointsPresenter(this, getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_charge_points, container, false);
        setUnbinder(ButterKnife.bind(this, view));

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(this);
        linearLayoutManager = new LinearLayoutManager(this.getContext(), 1, false);
        chargePointsRecyclerView.setLayoutManager(linearLayoutManager);
        chargePointAdapter = new ChargePointAdapter(getContext(), item -> interactionListener.goToChargePoint(item));
        chargePointsRecyclerView.setAdapter(chargePointAdapter);
        chargePointsRecyclerView.addOnScrollListener(getPaginationScrollListener());

        actionsListener.getChargePoints(getChargePointsOptions());
        countryAdapter = new CountryAdapter(getContext());
        countrySpinner.setAdapter(countryAdapter);
        countrySpinner.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<Country>) (view1, position, id, item) -> {
            if(item.id == null){
                selectedCountry = null;
            }else{
                selectedCountry = item;
            }
//            Snackbar.make(view1, "Cargando " + item, Snackbar.LENGTH_LONG).show();
            actionsListener.getChargePoints(getChargePointsOptions());
        });
        actionsListener.getCountries();

        return view;
    }

    @OnClick(R.id.addChargePointFloatingActionButton)
    public void newChargePoint(){
        interactionListener.goToChargePoint(new ChargePoint());
    }

    private void setInteractionListener(ChargePointsContract.InteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }

    @Override
    public void onRefresh() {
        actionsListener.getChargePoints(getChargePointsOptions());
    }

    @Override
    public void showChargePoints(List<ChargePoint> chargePoints) {
        this.chargePointAdapter.set(chargePoints);
    }

    @Override
    public void setCountries(List<Country> countries) {
        Country emptyCountry = new Country();
        emptyCountry.name = "Todos los paises";
        emptyCountry.id = null;
        countries.add(0, emptyCountry);
        countryAdapter = new CountryAdapter(getContext(), countries);
        countrySpinner.setAdapter(countryAdapter);
    }

    @Override
    public void showProgressIndicator(boolean active) {
        swipeRefreshLayout.setRefreshing(active);
        isLoading = active;
    }

    private Map<String, String> getChargePointsOptions(){
        Map<String, String> options = new HashMap<>();
        options.put("page", currentPage + "");
        if(selectedCountry != null){
            options.put("by_country_id", selectedCountry.id);
        }
        return options;
    }

    private PaginationScrollListener getPaginationScrollListener(){
        return new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
//                currentPage++;
                actionsListener.getChargePoints(getChargePointsOptions());
            }

            @Override
            public int getTotalPageCount() {
                return 1;
            }

            @Override
            public boolean isLastPage() {
                return currentPage >= 1;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        };
    }
}
