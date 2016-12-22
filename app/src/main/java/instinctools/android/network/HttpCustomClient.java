package instinctools.android.network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by orion on 22.12.16.
 */

public class HttpCustomClient {
    private static final String TAG = "HttpCustomClient";

    // Default options
    private static final String DEFAULT_METHOD = "GET";

    private URL mUri;
    private String mMethod;
    private Map<String, String> mHeaders;

    //
    private String mContent;
    private int mResponseCode;

    public HttpCustomClient(Builder builder) {
        try {
            this.mUri = new URL(builder.mUrl);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Malformed url in constructor", e);
        }

        this.mMethod = builder.mMethod;
        this.mHeaders = builder.mHeaders;

        this.mContent = "";
        this.mResponseCode = -1;
    }

    public void sendRequest() {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) mUri.openConnection();
        } catch (IOException e) {
            Log.e(TAG, "Fail open connection in sendRequest", e);
        }

        if (connection == null)
            return;

        try {
            connection.setRequestMethod(mMethod);
        } catch (ProtocolException e) {
            Log.e(TAG, "Fail open connection in sendRequest. Unknown request method type", e);
        }

        for (Map.Entry<String, String> pair : mHeaders.entrySet())
            connection.setRequestProperty(pair.getKey(), pair.getValue());

        try {
            mResponseCode = connection.getResponseCode();
        } catch (IOException e) {
            Log.e(TAG, "Fail get response http code in sendRequest", e);
        }

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (IOException e) {
            Log.e(TAG, "Fail get input stream in sendRequest", e);
        }

        if (reader == null)
            return;

        String inputLine;
        StringBuilder response = new StringBuilder();
        try {
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
        } catch (IOException e) {
            Log.e(TAG, "Fail read stream in sendRequest", e);
        }

        try {
            reader.close();
        } catch (IOException e) {
            Log.e(TAG, "Fail close buffer reader in sendRequest", e);
        }

        mContent = response.toString();
    }

    public int getResponseCode() {
        return mResponseCode;
    }

    public String getContent() {
        return mContent;
    }

    public String getMethod() {
        return mMethod;
    }

    public static class Builder {
        private final String mUrl;
        private String mMethod;
        private Map<String, String> mHeaders;

        public Builder(String url) {
            this.mUrl = url;
            this.mMethod = DEFAULT_METHOD;
            this.mHeaders = new HashMap<>();
        }

        public Builder setMethod(String method) {
            this.mMethod = method;
            return this;
        }

        public Builder addHeader(String key, String value) {
            this.mHeaders.put(key, value);
            return this;
        }

        public HttpCustomClient build() {
            return new HttpCustomClient(this);
        }
    }
}
