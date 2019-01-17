package com.andoresu.cryptoadmin.core.sales.data;

import com.andoresu.cryptoadmin.authorization.data.DocumentType;
import com.andoresu.cryptoadmin.utils.BaseObject;

public class BankAccount extends BaseObject {

    public String bank;
    public String number;
    public String ownerName;
    public String identification;
    public DocumentType documentType;

}
