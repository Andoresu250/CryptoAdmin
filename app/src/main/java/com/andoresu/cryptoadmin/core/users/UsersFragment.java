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
import com.andoresu.cryptoadmin.authorization.login.data.UsersResponse;
import com.andoresu.cryptoadmin.client.ErrorResponse;
import com.andoresu.cryptoadmin.utils.BaseFragment;
import com.andoresu.cryptoadmin.utils.PaginationScrollListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UsersFragment extends BaseFragment implements UsersContract.View, SwipeRefreshLayout.OnRefreshListener{

    String TAG = "CRYPTO_" + UsersFragment.class.getSimpleName();

    private static final int PAGE_START = 1;
    public static final int PER_PAGE = 15;
    private boolean isLoading = false;

    private static final String ALL_USERS = "Todos los usuarios";
    private static final String ACTIVATED_USERS = "Usuarios Activos";
    private static final String DEACTIVATED_USERS = "Usuarios No Activados";

    @BindView(R.id.userStateSpinner)
    MaterialSpinner userStateSpinner;

    @BindView(R.id.usersRecyclerView)
    RecyclerView usersRecyclerView;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private UsersContract.UserActionsListener actionsListener;

    private UsersContract.InteractionListener interactionListener;

    private String selectedItem = ALL_USERS;

    private UserAdapter userAdapter;

    private LinearLayoutManager linearLayoutManager;

    private int currentPage = PAGE_START;

    public UsersFragment(){
        actionsListener = new UsersPresenter(this, getContext());
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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        setUnbinder(ButterKnife.bind(this, view));

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(this);
        linearLayoutManager = new LinearLayoutManager(this.getContext(), 1, false);
        usersRecyclerView.setLayoutManager(linearLayoutManager);
        userAdapter = new UserAdapter(getContext(), item -> interactionListener.goToUserDetail(item));
        usersRecyclerView.setAdapter(userAdapter);
        usersRecyclerView.addOnScrollListener(getPaginationScrollListener());

        actionsListener.getUsers(getUsersOptions());
        userStateSpinner.setItems(ALL_USERS, ACTIVATED_USERS, DEACTIVATED_USERS);
        userStateSpinner.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view1, position, id, item) -> {
            currentPage = 1;
            Snackbar.make(view1, "Cargando " + item, Snackbar.LENGTH_LONG).show();
            selectedItem = item;
            userAdapter.set(new ArrayList<>());
            actionsListener.getUsers(getUsersOptions());
        });
        return view;
    }

    @Override
    public void showUsers(UsersResponse usersResponse) {
        this.userAdapter.setUsersResponse(usersResponse);
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
    public void onRefresh() {
        actionsListener.getUsers(getUsersOptions());
    }

    public void setInteractionListener(UsersContract.InteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }

    private PaginationScrollListener getPaginationScrollListener(){
        return new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                actionsListener.getUsers(getUsersOptions());
            }

            @Override
            public int getTotalPageCount() {
                return userAdapter.usersResponse.getTotalPage();
            }

            @Override
            public boolean isLastPage() {
                return currentPage >= userAdapter.usersResponse.getTotalPage();
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        };
    }
}
