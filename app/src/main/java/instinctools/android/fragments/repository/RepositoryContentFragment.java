package instinctools.android.fragments.repository;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import instinctools.android.R;
import instinctools.android.activity.RepositoryDescriptionActivity;
import instinctools.android.adapters.AbstractRecyclerAdapter;
import instinctools.android.adapters.contents.ContentsAdapter;
import instinctools.android.decorations.DividerItemDecoration;
import instinctools.android.loaders.repository.AsyncRepositoryContentLoader;
import instinctools.android.models.github.contents.Content;

public class RepositoryContentFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Content>> {
    // View
    private ViewGroup mViewGroupContainer;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private AbstractRecyclerAdapter mContentAdapter;

    private String mCurrentPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_repository_description_content, null);

        // TODO fix me
        mCurrentPath = "";

        mViewGroupContainer = (ViewGroup) view.findViewById(R.id.layout_repository_description_content);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_repository_content);
        mRecyclerView.setVisibility(View.INVISIBLE);

        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_loading_repository_content);
        mProgressBar.setVisibility(View.VISIBLE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mContentAdapter = new ContentsAdapter(getContext());
        mRecyclerView.setAdapter(mContentAdapter);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST, false));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getSupportLoaderManager().initLoader(RepositoryDescriptionActivity.LOADER_REPOSITORY_CONTENT_ID, Bundle.EMPTY, this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().getSupportLoaderManager().destroyLoader(RepositoryDescriptionActivity.LOADER_REPOSITORY_CONTENT_ID);
    }

    @Override
    public Loader<List<Content>> onCreateLoader(int id, Bundle args) {
        return new AsyncRepositoryContentLoader(getContext(), ((RepositoryDescriptionActivity)getActivity()).getFullName(), mCurrentPath);
    }

    @Override
    public void onLoadFinished(Loader<List<Content>> loader, List<Content> contents) {
        if (contents != null)
            mContentAdapter.setResource(contents);
        else
            Snackbar.make(mViewGroupContainer, R.string.msg_repository_description_content_loading_fail, Snackbar.LENGTH_SHORT).show();

        mContentAdapter.notifyDataSetChanged();

        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<Content>> loader) {

    }
}
