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
import com.andoresu.cryptoadmin.core.notices.data.Notice;
import com.andoresu.cryptoadmin.core.notices.data.NoticesResponse;
import com.andoresu.cryptoadmin.list.RecyclerViewFragment;
import com.andoresu.cryptoadmin.utils.BaseFragment;
import com.andoresu.cryptoadmin.utils.PaginationScrollListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoticesFragment extends RecyclerViewFragment<Notice> implements NoticesContract.View, SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.addNoticeButton)
    FloatingActionButton addNoticeButton;

    private NoticesContract.UserActionsListener actionsListener;

    private NoticesContract.InteractionListener interactionListener;

    private NoticesResponse noticesResponse;

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

    @Override
    public void handleView() {
        super.handleView();
        viewAdapter = new NoticeAdapter(getContext(), item -> interactionListener.goToNoticeDetail(item));
        listRecyclerView.setAdapter(viewAdapter);
        addNoticeButton.setOnClickListener(view1 -> interactionListener.goToNoticeDetail(null));
        onRefresh();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_notices;
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
    public void onRefresh(boolean clear) {
        super.onRefresh(clear);
        if(clear){
            viewAdapter.set(new ArrayList<>());
        }
        actionsListener.getNotices(getNoticesOptions());
    }

    @Override
    public void showNotices(NoticesResponse noticesResponse) {
        this.noticesResponse = noticesResponse;
        viewAdapter.addAll(noticesResponse.blogs);
        isEmpty();
    }

    @Override
    public int getTotalItems() {
        return noticesResponse.totalCount;
    }

}
