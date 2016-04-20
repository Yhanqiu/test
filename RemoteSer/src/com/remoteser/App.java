package com.remoteser;

import android.app.Application;
import android.util.Log;
import androidx.pluginmgr.PluginManager;

/**
 * @author Lody
 * @version 1.0
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("App", this.toString());
        PluginManager.init(this);
    }
}
