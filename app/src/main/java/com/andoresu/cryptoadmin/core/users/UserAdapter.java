package com.andoresu.cryptoadmin.core.users;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andoresu.cryptoadmin.R;
import com.andoresu.cryptoadmin.authorization.data.User;
import com.andoresu.cryptoadmin.authorization.login.data.UsersResponse;
import com.andoresu.cryptoadmin.utils.BaseRecyclerViewAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.andoresu.cryptoadmin.utils.MyUtils.getText;

public class UserAdapter extends BaseRecyclerViewAdapter<User> {

    public UsersResponse usersResponse;

    public UserAdapter(Context context, OnItemClickListener<User> listener) {
        super(context, listener);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.item_user;
    }

    @NonNull
    @Override
    public BaseViewHolder<User> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(getLayoutResId(), parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<User> holder, int position) {
        super.onBindViewHolder(holder, position);
        UserViewHolder viewHolder = (UserViewHolder) holder;
        User user = getItem(position);
        viewHolder.nameTextView.setText(getText(context, R.string.name_label, user.profile.fullName));
        viewHolder.emailTextView.setText(getText(context, R.string.email_label, user.email));
        viewHolder.stateTextView.setText(getText(context, R.string.state_label, user.state));
    }

    public void setUsersResponse(UsersResponse usersResponse){
        this.usersResponse = usersResponse;
        addAll(usersResponse.users);
    }

    public static class UserViewHolder extends BaseViewHolder<User> {

        @BindView(R.id.nameTextView)
        TextView nameTextView;
        @BindView(R.id.emailTextView)
        TextView emailTextView;
        @BindView(R.id.stateTextView)
        TextView stateTextView;

        public UserViewHolder(View v){
            super(v);
            ButterKnife.bind(this, v);
        }

        public void bind(final User item, final OnItemClickListener<User> listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(item));
        }
    }
}
