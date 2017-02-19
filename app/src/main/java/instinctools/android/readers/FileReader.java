package instinctools.android.readers;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by orion on 16.12.16.
 */

public class FileReader implements IReader<Integer, String> {
    private static final String TAG = "FileReader";

    private Context mContext;

    public FileReader(Context context) {
        this.mContext = context;
    }

    @Override
    public String read(Integer resId) {
        InputStream stream = mContext.getResources().openRawResource(resId);

        StringBuffer buffer = new StringBuffer();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String buff;
            while ((buff = reader.readLine()) != null) {
                buffer.append(buff);
            }
        } catch (IOException e) {
            Log.e(TAG, "Error read from stream in FileReader...", e);
        } finally {
            if (stream != null)
                try {
                    stream.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error close stream in FileReader...", e);
                }
        }

        return buffer.toString();
    }
}
