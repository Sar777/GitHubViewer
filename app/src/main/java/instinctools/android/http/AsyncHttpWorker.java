package instinctools.android.http;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;

/**
 * Created by orion on 12.1.17.
 */

class AsyncHttpWorker extends AsyncTask<Void, Void, Void> {
    private OnHttpClientListener mListener;
    private WeakReference<HttpClientFactory.HttpClient> mClientReference;

    AsyncHttpWorker(HttpClientFactory.HttpClient client, OnHttpClientListener listener) {
        this.mClientReference = new WeakReference<>(client);
        this.mListener = listener;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        HttpClientFactory.HttpClient client = mClientReference.get();
        if (client == null)
            return null;

        client.send();
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        HttpClientFactory.HttpClient client = mClientReference.get();
        if (client == null) {
            mListener.onError(HttpURLConnection.HTTP_BAD_REQUEST);
            return;
        }

        if (client.getCode() == -1) {
            mListener.onError(HttpURLConnection.HTTP_BAD_REQUEST);
            return;
        }

        mListener.onSuccess(client.getCode(), client.getContent());
    }
}