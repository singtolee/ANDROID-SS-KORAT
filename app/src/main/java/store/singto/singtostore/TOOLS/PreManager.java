package store.singto.singtostore.TOOLS;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by qiangli on 14/12/2017 AD.
 */

public class PreManager {

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;

    private static final String PREF_NAME = "SINGTOSTORE";
    private static final String ISFIRSTTIMELAUNCH = "IsFirstTimeLaunch";

    public PreManager(Context context){
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();

    }

    public void setFirstTimeLaunch(boolean isFirstTime){
        editor.putBoolean(ISFIRSTTIMELAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch(){
        return preferences.getBoolean(ISFIRSTTIMELAUNCH, true);
    }


}
