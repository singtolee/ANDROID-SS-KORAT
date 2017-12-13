package store.singto.singtostore.TOOLS;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by qiangli on 13/12/2017 AD.
 */

public class SaveLocale {

    private Context context;
    public SaveLocale(Context context){
        this.context = context;
    }

    public void save(String locale){
        SharedPreferences sp = context.getSharedPreferences("LOCALE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("locale", locale);
        editor.commit();
    }

    public Map<String, String> read(){
        Map<String, String> data = new HashMap<String, String>();
        SharedPreferences sp = context.getSharedPreferences("LOCALE", Context.MODE_PRIVATE);
        data.put("locale", sp.getString("locale", "NULL"));
        return data;
    }
}
