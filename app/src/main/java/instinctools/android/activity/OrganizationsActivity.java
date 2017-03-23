package instinctools.android.activity;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import instinctools.android.R;
import instinctools.android.adapters.organizations.OrganizationsAdapter;
import instinctools.android.decorations.DividerItemDecoration;
import instinctools.android.loaders.organizations.AsyncOrganizationsListLoader;
import instinctools.android.models.github.organizations.Organization;

public class OrganizationsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Organization>> {
    private static final int LOADER_ORGANIZATIONS_ID = 1;

    // View
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;

    private OrganizationsAdapter mOrganizationsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizations);

        initView();

        getSupportLoaderManager().initLoader(LOADER_ORGANIZATIONS_ID, Bundle.EMPTY, this);
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_organizations);
        mProgressBar.setVisibility(View.VISIBLE);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_organizations_list);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mOrganizationsAdapter = new OrganizationsAdapter(this);
        mRecyclerView.setAdapter(mOrganizationsAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST, false));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public Loader<List<Organization>> onCreateLoader(int i, Bundle bundle) {
        return new AsyncOrganizationsListLoader(this, null);
    }

    @Override
    public void onLoadFinished(Loader<List<Organization>> loader, List<Organization> organizations) {
        mOrganizationsAdapter.setResource(organizations);
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<List<Organization>> loader) {

    }
}
