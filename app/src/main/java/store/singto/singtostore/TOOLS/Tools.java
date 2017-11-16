package store.singto.singtostore.TOOLS;

import android.util.Patterns;

/**
 * Created by qiangli on 11/16/2017 AD.
 */

public class Tools {
    public static boolean isEmail(String email) {
        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return false;
        }else {
            return true;
        }
    }

}
