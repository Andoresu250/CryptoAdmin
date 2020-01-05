package com.andoresu.cryptoadmin.core.users;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andoresu.cryptoadmin.R;
import com.andoresu.cryptoadmin.authorization.data.User;
import com.andoresu.cryptoadmin.authorization.login.data.UsersResponse;
import com.andoresu.cryptoadmin.client.ErrorResponse;
import com.andoresu.cryptoadmin.list.RecyclerViewFragment;
import com.andoresu.cryptoadmin.utils.BaseFragment;
import com.andoresu.cryptoadmin.utils.PaginationScrollListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UsersFragment extends RecyclerViewFragment<User> implements UsersContract.View, SwipeRefreshLayout.OnRefreshListener{

    String TAG = "CRYPTO_" + UsersFragment.class.getSimpleName();

    private static final String ALL_USERS = "Todos los usuarios";
    private static final String ACTIVATED_USERS = "Usuarios Activos";
    private static final String DEACTIVATED_USERS = "Usuarios No Activados";

    @BindView(R.id.userStateSpinner)
    MaterialSpinner userStateSpinner;

    private UsersContract.UserActionsListener actionsListener;

    private UsersContract.InteractionListener interactionListener;

    private String selectedItem = ALL_USERS;

    private UsersResponse usersResponse;

    public UsersFragment(){

    }

    public static UsersFragment newInstance(@NonNull UsersContract.InteractionListener interactionListener) {
        Bundle args = new Bundle();
        UsersFragment fragment = new UsersFragment();
        fragment.setArguments(args);
        fragment.setInteractionListener(interactionListener);
        fragment.setTitle("Usuarios");
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){}
        actionsListener = new UsersPresenter(this, getContext());
    }

    @Override
    public void handleView() {
        super.handleView();
        viewAdapter = new UserAdapter(getContext(), item -> interactionListener.goToUserDetail(item));
        listRecyclerView.setAdapter(viewAdapter);
        onRefresh();
        actionsListener.getUsers(getUsersOptions());
        userStateSpinner.setItems(ALL_USERS, ACTIVATED_USERS, DEACTIVATED_USERS);
        userStateSpinner.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view1, position, id, item) -> {
            selectedItem = item;
            onRefresh(true);
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_users;
    }

    @Override
    public void showUsers(UsersResponse usersResponse) {
        this.usersResponse = usersResponse;
        viewAdapter.addAll(usersResponse.users);
        isEmpty();
    }

    private Map<String, String> getUsersOptions(){
        Map<String, String> options = new HashMap<>();
        options.put("by_profile_type", "Person");
        options.put("page", currentPage + "");
        switch (selectedItem){
            case ACTIVATED_USERS:
                options.put("by_state", "activated");
                break;
            case DEACTIVATED_USERS:
                options.put("by_state", "deactivated");
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
        actionsListener.getUsers(getUsersOptions());
    }

    @Override
    public int getTotalItems() {
        return usersResponse.totalCount;
    }

    public void setInteractionListener(UsersContract.InteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }

}
