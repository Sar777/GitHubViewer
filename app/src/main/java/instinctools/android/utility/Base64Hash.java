package instinctools.android.utility;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Created by orion on 12.1.17.
 */

public class Base64Hash {
    public static String create(String data) {
        byte[] bytes = new byte[0];
        try {
            bytes = data.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}
