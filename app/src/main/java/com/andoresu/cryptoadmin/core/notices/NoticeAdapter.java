package com.andoresu.cryptoadmin.core.notices;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andoresu.cryptoadmin.R;
import com.andoresu.cryptoadmin.core.notices.data.Notice;
import com.andoresu.cryptoadmin.core.notices.data.NoticesResponse;
import com.andoresu.cryptoadmin.list.RecyclerViewAdapter;
import com.andoresu.cryptoadmin.utils.BaseRecyclerViewAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

import static com.andoresu.cryptoadmin.utils.MyUtils.HIDE_VIEW;
import static com.andoresu.cryptoadmin.utils.MyUtils.getText;

public class NoticeAdapter extends RecyclerViewAdapter<Notice> {

    public NoticesResponse noticesResponse;

    public NoticeAdapter(Context context, OnItemClickListener<Notice> listener) {
        super(context, listener);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.item_notice;
    }

    @NonNull
    @Override
    public BaseViewHolder<Notice> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(getLayoutResId(), parent, false);
        return new NoticeAdapter.NoticeViewHolder(view);
    }


    @Override
    public void setData(BaseViewHolder<Notice> holder, int position) {
        NoticeAdapter.NoticeViewHolder viewHolder = (NoticeAdapter.NoticeViewHolder) holder;
        Notice notice = getItem(position);
        viewHolder.noticeTitleTextView.setText(notice.title);
        viewHolder.noticeBodyTextView.setText(notice.body);
    }

    public static class NoticeViewHolder extends BaseViewHolder<Notice> {

        @BindView(R.id.noticeTitleTextView)
        TextView noticeTitleTextView;

        @BindView(R.id.noticeBodyTextView)
        TextView noticeBodyTextView;

        public NoticeViewHolder(View v){
            super(v);
            ButterKnife.bind(this, v);
        }

        public void bind(final Notice item, final OnItemClickListener<Notice> listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(item));
        }
    }
}
