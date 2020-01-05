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
import com.andoresu.cryptoadmin.core.settingdetail.data.Setting;
import com.andoresu.cryptoadmin.list.RecyclerViewFragment;
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

public class ChargePointsFragment  extends RecyclerViewFragment<ChargePoint> implements ChargePointsContract.View{

    String TAG = "CRYPTO_" + ChargePointsFragment.class.getSimpleName();

    @BindView(R.id.countrySpinner)
    MaterialSpinner countrySpinner;

    private ChargePointsContract.UserActionsListener actionsListener;

    private ChargePointsContract.InteractionListener interactionListener;

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

    @Override
    public void handleView() {
        super.handleView();
        viewAdapter = new ChargePointAdapter(getContext(), item -> interactionListener.goToChargePoint(item));
        listRecyclerView.setAdapter(viewAdapter);
        onRefresh();
        countryAdapter = new CountryAdapter(getContext());
        countrySpinner.setAdapter(countryAdapter);
        countrySpinner.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<Country>) (view1, position, id, item) -> {
            if(item.id == null){
                selectedCountry = null;
            }else{
                selectedCountry = item;
            }
            onRefresh();
        });
        actionsListener.getCountries();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_charge_points;
    }

    @OnClick(R.id.addChargePointFloatingActionButton)
    public void newChargePoint(){
        interactionListener.goToChargePoint(new ChargePoint());
    }

    private void setInteractionListener(ChargePointsContract.InteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }

    @Override
    public void onRefresh(boolean clear) {
        super.onRefresh(clear);
        if(clear){
            viewAdapter.set(new ArrayList<>());
        }
        actionsListener.getChargePoints(getChargePointsOptions());
    }

//    @Override
//    public int getTotalItems() {
//        return settingsResponse.totalCount;
//    }

    @Override
    public void showChargePoints(List<ChargePoint> chargePoints) {
        viewAdapter.addAll(chargePoints);
        isEmpty();
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


    private Map<String, String> getChargePointsOptions(){
        Map<String, String> options = new HashMap<>();
        options.put("page", currentPage + "");
        if(selectedCountry != null){
            options.put("by_country_id", selectedCountry.id);
        }
        return options;
    }

}
