package com.example.speedtester;

import android.app.Application;
import android.content.Context;

public class GlobalApplication extends Application {

    private static Context appContext;
    private static Config confiqsettings = new Config();
    static boolean testanot;
    private static UserDetails user = new UserDetails();
    private static Data data = new Data();
    static boolean displayOTP;
    static boolean showactivationpage;

    String token;
    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        testanot = false;
        displayOTP = true;
        showactivationpage = false;

        if(testanot){
            confiqsettings.getAPlisturl = "http://192.168.1.124:8081/api/speedtest/getaplist";
            confiqsettings.loginURL = "http://192.168.1.124:8081/api/users/login";
            confiqsettings.logouturl = "http://192.168.1.124:8081/api/users/logout";
            confiqsettings.token= "ectivisecloudDBAuthCode:b84846daf467cede0ee462d04bcd0ade";
            confiqsettings.signup="http://192.168.1.124:8081/api/users/signup";
            confiqsettings.saveuser="http://192.168.1.124:8081/api/users/saveuser";
            confiqsettings.gettestresult="http://192.168.1.124:8081/api/speedtest/gettestresultlist";

        }
        else{
            confiqsettings.getAPlisturl = "http://dev1.ectivisecloud.com:8081/api/speedtest/getaplist";
            confiqsettings.loginURL = "http://dev1.ectivisecloud.com:8081/api/users/login";
            confiqsettings.logouturl = "http://dev1.ectivisecloud.com:8081/api/users/logout";
            confiqsettings.token= "ectivisecloudDBAuthCode:b84846daf467cede0ee462d04bcd0ade";
            confiqsettings.signup="http://dev1.ectivisecloud.com:8081/api/users/signup";
            confiqsettings.saveuser="http://dev1.ectivisecloud.com:8081/api/users/saveuser";
            confiqsettings.gettestresult="http://dev1.ectivisecloud.com:8081/api/speedtest/gettestresultlist";
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

    public static boolean isDisplayOTP(){return displayOTP;}

    public static boolean isShowactivationpage(){return showactivationpage;}


    public static class Config{
        public String getAPlisturl ;
        public String loginURL ;
        public String logouturl;
        public String token;
        public String saveuser;
        public String signup;
        public String gettestresult;
    }

    public static class UserDetails{
        public String phonenumber;
        public String userToken;
    }

    public static class Data{
        public String APlist;
    }

}