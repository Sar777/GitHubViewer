package instinctools.android.loaders;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

import instinctools.android.models.github.organizations.Organization;
import instinctools.android.services.github.user.GithubServiceUser;

public class AsyncOrganizationsLoader extends AsyncTaskLoader<List<Organization>> {
    private String mUsername;

    public AsyncOrganizationsLoader(Context context, @Nullable String username) {
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
