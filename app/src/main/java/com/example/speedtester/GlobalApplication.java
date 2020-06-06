package com.example.speedtester;

import android.app.Application;
import android.content.Context;

public class GlobalApplication extends Application {

    private static Context appContext;
    private static Config confiqsettings = new Config();
    static boolean testanot;
    private static UserDetails user = new UserDetails();
    private static Data data = new Data();
    String token;
    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        testanot = false;
        if(testanot){
            confiqsettings.getAPlisturl = "http://192.168.1.124:8081/api/speedtest/getaplist";
            confiqsettings.loginURL = "http://192.168.1.124:8081/api/users/login";
            confiqsettings.logouturl = "http://192.168.1.124:8081/api/users/logout";
            confiqsettings.token= "ectivisecloudDBAuthCode:b84846daf467cede0ee462d04bcd0ade";
        }
        else{
            confiqsettings.getAPlisturl = "http://api.ectivisecloud.com:8081/api/speedtest/getaplist";
            confiqsettings.loginURL = "http://api.ectivisecloud.com:8081/api/users/login";
            confiqsettings.logouturl = "http://api.ectivisecloud.com:8081/api/users/logout";
            confiqsettings.token= "ectivisecloudDBAuthCode:b84846daf467cede0ee462d04bcd0ade";
        }

        data.APlist="";
        /* If you has other classes that need context object to initialize when application is created,
         you can use the appContext here to process. */
    }

    public static Context getAppContext() {
        return appContext;
    }

    public static Config getconfiq() {
            return confiqsettings;
    }

    public static UserDetails getuserdetails() {
        return user;
    }

    public static Data getData(){return data;}

    public static class Config{
        String getAPlisturl ;
        String loginURL ;
        String logouturl;
        String token;
    }

    public static class UserDetails{
        String phonenumber;
        String userToken;
    }

    public static class Data{
        String APlist;
    }
}