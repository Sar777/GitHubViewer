package instinctools.android.models.github.events.enums;

import java.util.HashMap;
import java.util.Map;

public enum EventType {
    COMMIT_COMMENT("CommitCommentEvent"),
    CREATE("CreateEvent"),
    DELETE("DeleteEvent"),
    DEPLOYMENT("DeploymentEvent"),
    DEPLOYMENT_STATUS("DeploymentStatusEvent"),
    DOWNLOAD("DownloadEvent"),
    FOLLOW("FollowEvent"),
    FORK("ForkEvent"),
    FORK_APPLY("ForkApplyEvent"),
    GIST("GistEvent"),
    GOLLUM("GollumEvent"),
    ISSUE_COMMENT("IssueCommentEvent"),
    ISSUES("IssuesEvent"),
    LABEL("LabelEvent"),
    MEMBER("MemberEvent"),
    MEMBERSHIP("MembershipEvent"),
    MILESTONE("MilestoneEvent"),
    ORGANIZATION("OrganizationEvent"),
    PAGE_BUILD("PageBuildEvent"),
    PROJECT_CARD("ProjectCardEvent"),
    PROJECT_COLUMN("ProjectColumnEvent"),
    PROJECT("ProjectEvent"),
    PUBLIC("PublicEvent"),
    PULL_REQUEST("PullRequestEvent"),
    PULL_REQUEST_REVIEW("PullRequestReviewEvent"),
    PULL_REQUEST_REVIEW_COMMENT("PullRequestReviewCommentEvent"),
    PUSH("PushEvent"),
    RELEASE("ReleaseEvent"),
    REPOSITORY("RepositoryEvent"),
    STATUS("StatusEvent"),
    REAM("TeamEvent"),
    TEAM_ADD("TeamAddEvent"),
    WATCH("WatchEvent");

    private String mType;

    private static final Map<String, EventType> mEvents = new HashMap<>();

    static {
        for (EventType type : EventType.values()) {
            mEvents.put(type.toString(), type);
        }
    }

    EventType(String type) {
        this.mType = type;
    }

    public void setType(String type) {
        this.mType = type;
    }

    public static EventType get(String type) {
        EventType order = mEvents.get(type);
        if (order == null)
            throw new IllegalArgumentException("Unknown event type: " + type);

        return order;
    }

    @Override
    public String toString() {
        return mType;
    }
}
