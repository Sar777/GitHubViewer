package instinctools.android.utility;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by orion on 16.12.16.
 */

public class DrawableFactory {
    private static final String TAG = "DrawableFactory";

    public static Drawable createFromAssets(Context context, String name) {

        InputStream stream = null;
        Drawable drawable = null;
        try {
            stream = context.getAssets().open(name);
            drawable = Drawable.createFromStream(stream, null);
        } catch (IOException e) {
            Log.e(TAG, "Error open stream in createFromAssets...", e);
        } finally {
            if (stream != null)
                try {
                    stream.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error close stream in createFromAssets...", e);
                }
        }

        return drawable;
    }
}
