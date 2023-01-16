package com.gsp.trusttags.mobile.mapping;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.StrictMode;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.gsp.trusttags.mobile.mapping.helper.TagValues;
import com.gsp.trusttags.mobile.mapping.networking.DaggerAppComponent;
import com.gsp.trusttags.mobile.mapping.networking.DaggerDependancyInjector;
import com.gsp.trusttags.mobile.mapping.networking.DependancyInjector;
import com.gsp.trusttags.mobile.mapping.networking.NetworkModule;

import java.io.File;

public class MyApplication extends MultiDexApplication {

    DependancyInjector deps;

    private static String mStringVersion;

    public static Context mContext;

    public DependancyInjector getDeps() {
        return deps;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            mStringVersion = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .inject(this);

        File cacheFile = new File(getCacheDir(), "responses");
        deps = DaggerDependancyInjector.builder().networkModule(new NetworkModule(cacheFile,getApplicationContext())).build();

        if(BuildConfig.FLAVOR=="dev")
            TagValues.MainURL = "http://gspdev-api.trusttags.in/";
        else if(BuildConfig.FLAVOR=="prod")
            TagValues.MainURL = "https://gsp-api.trusttags.in/";
        else if(BuildConfig.FLAVOR=="msdev")
            TagValues.MainURL = "https://msdev-api.trusttags.in/";
        else if(BuildConfig.FLAVOR=="msprod")
            TagValues.MainURL = "https://ms-api.trusttags.in/";//"https://ms-p1-api-staging.trusttags.in/";
        else if(BuildConfig.FLAVOR=="mildev")
            TagValues.MainURL = "http://mildev-api.trusttags.in/";
        else if(BuildConfig.FLAVOR=="milprod")
            TagValues.MainURL = "https://mil-api.trusttags.in/";
        else if(BuildConfig.FLAVOR=="moldev")
            TagValues.MainURL = "http://mol-api.trusttags.in/";
        else if(BuildConfig.FLAVOR=="molprod")
            TagValues.MainURL = "http://mol-api.trusttags.in/";
        else if(BuildConfig.FLAVOR=="wwdev")
            TagValues.MainURL = "https://ww-dev-api.trusttags.in/";
        else if(BuildConfig.FLAVOR=="wwprod")
            TagValues.MainURL = "https://ww-api.trusttags.in/";

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static Context getContext() {
        return mContext;
    }

    public static String getVersionNumber() {
        return mStringVersion;
    }

    public static boolean activityVisible;

    public static boolean isActivityVisible() {
        return activityVisible; // return true or false
    }

    public static void activityResumed() {
        activityVisible = true;// this will set true when activity resumed
    }

    public static void activityPaused() {
        activityVisible = false;// this will set false when activity paused
    }


}
