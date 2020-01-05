package com.andoresu.cryptoadmin.core.contacts;

import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;

import com.andoresu.cryptoadmin.R;
import com.andoresu.cryptoadmin.core.contacts.data.Contact;
import com.andoresu.cryptoadmin.utils.BaseRecyclerViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactViewHolder extends BaseRecyclerViewAdapter.BaseViewHolder<Contact> {

    @BindView(R.id.contactNameTextInputLayout)
    public TextInputLayout contactNameTextInputLayout;
    @BindView(R.id.contactNameEditText)
    public EditText contactNameEditText;
    @BindView(R.id.contactPhoneTextInputLayout)
    public TextInputLayout contactPhoneTextInputLayout;
    @BindView(R.id.contactPhoneEditText)
    public EditText contactPhoneEditText;

    public ContactViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
