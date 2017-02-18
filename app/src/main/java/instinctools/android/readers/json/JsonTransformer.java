package instinctools.android.readers.json;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import instinctools.android.models.github.authorization.AccessToken;
import instinctools.android.models.github.commits.Commit;
import instinctools.android.models.github.errors.ErrorResponse;
import instinctools.android.models.github.events.Event;
import instinctools.android.models.github.issues.Issue;
import instinctools.android.models.github.issues.IssueLabel;
import instinctools.android.models.github.notification.Notification;
import instinctools.android.models.github.organizations.Organization;
import instinctools.android.models.github.repositories.Repository;
import instinctools.android.models.github.repositories.RepositoryReadme;
import instinctools.android.models.github.search.SearchResponse;
import instinctools.android.models.github.user.User;
import instinctools.android.readers.json.transformers.ITransformer;
import instinctools.android.readers.json.transformers.github.authorization.AccessTokenTransformer;
import instinctools.android.readers.json.transformers.github.commits.CommitTransformer;
import instinctools.android.readers.json.transformers.github.commits.ListCommitsTransformer;
import instinctools.android.readers.json.transformers.github.errors.ErrorResponseTransformer;
import instinctools.android.readers.json.transformers.github.events.EventTransformer;
import instinctools.android.readers.json.transformers.github.events.ListEventsTransformer;
import instinctools.android.readers.json.transformers.github.issues.IssueLabelTransformer;
import instinctools.android.readers.json.transformers.github.issues.IssueTransformer;
import instinctools.android.readers.json.transformers.github.issues.ListIssueLabelTransformer;
import instinctools.android.readers.json.transformers.github.issues.ListIssueTransformer;
import instinctools.android.readers.json.transformers.github.notification.ListNotificationsTransformer;
import instinctools.android.readers.json.transformers.github.notification.NotificationTransformer;
import instinctools.android.readers.json.transformers.github.organizations.ListOrganizationsTransformer;
import instinctools.android.readers.json.transformers.github.organizations.OrganizationTransformer;
import instinctools.android.readers.json.transformers.github.repository.ListRepositoriesTransformer;
import instinctools.android.readers.json.transformers.github.repository.RepositoryReadmeTransformer;
import instinctools.android.readers.json.transformers.github.repository.RepositoryTransformer;
import instinctools.android.readers.json.transformers.github.search.SearchResponseTransformer;
import instinctools.android.readers.json.transformers.github.user.UserTransformer;

public class JsonTransformer {
    private static final String TAG = "JsonTransformer";

    private static Map<String, Class<? extends ITransformer>> mTransformersMap = new HashMap<>();

    static {
        // User
        mTransformersMap.put(User.class.getName(), UserTransformer.class);
        // Auth
        mTransformersMap.put(AccessToken.class.getName(), AccessTokenTransformer.class);
        // Repository
        mTransformersMap.put(Repository.class.getName(), RepositoryTransformer.class);
        mTransformersMap.put(Repository[].class.getName(), ListRepositoriesTransformer.class);
        mTransformersMap.put(RepositoryReadme.class.getName(), RepositoryReadmeTransformer.class);
        // Errors
        mTransformersMap.put(ErrorResponse.class.getName(), ErrorResponseTransformer.class);
        // Search
        mTransformersMap.put(SearchResponse.class.getName(), SearchResponseTransformer.class);
        // Notifications
        mTransformersMap.put(Notification.class.getName(), NotificationTransformer.class);
        mTransformersMap.put(Notification[].class.getName(), ListNotificationsTransformer.class);
        // Issues
        mTransformersMap.put(Issue.class.getName(), IssueTransformer.class);
        mTransformersMap.put(Issue[].class.getName(), ListIssueTransformer.class);
        mTransformersMap.put(IssueLabel.class.getName(), IssueLabelTransformer.class);
        mTransformersMap.put(IssueLabel[].class.getName(), ListIssueLabelTransformer.class);
        // Commits
        mTransformersMap.put(Commit.class.getName(), CommitTransformer.class);
        mTransformersMap.put(Commit[].class.getName(), ListCommitsTransformer.class);
        // Events
        mTransformersMap.put(Event.class.getName(), EventTransformer.class);
        mTransformersMap.put(Event[].class.getName(), ListEventsTransformer.class);
        // Organizations
        mTransformersMap.put(Organization.class.getName(), OrganizationTransformer.class);
        mTransformersMap.put(Organization[].class.getName(), ListOrganizationsTransformer.class);
    }

    public static <Model, T> Model transform(@NonNull String json, Class<T> clazz) {
        if (!mTransformersMap.containsKey(clazz.getName()))
            throw new UnsupportedOperationException("Not found transformer for class " + clazz.getName());

        try {
            ITransformer transformer = mTransformersMap.get(clazz.getName()).newInstance();
            return (Model) transformer.transform(json);
        } catch (Exception e) {
            Log.e(TAG, "Transform exception", e);
        }

        return null;
    }
}
