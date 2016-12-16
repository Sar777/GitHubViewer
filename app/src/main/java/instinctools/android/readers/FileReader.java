package instinctools.android.readers;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import instinctools.android.R;

/**
 * Created by orion on 16.12.16.
 */

public class FileReader implements IReader<Integer, String> {
    private Context mContext;

    public FileReader(Context context) {
        this.mContext = context;
    }

    @Override
    public String read(Integer resId) {
        InputStream stream = mContext.getResources().openRawResource(R.raw.data);

        StringBuffer buffer = new StringBuffer();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String buff;
            while ((buff = reader.readLine()) != null) {
                buffer.append(buff);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try { stream.close(); } catch (Throwable ignore) {}
        }

        return buffer.toString();
    }
}
