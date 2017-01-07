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

public class HttpClientFactory {
    private static final String TAG = "HttpClientFactory";

    // Default options
    private static final String DEFAULT_METHOD = "GET";

    private HttpClientFactory() {
    }

    public static HttpClient create(String url) {
        return new HttpClient(url);
    }

    public static class HttpClient {
        private URL mUri;
        private String mMethod;
        private Map<String, String> mHeaders;

        private int mResponseCode;
        private String mContent;

        public HttpClient(String url) {
            try {
                this.mUri = new URL(url);
            } catch (MalformedURLException e) {
                Log.e(TAG, "Malformed url in constructor", e);
            }

            this.mMethod = DEFAULT_METHOD;
            this.mResponseCode = -1;
            this.mHeaders = new HashMap<>();
        }

        public HttpClient setMethod(String method) {
            this.mMethod = method;
            return this;
        }

        public HttpClient addHeader(String key, String value) {
            this.mHeaders.put(key, value);
            return this;
        }

        public int getResponseCode() {
            return mResponseCode;
        }

        public String getContent() {
            return mContent;
        }

        public String send() {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) mUri.openConnection();
            } catch (IOException e) {
                Log.e(TAG, "Fail open connection in sendRequest", e);
            }

            if (connection == null)
                return mContent;

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
                return mContent;

            String inputLine;
            StringBuilder response = new StringBuilder();
            try {
                while ((inputLine = reader.readLine()) != null) {
                    response.append(inputLine);
                }
            } catch (IOException e) {
                Log.e(TAG, "Fail transform stream in sendRequest", e);
            }

            try {
                reader.close();
            } catch (IOException e) {
                Log.e(TAG, "Fail close buffer reader in sendRequest", e);
            }

            mContent = response.toString();
            return mContent;
        }
    }
}
