package com.andoresu.cryptoadmin.core.notices;

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
import com.andoresu.cryptoadmin.core.notices.data.NoticesResponse;
import com.andoresu.cryptoadmin.utils.BaseFragment;
import com.andoresu.cryptoadmin.utils.PaginationScrollListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoticesFragment extends BaseFragment implements NoticesContract.View, SwipeRefreshLayout.OnRefreshListener{

    private static final int PAGE_START = 1;
    private boolean isLoading = false;

    @BindView(R.id.noticesRecyclerView)
    RecyclerView noticesRecyclerView;

    @BindView(R.id.noticesSwipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.addNoticeButton)
    FloatingActionButton addNoticeButton;

    private NoticesContract.UserActionsListener actionsListener;

    private NoticesContract.InteractionListener interactionListener;

    private NoticeAdapter noticeAdapter;

    private LinearLayoutManager linearLayoutManager;

    private int currentPage = PAGE_START;

    public NoticesFragment(){}

    public static NoticesFragment newInstance(NoticesContract.InteractionListener interactionListener) {

        Bundle args = new Bundle();

        NoticesFragment fragment = new NoticesFragment();
        fragment.setArguments(args);
        fragment.setTitle("Noticias");
        fragment.setInteractionListener(interactionListener);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){}
        actionsListener = new NoticesPresenter(this, getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notices, container, false);
        setUnbinder(ButterKnife.bind(this, view));

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(this);
        linearLayoutManager = new LinearLayoutManager(this.getContext(), 1, false);
        noticesRecyclerView.setLayoutManager(linearLayoutManager);
        noticeAdapter = new NoticeAdapter(getContext(), item -> interactionListener.goToNoticeDetail(item));
        noticesRecyclerView.setAdapter(noticeAdapter);
        noticesRecyclerView.addOnScrollListener(getPaginationScrollListener());

        addNoticeButton.setOnClickListener(view1 -> interactionListener.goToNoticeDetail(null));

        actionsListener.getNotices(getNoticesOptions());
        return view;
    }

    private Map<String, String> getNoticesOptions(){
        Map<String, String> options = new HashMap<>();
        options.put("page", currentPage + "");
        return options;
    }

    private void setInteractionListener(NoticesContract.InteractionListener interactionListener) {
        this.interactionListener = interactionListener;
    }

    @Override
    public void onRefresh() {
        actionsListener.getNotices(getNoticesOptions());
    }

    @Override
    public void showNotices(NoticesResponse noticesResponse) {
        this.noticeAdapter.setNoticesResponse(noticesResponse);
    }

    private PaginationScrollListener getPaginationScrollListener(){
        return new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage++;
                actionsListener.getNotices(getNoticesOptions());
            }

            @Override
            public int getTotalPageCount() {
                return noticeAdapter.noticesResponse.getTotalPage();
            }

            @Override
            public boolean isLastPage() {
                return currentPage >= noticeAdapter.noticesResponse.getTotalPage();
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        };
    }
}
