package com.andoresu.cryptoadmin.list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.andoresu.cryptoadmin.R;
import com.andoresu.cryptoadmin.utils.BaseDialogFragment;

import butterknife.BindView;

public class ListDialogFragment<T> extends BaseDialogFragment {

    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    public RecyclerViewAdapter<T> recyclerViewAdapter;

    public ListDialogListener listDialogListener;

    public Double widthPercentage;
    public Double heightPercentage;

    public static <T>ListDialogFragment<T> newInstance(@NonNull ListDialogListener listDialogListener) {
        Bundle args = new Bundle();
        ListDialogFragment<T> fragment = new ListDialogFragment<>();
        fragment.setArguments(args);
        fragment.setListDialogListener(listDialogListener);
        return fragment;
    }

    private void setListDialogListener(ListDialogListener listDialogListener) {
        this.listDialogListener = listDialogListener;
    }

    @Override
    public int getLayoutId() {
        return R.layout.df_list;
    }

    @Override
    public void handleView() {
        builder.setNegativeButton(R.string.close, (dialogInterface, i) -> dialogInterface.dismiss());
        builder.setPositiveButton(R.string.ok, (dialogInterface, i) -> {
            listDialogListener.onClickPositiveButton();
            dialogInterface.dismiss();
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext(), 1, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(this.recyclerViewAdapter);
    }

    public void setRecyclerViewAdapter(RecyclerViewAdapter<T> recyclerViewAdapter){
        this.recyclerViewAdapter = recyclerViewAdapter;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(widthPercentage != null && heightPercentage != null){
            setDialogSize(widthPercentage, heightPercentage);
        }else{
            setDialogSize(null, 0.5);
        }

    }

    public interface ListDialogListener {

        void onClickPositiveButton();

    }
}