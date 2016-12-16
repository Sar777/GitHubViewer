package instinctools.android.utility;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by orion on 16.12.16.
 */

public class DrawableFactory {
    public static Drawable createFromAssets(Context context, String name) {
        InputStream stream = null;
        Drawable drawable = null;
        try {
            stream = context.getAssets().open(name);
            drawable = Drawable.createFromStream(stream, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (stream != null)
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return drawable;
    }
}
