package instinctools.android.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import instinctools.android.fragments.search.SearchFragment;
import instinctools.android.models.github.search.SearchRequest;
import instinctools.android.models.github.search.SearchResponse;
import instinctools.android.services.github.search.GithubServiceSearch;

public class AsyncSearchRequestLoader extends AsyncTaskLoader<SearchResponse> {
    private SearchRequest mRequest;

    public AsyncSearchRequestLoader(Context context, Bundle bundle) {
        super(context);

        mRequest = bundle.getParcelable(SearchFragment.BUNDLE_SEARCH_REQUEST);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public SearchResponse loadInBackground() {
        return GithubServiceSearch.getSearch(mRequest);
    }
}
