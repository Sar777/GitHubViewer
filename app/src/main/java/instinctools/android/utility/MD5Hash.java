package instinctools.android.utility;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Hash {
    private static final String TAG = "MD5Hash";

    public static String create(String data) {
        String md5Hash = null;
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(data.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; ++i)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            md5Hash = hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "Error create md5 hash", e);
        }

        return md5Hash;
    }
}
