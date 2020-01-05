package com.andoresu.cryptoadmin.core.contacts.data;

import com.andoresu.cryptoadmin.utils.BaseListResponse;

import java.io.Serializable;
import java.util.List;

public class ContactsResponse  extends BaseListResponse implements Serializable {
    public List<Contact> contacts;
}
