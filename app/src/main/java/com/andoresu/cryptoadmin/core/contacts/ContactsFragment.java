package com.andoresu.cryptoadmin.core.contacts;



import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.andoresu.cryptoadmin.R;
import com.andoresu.cryptoadmin.core.contacts.data.Contact;
import com.andoresu.cryptoadmin.core.contacts.data.ContactsResponse;
import com.andoresu.cryptoadmin.core.setting.SettingAdapter;
import com.andoresu.cryptoadmin.core.setting.SettingsPresenter;
import com.andoresu.cryptoadmin.list.RecyclerViewFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContactsFragment extends RecyclerViewFragment<Contact> implements ContactsContract.View {

    public ContactsContract.InteractionListener interactionListener;
    public ContactsContract.UserActionsListener actionsListener;
    public ContactsResponse contactsResponse;

    public static ContactsFragment newInstance() {

        Bundle args = new Bundle();
        ContactsFragment fragment = new ContactsFragment();
        fragment.setArguments(args);
        fragment.setCustomTitle("Contactos Robados");
        fragment.addSearch = true;
        fragment.setHasOptionsMenu(true);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){}
        actionsListener = new ContactsPresenter(this, getContext());
    }

    @Override
    public void handleView() {
        super.handleView();
        viewAdapter = new ContactAdapter(getContext());
        listRecyclerView.setAdapter(viewAdapter);
        onRefresh();
    }

    private Map<String, String> getOptions(){
        Map<String, String> options = new HashMap<>();
        options.put("page", currentPage + "");
        if(searchQuery != null){
            options.put("search", searchQuery);
        }
        return options;
    }

    @Override
    public void showContacts(ContactsResponse contactsResponse) {
        this.contactsResponse = contactsResponse;
        viewAdapter.addAll(contactsResponse.contacts);
        isEmpty();
    }

    @Override
    public void onRefresh(boolean clear) {
        super.onRefresh(clear);
        Log.i(TAG, "onRefresh: clear " + clear);
        if(clear){
            viewAdapter.set(new ArrayList<>());
        }
        actionsListener.getContacts(getOptions());
    }

    @Override
    public int getTotalItems() {
        return contactsResponse.totalCount;
    }

    @Override
    public void showProgressIndicator(boolean active) {
        showLoading(active);
    }

}
