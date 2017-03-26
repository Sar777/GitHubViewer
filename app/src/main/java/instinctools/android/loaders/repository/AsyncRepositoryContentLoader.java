package instinctools.android.loaders.repository;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import instinctools.android.models.github.contents.Content;
import instinctools.android.services.github.repository.GithubServiceRepository;

public class AsyncRepositoryContentLoader extends AsyncTaskLoader<List<Content>> {
    private String mFullname;
    private String mPath;

    public AsyncRepositoryContentLoader(Context context, String fullname, String path) {
        super(context);

        this.mFullname = fullname;
        this.mPath = path;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Content> loadInBackground() {
        List<Content> contents = GithubServiceRepository.getContent(mFullname, mPath);
        if (contents == null)
            return null;

        Collections.sort(contents, new Comparator<Content>() {
            @Override
            public int compare(Content left, Content right) {
                return right.getType().compareTo(left.getType());
            }
        });

        return contents;
    }
}
