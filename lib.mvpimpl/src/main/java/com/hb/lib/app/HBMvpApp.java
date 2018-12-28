package com.hb.lib.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import androidx.multidex.MultiDex;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatDelegate;
import com.hb.lib.utils.BuildConfig;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.support.HasSupportFragmentInjector;
import timber.log.Timber;

import javax.inject.Inject;
import java.io.File;

public class HBMvpApp extends Application
        implements HasActivityInjector, HasSupportFragmentInjector,
        LifecycleDelegate, OnActivityCurrentListener {


    private static HBMvpApp instance;

    public static <T extends HBMvpApp> T getInstance() {
        return (T) instance;
    }

    public static String folder;

    private Activity mCurrentActivity;

    @Inject
    DispatchingAndroidInjector<Activity> activityInjector;
    @Inject
    DispatchingAndroidInjector<Fragment> fragmentInjector;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;


        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        initAppFolder();

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        AppLifecycleHandler lifecycleDelegate = new AppLifecycleHandler(this);
        registerLifecycleHandler(lifecycleDelegate);

    }

    private void registerLifecycleHandler(AppLifecycleHandler lifecycleHandler) {
        registerActivityLifecycleCallbacks(lifecycleHandler);
        registerComponentCallbacks(lifecycleHandler);
    }

    private void initAppFolder() {

        File fileTemp = getExternalCacheDir();
        if (fileTemp != null && fileTemp.exists()) {
            folder = fileTemp.getAbsolutePath();
        } else {
            folder = getCacheDir().getAbsolutePath();
        }

        folder += File.separator;

    }


    @Override
    public AndroidInjector<Activity> activityInjector() {
        return activityInjector;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentInjector;
    }


    @Override
    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    @Override
    public void setCurrentActivity(Activity activity) {
        mCurrentActivity = activity;
    }


    @Override
    public void onAppBackgrounded() {

    }

    @Override
    public void onAppForegrounded() {

    }

    @Override
    public void onAppKilled() {

    }


}

