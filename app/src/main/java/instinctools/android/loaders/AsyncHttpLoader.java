package instinctools.android.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import instinctools.android.activity.MainActivity;
import instinctools.android.data.Book;
import instinctools.android.network.HttpClientFactory;
import instinctools.android.readers.JsonBookReader;

/**
 * Created by orion on 22.12.16.
 */

public class AsyncHttpLoader extends AsyncTaskLoader<List<Book>> {
    private String mUrl = "http";

    public AsyncHttpLoader(Context context, Bundle args) {
        super(context);

        if (args != null)
            mUrl = args.getString(MainActivity.BUNDLE_LOADER_URL);
    }

    @Override
    public List<Book> loadInBackground() {
        String content = HttpClientFactory.create(mUrl).addHeader("Accept", "application/json").setMethod("GET").send();
        if (TextUtils.isEmpty(content))
            return new ArrayList<>();

        List<Book> books = new JsonBookReader().read(content);
        return books;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
