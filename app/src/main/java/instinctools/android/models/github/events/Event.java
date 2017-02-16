package instinctools.android.models.github.events;

import java.util.Date;

import instinctools.android.models.github.events.enums.EventType;

public class Event {
    private Integer mId;
    private Date mCreatedAt;
    private EventType mType;
    private boolean mIsPublic;
    private Payload mPayload;
    private EventRepository mRepo;
    private Actor mActor;
    private EventOrganization mOrg;

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
        this.mId = id;
    }

    public Date getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.mCreatedAt = createdAt;
    }

    public EventType getType() {
        return mType;
    }

    public void setType(EventType type) {
        this.mType = type;
    }

    public boolean isPublic() {
        return mIsPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.mIsPublic = isPublic;
    }

    public Payload getPayload() {
        return mPayload;
    }

    public void setPayload(Payload payload) {
        this.mPayload = payload;
    }

    public EventRepository getRepo() {
        return mRepo;
    }

    public void setRepo(EventRepository repo) {
        this.mRepo = repo;
    }

    public Actor getActor() {
        return mActor;
    }

    public void setActor(Actor actor) {
        this.mActor = actor;
    }

    public EventOrganization getOrg() {
        return mOrg;
    }

    public void setOrg(EventOrganization org) {
        this.mOrg = org;
    }
}
