package com.andoresu.cryptoadmin.authorization.data;

import com.andoresu.cryptoadmin.utils.BaseObject;

import java.io.Serializable;
import java.util.Date;

public class User extends BaseObject implements Serializable {

    private static String TAG = "CRYPTO_" + "User";

    public static String NAME = "USER_TAG";

    public static final String TYPE_PERSON = "Person";
    public static final String TYPE_ADMIN = "Admin";

    public static final String STATE_ACTIVATED = "activated";
    public static final String STATE_DEACTIVATED = "deactivated";

    public String email;
    public String state;
    public String profileType;
    public String token;
    public Profile profile;
    public String password;
    public String passwordConfirmation;

    public User(){}

    public boolean isAdmin() {
        return profile instanceof Admin;
    }

    public boolean isPerson(){
        return profile instanceof Person;
    }

    public Admin getAdmin(){
        if (isAdmin()){
            return (Admin) profile;
        }
        return null;
    }



    public void setType(){
        if(this.profile instanceof Person){
            this.profileType = TYPE_PERSON;
        }else if(this.profile instanceof Admin){
            this.profileType = TYPE_ADMIN;
        }
    }

    public boolean isActivated(){
        return STATE_ACTIVATED.equals(state);
    }

    @Override
    public String toString() {
        String s = "\n";
        s += ("id: " + id + "\n");
        s += ("email: " + email + "\n");
        s += ("state: " + state + "\n");
        s += ("profileType: " + profileType + "\n");
        s += ("createdAt: " + createdAt.toString() + "\n");
        s += ("token: " + token + "\n");
        s += ("profile: " + profile.toString());
        return s;
    }
}