package instinctools.android.models.github.events;

import android.content.ContentValues;
import android.support.annotation.Nullable;

import java.util.Date;

import instinctools.android.database.DBConstants;
import instinctools.android.models.github.events.enums.EventType;
import instinctools.android.models.github.events.payload.Payload;

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

    @Nullable
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

    @Nullable
    public EventOrganization getOrg() {
        return mOrg;
    }

    public void setOrg(EventOrganization org) {
        this.mOrg = org;
    }

    public ContentValues build() {
        ContentValues values = new ContentValues();

        values.put(DBConstants.EVENT_ID, mId);
        values.put(DBConstants.EVENT_TYPE, mType.toString());
        values.put(DBConstants.EVENT_CREATED_AT, mCreatedAt.getTime());
        values.put(DBConstants.EVENT_IS_PUBLIC, mIsPublic);
        return values;
    }
}
