package instinctools.android.services;

import android.content.Intent;

import java.util.ArrayList;

import instinctools.android.activity.SearchActivity;
import instinctools.android.constans.Constants;
import instinctools.android.models.github.repositories.Repository;
import instinctools.android.models.github.search.SearchRequest;
import instinctools.android.models.github.search.SearchResponse;
import instinctools.android.services.github.search.GithubServiceSearch;

public class HttpSearchRepositoryService extends HttpRepositoryService {
    public HttpSearchRepositoryService() {
        this.mTypeInfo = Constants.REPOSITORY_TYPE_SEARCH;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SearchResponse searchResponse = GithubServiceSearch.getSearchRepository((SearchRequest) intent.getParcelableExtra(SearchActivity.INTENT_SEARCH_REQUEST));
        if (searchResponse == null) {
            // Cleanup
            onHandleIntent(new ArrayList<Repository>());
            return;
        }

        onHandleIntent(searchResponse.getRepositories());
    }
}
