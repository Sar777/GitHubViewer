package instinctools.android.fragments.repository;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
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
import instinctools.android.adapters.contents.ContentPathAdapter;
import instinctools.android.adapters.contents.ContentsAdapter;
import instinctools.android.decorations.DividerItemDecoration;
import instinctools.android.loaders.repository.AsyncRepositoryContentLoader;
import instinctools.android.models.github.contents.Content;

public class RepositoryContentFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Content>> {
    public static final String INTENT_FILTER_CONTENT_CLICK = "CONTENT_CLICK";
    public static final String EXTRA_CONTENT_PATH = "CONTENT_PATH";
    public static final String EXTRA_CONTENT_NAME = "CONTENT_NAME";

    // View
    private ViewGroup mViewGroupContainer;
    private RecyclerView mRecyclerViewContent;
    private RecyclerView mRecyclerViewContentPath;
    private ProgressBar mProgressBar;
    private AbstractRecyclerAdapter mContentAdapter;
    private AbstractRecyclerAdapter mContentPathAdapter;

    private String mCurrentPath;

    // Broadcast
    private BroadcastReceiver mContentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mCurrentPath = intent.getStringExtra(EXTRA_CONTENT_PATH);

            mRecyclerViewContent.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);

            mContentPathAdapter.getResource().add(intent.getStringExtra(EXTRA_CONTENT_NAME));
            //mContentPathAdapter.notifyItemInserted(mContentPathAdapter.getResource().size() - 1);
            mContentPathAdapter.notifyDataSetChanged();

            getActivity().getSupportLoaderManager().restartLoader(RepositoryDescriptionActivity.LOADER_REPOSITORY_CONTENT_ID, Bundle.EMPTY, RepositoryContentFragment.this);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mContentReceiver, new IntentFilter(INTENT_FILTER_CONTENT_CLICK));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_repository_description_content, null);

        // TODO fix me
        mCurrentPath = "";

        mViewGroupContainer = (ViewGroup) view.findViewById(R.id.layout_repository_description_content);

        mRecyclerViewContent = (RecyclerView) view.findViewById(R.id.recycler_repository_content);
        mRecyclerViewContent.setVisibility(View.INVISIBLE);

        mRecyclerViewContentPath = (RecyclerView) view.findViewById(R.id.recycler_repository_content_path);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerViewContentPath.setLayoutManager(linearLayoutManager);

        mContentPathAdapter = new ContentPathAdapter(getContext());
        mRecyclerViewContentPath.setAdapter(mContentPathAdapter);

        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_loading_repository_content);
        mProgressBar.setVisibility(View.VISIBLE);

        mRecyclerViewContent.setLayoutManager(new LinearLayoutManager(getContext()));

        mContentAdapter = new ContentsAdapter(getContext());
        mRecyclerViewContent.setAdapter(mContentAdapter);

        mRecyclerViewContent.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST, false));

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
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mContentReceiver);
        super.onDestroy();
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

        mRecyclerViewContent.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<Content>> loader) {

    }
}
