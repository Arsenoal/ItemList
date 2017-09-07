package test.com.itemlist;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by Arsen on 01.08.2017.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}