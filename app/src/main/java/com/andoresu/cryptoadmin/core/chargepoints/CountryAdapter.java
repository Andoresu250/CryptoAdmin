package com.andoresu.cryptoadmin.core.chargepoints;

import android.content.Context;

import com.andoresu.cryptoadmin.authorization.data.Country;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jaredrummler.materialspinner.MaterialSpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

public class CountryAdapter extends MaterialSpinnerAdapter<Country> {

    public CountryAdapter(Context context, List<Country> items) {
        super(context, items);
    }

    public CountryAdapter(Context context) {
        super(context, new ArrayList<>());
    }


}
