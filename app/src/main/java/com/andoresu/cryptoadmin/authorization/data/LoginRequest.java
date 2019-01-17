package com.andoresu.cryptoadmin.authorization.data;

public class LoginRequest {

    public LoginUser user;

    public LoginRequest(){}

    public LoginRequest(LoginUser user) {
        this.user = user;
    }
}
