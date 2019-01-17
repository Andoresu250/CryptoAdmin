package com.andoresu.cryptoadmin.core.charges.data;


import com.andoresu.cryptoadmin.core.users.UsersFragment;
import com.andoresu.cryptoadmin.utils.BaseListResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChargesResponse extends BaseListResponse implements Serializable {

    public List<Charge> charges = new ArrayList<>();

}
