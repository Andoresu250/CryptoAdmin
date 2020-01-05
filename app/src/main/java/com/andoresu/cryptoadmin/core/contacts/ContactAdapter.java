package com.andoresu.cryptoadmin.core.contacts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andoresu.cryptoadmin.R;
import com.andoresu.cryptoadmin.core.contacts.data.Contact;
import com.andoresu.cryptoadmin.core.setting.SettingViewHolder;
import com.andoresu.cryptoadmin.list.RecyclerViewAdapter;

public class ContactAdapter extends RecyclerViewAdapter<Contact> {

    public ContactAdapter(Context context) {
        super(context);
    }

    @Override
    public void setData(BaseViewHolder<Contact> holder, int position) {
        Contact contact = get(position);
        ContactViewHolder viewHolder = (ContactViewHolder) holder;
        viewHolder.contactNameEditText.setText(contact.name);
        viewHolder.contactPhoneEditText.setText(contact.phone);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.item_contact;
    }

    @NonNull
    @Override
    public BaseViewHolder<Contact> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(getLayoutResId(), parent, false);
        return new ContactViewHolder(view);
    }
}
