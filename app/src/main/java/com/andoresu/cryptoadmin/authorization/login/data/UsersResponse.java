package com.andoresu.cryptoadmin.authorization.login.data;

import com.andoresu.cryptoadmin.authorization.data.User;
import com.andoresu.cryptoadmin.utils.BaseListResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UsersResponse extends BaseListResponse implements Serializable {

    public List<User> users = new ArrayList<>();

}
