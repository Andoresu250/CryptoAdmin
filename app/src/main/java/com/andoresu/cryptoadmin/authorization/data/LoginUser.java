package com.andoresu.cryptoadmin.authorization.data;

public class LoginUser {

    public String email;
    public String password;

    public LoginUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "email: " + this.email;
    }
}
