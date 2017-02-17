package instinctools.android.models.github.events;


import android.content.ContentValues;

import instinctools.android.database.DBConstants;

public class EventRepository {
    private Integer mId;
    private String mName;
    private String mUrl;

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public ContentValues build() {
        ContentValues values = new ContentValues();

        values.put(DBConstants.EVENT_REPO_ID, mId);
        values.put(DBConstants.EVENT_REPO_NAME, mName);
        values.put(DBConstants.EVENT_REPO_URL, mUrl);
        return values;
    }
}
