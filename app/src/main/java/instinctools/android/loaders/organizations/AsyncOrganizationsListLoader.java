package instinctools.android.loaders.organizations;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

import instinctools.android.models.github.organizations.Organization;
import instinctools.android.services.github.user.GithubServiceUser;

public class AsyncOrganizationsListLoader extends AsyncTaskLoader<List<Organization>> {
    private String mUsername;

    public AsyncOrganizationsListLoader(Context context, String username) {
        super(context);

        this.mUsername = username;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Organization> loadInBackground() {
        return GithubServiceUser.getOrganizationsList(mUsername);
    }
}
