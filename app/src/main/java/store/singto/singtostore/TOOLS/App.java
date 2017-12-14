package store.singto.singtostore.TOOLS;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

/**
 * Created by qiangli on 14/12/2017 AD.
 */

public class App extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleManager.setLocale(this);
    }
}
