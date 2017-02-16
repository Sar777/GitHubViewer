package instinctools.android.models.github.events.enums;

import java.util.HashMap;
import java.util.Map;

public enum EventType {
    COMMIT_COMMENT("CommitComment"),
    CREATE("Create"),
    DELETE("Delete"),
    DEPLOYMENT("Deployment"),
    DEPLOYMENT_STATUS("DeploymentStatus"),
    DOWNLOAD("Download"),
    FOLLOW("Follow"),
    FORK("Fork"),
    FORK_APPLY("ForkApply"),
    GIST("Gist"),
    GOLLUM("Gollum"),
    ISSUE_COMMENT("IssueComment"),
    ISSUES("Issues"),
    LABEL("Label"),
    MEMBER("Member"),
    MEMBERSHIP("Membership"),
    MILESTONE("Milestone"),
    ORGANIZATION("Organization"),
    PAGE_BUILD("PageBuild"),
    PROJECT_CARD("ProjectCard"),
    PROJECT_COLUMN("ProjectColumn"),
    PROJECT("Project"),
    PUBLIC("Public"),
    PULL_REQUEST("PullRequest"),
    PULL_REQUEST_REVIEW("PullRequestReview"),
    PULL_REQUEST_REVIEW_COMMENT("PullRequestReviewComment"),
    PUSH("Push"),
    RELEASE("Release"),
    REPOSITORY("Repository"),
    STATUS("Status"),
    REAM("Team"),
    TEAM_ADD("TeamAdd"),
    WATCH("Watch");

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
