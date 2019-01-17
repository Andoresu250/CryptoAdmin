package com.andoresu.cryptoadmin.core.profile.data;

import java.util.ArrayList;
import java.util.List;

public class UserErrors {

    public List<String> email = new ArrayList<>();
    public List<String> password = new ArrayList<>();
    public List<String> passwordConfirmation = new ArrayList<>();
    public AdminErrors profile = new AdminErrors();

}
