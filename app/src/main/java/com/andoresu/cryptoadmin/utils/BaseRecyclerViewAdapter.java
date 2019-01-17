package com.andoresu.cryptoadmin.utils;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewAdapter.BaseViewHolder<T>> {

    public final Context context;
    public ArrayList<T> items;
    public final OnItemClickListener<T> listener;

    public BaseRecyclerViewAdapter(Context context,@NonNull ArrayList<T> items, OnItemClickListener<T> listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    public BaseRecyclerViewAdapter(Context context, OnItemClickListener<T> listener) {
        this.context = context;
        this.items = new ArrayList<>();
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<T> holder, int position) {
        T item = items.get(position);
        holder.bind(item, listener);
    }

    @LayoutRes
    protected abstract int getLayoutResId();

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void add(T item) {
        if(!items.contains(item)){
            items.add(item);
            notifyItemInserted(items.size() - 1);
        }
    }

    public void addAll(List<T> items) {
        for (T item : items) {
            add(item);
        }
        notifyDataSetChanged();
    }

    public void set(List<T> items){
        clear();
        addAll(items);
    }

    public void remove(T item) {
        int position = items.indexOf(item);
        if (position > -1) {
            items.remove(position);
            notifyItemRemoved(position);
        }
    }

    public T getItem(int position){
        return items.get(position);
    }

    public void clear() {
        while (getItemCount() > 0) {
            remove(getItem(0));
            notifyItemRemoved(0);
        }
        notifyDataSetChanged();

    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public abstract static class BaseViewHolder<T> extends RecyclerView.ViewHolder {

        public View itemView;

        public BaseViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        public void bind(final T item, final OnItemClickListener<T> listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(item));
        }
    }

    public interface OnItemClickListener<T> {
        void onItemClick(T item);
    }

}
