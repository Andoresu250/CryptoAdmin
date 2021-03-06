package com.andoresu.cryptoadmin.client;

import com.andoresu.cryptoadmin.authorization.data.Admin;
import com.andoresu.cryptoadmin.authorization.data.Person;
import com.andoresu.cryptoadmin.authorization.data.Profile;
import com.andoresu.cryptoadmin.authorization.data.User;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class GsonBuilderUtils {

    public static Gson SERIALIZED_GSON = getGenericGson(true);

    public static Gson getUserGson(){
        return getUserGson(false);
    }

    public static Gson getUserGson(boolean lowerCase){
        return getGenericBuilder(lowerCase)
                .registerTypeAdapterFactory(userProfileTypeFactory())
                .create();
    }

    public static RuntimeTypeAdapterFactory<Profile> userProfileTypeFactory(){
        return RuntimeTypeAdapterFactory
                .of(Profile.class, "type")
                .registerSubtype(Admin.class, User.TYPE_ADMIN)
                .registerSubtype(Person.class, User.TYPE_PERSON);
    }

    public static Gson getGenericGson(boolean lowerCase){
        return getGenericBuilder(lowerCase).create();
    }

    public static Gson getGenericGson(){
        return getGenericBuilder().create();
    }

    private static GsonBuilder getGenericBuilder(boolean lowerCase){
//        GsonBuilder builder = Utils.gsonBuilderWithDate();
        GsonBuilder builder = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss Z")
                .setExclusionStrategies(new AnnotationExclusionStrategy());
        if(lowerCase){
            builder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        }
        return builder;
    }

    private static GsonBuilder getGenericBuilder(){
        return getGenericBuilder(false);
    }

}
