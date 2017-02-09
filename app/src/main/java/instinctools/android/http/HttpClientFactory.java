package instinctools.android.http;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClientFactory {
    private static final String TAG = "HttpClientFactory";

    // Methods
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_DELETE = "DELETE";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_PATCH = "PATCH";

    // Headers
    public static final String HEADER_ACCEPT = "Accept";
    public static final String HEADER_LINK = "Link";
    public static final String HEADER_CONTENT_LENGHT = "Content-Length";
    public static final String HEADER_AUTHORIZATION = "Authorization";

    // Types
    public static final String HEADER_ACCEPT_TYPE_JSON = "application/json";
    public static final String HEADER_ACCEPT_TYPE_CUSTOM_GITHUB = "application/vnd.github.cloak-preview";

    private HttpClientFactory() {
    }

    public static HttpClient create(String url) {
        return new HttpClient(url);
    }

    public static class HttpClient {
        private URL mUri;
        private String mMethod;
        private String mData;
        private Map<String, String> mRequestHeaders;
        private Map<String, List<String>> mReponseHeaders;
        private Map<String, String> mParams;

        private int mCode;
        private String mContent;

        HttpClient(String url) {
            try {
                this.mUri = new URL(url);
            } catch (MalformedURLException e) {
                Log.e(TAG, "Malformed url in constructor", e);
            }

            this.mMethod = METHOD_GET;
            this.mCode = -1;
            this.mRequestHeaders = new HashMap<>();
            this.mReponseHeaders = new HashMap<>();
            this.mParams = new HashMap<>();
        }

        private String getBuildParams() {
            if (mParams.isEmpty())
                return null;

            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String, String> pair : mParams.entrySet()) {
                try {
                    builder.append(URLEncoder.encode(pair.getKey(), "UTF-8"));
                    builder.append("=");
                    builder.append(pair.getValue());
                    builder.append("&");
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "Fail build params encoding", e);
                }
            }

            builder.setLength(builder.length() - 1);
            return builder.toString();
        }

        public HttpClient setMethod(String method) {
            this.mMethod = method;
            return this;
        }

        public HttpClient addHeader(String key, String value) {
            this.mRequestHeaders.put(key, value);
            return this;
        }

        public HttpClient addParam(String key, String value) {
            this.mParams.put(key, value);
            return this;
        }

        public HttpClient setData(String data) {
            this.mData = data;
            return this;
        }

        public int getCode() {
            return mCode;
        }

        public String getContent() {
            return mContent;
        }

        public Map<String, List<String>> getReponseHeaders() {
            return mReponseHeaders;
        }

        public String getResponseHeader(String header) {
            List<String> headers  = mReponseHeaders.get(header);
            if (headers == null || headers.isEmpty())
                return null;

            return headers.get(0);
        }

        public HttpClient send() {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) mUri.openConnection();
                connection.setRequestMethod(mMethod);
                for (Map.Entry<String, String> pair : mRequestHeaders.entrySet())
                    connection.setRequestProperty(pair.getKey(), pair.getValue());

            } catch (IOException e) {
                Log.e(TAG, "Fail http client", e);
            }

            if (mMethod.equals(METHOD_POST)) {
                if (mData != null) {
                    DataOutputStream dataOutputStream = null;
                    try {
                        dataOutputStream = new DataOutputStream(connection.getOutputStream());
                        dataOutputStream.writeBytes(mData);

                        dataOutputStream.flush();
                    } catch (IOException e) {
                        Log.e(TAG, "Fail write output stream", e);
                    } finally {
                        if (dataOutputStream != null)
                            try {
                                dataOutputStream.close();
                            } catch (IOException e) {
                                Log.e(TAG, "Fail close output stream", e);
                            }
                    }
                }
            }

            try {
                mCode = connection.getResponseCode();
            } catch (IOException e) {
                Log.e(TAG, "Fail get response http code in send", e);
            }

            BufferedReader reader = null;
            try {
                if (mCode >= HttpURLConnection.HTTP_BAD_REQUEST)
                    reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                else
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } catch (IOException e) {
                Log.e(TAG, "Fail get input stream in send", e);
            }

            if (reader == null)
                return this;

            String inputLine;
            StringBuilder response = new StringBuilder();
            try {
                while ((inputLine = reader.readLine()) != null) {
                    response.append(inputLine);
                }
            } catch (IOException e) {
                Log.e(TAG, "Fail transform stream in send", e);
            }

            try {
                reader.close();
            } catch (IOException e) {
                Log.e(TAG, "Fail close buffer reader in send", e);
            }

            mReponseHeaders = connection.getHeaderFields();

            connection.disconnect();

            mContent = response.toString();
            return this;
        }

        public void send(final OnHttpClientListener listener) {
            new AsyncHttpWorker(this, listener).execute();
        }
    }
}
