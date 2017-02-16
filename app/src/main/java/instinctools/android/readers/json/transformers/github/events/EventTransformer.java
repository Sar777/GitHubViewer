package instinctools.android.readers.json.transformers.github.events;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import instinctools.android.models.github.events.Event;
import instinctools.android.models.github.events.enums.EventType;
import instinctools.android.readers.json.transformers.ITransformer;
import instinctools.android.utility.GitHubDate;

public class EventTransformer implements ITransformer<Event> {
    private static final String TAG = "EventTransformer";

    private static final String J_ID = "id";
    private static final String J_TYPE = "type";
    private static final String J_PUBLIC = "public";
    private static final String J_CREATED_AT = "created_at";

    private static final String J_PAYLOAD = "payload";
    private static final String J_REPO = "repo";
    private static final String J_ACTOR = "actor";
    private static final String J_ORG = "org";

    @Override
    public Event transform(Object object) {
        JSONObject jsonObject;
        if (object instanceof JSONObject)
            jsonObject = (JSONObject)object;
        else if (object instanceof String) {
            try {
                jsonObject = new JSONObject((String)object);
            } catch (JSONException e) {
                Log.e(TAG, "Create json object error...", e);
                return null;
            }
        } else
            return null;

        Event event = new Event();
        try {
            event.setId(jsonObject.getInt(J_ID));
            event.setCreatedAt(GitHubDate.parse(jsonObject.getString(J_CREATED_AT)));
            event.setIsPublic(jsonObject.getBoolean(J_PUBLIC));
            event.setType(EventType.get(jsonObject.getString(J_TYPE)));

            event.setPayload(new EventPayloadTransformer().transform(jsonObject.getJSONObject(J_PAYLOAD)));
            event.setActor(new EventActorTransformer().transform(jsonObject.getJSONObject(J_ACTOR)));

            if (jsonObject.has(J_REPO))
                event.setRepo(new EventRepoTransformer().transform(jsonObject.getJSONObject(J_REPO)));

            if (jsonObject.has(J_ORG))
                event.setOrg(new EventOrganizationTransformer().transform(jsonObject.getJSONObject(J_ORG)));
        } catch (JSONException e) {
            Log.e(TAG, "Parse json field error...", e);
            return null;
        }

        return event;
    }
}
