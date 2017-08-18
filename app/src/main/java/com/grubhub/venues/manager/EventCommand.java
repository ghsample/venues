package com.grubhub.venues.manager;

import android.util.Log;

import com.grubhub.venues.constant.RequestConstants;
import com.grubhub.venues.networking.handler.IResponseHandler;
import com.grubhub.venues.model.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventCommand {

    private static final String TAG = "EventCommand";

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String EVENT_COUNT = "eventCount";
    private static final String RANK = "rank";
    private static final String VENUE = "venue";
    private static final String CITY = "city";
    private static final String STATE = "state";
    private static final String PERFORMER_HERO_IMAGE = "performerHeroImage";
    private static final String CATEGORY_HERO_IMAGE = "categoryHeroImage";
    private static final String VENUE_DISPLAY = "venueDisplay";
    private static final String DATE_DISPLAY = "dateDisplay";
    private static final String START_DATE = "startDate";

    public static void getEvents(IResponseHandler handler)
    {
        try {
            RequestManager.get(handler, RequestConstants.GET_EVENTS, RequestConstants.ACTION_LOAD);
        } catch (Exception e) {
            Log.e(TAG, "Error requesting stores.", e);
        }
    }

    /**
     * Parse JSON for Event Objects
     * @param json
     * @return - A list of Events
     */
    public static List<Event> parseEvents(JSONObject json) {
        List<Event> events = new ArrayList<>();

        try {
            JSONArray eventArray = json.getJSONArray("response");

            JSONObject o = null;

            /**
             * Extract each Event and create an object.
             */
            for (int i = 0; i < eventArray.length(); i++) {
                o = eventArray.getJSONObject(i);
                events.add(new Event(
                        o.getInt(ID),
                        o.getString(NAME),
                        o.getInt(EVENT_COUNT),
                        o.getInt(RANK),
                        o.getString(VENUE),
                        o.getString(CITY),
                        o.getString(STATE),
                        o.getString(PERFORMER_HERO_IMAGE),
                        o.getString(CATEGORY_HERO_IMAGE),
                        o.getString(VENUE_DISPLAY),
                        o.getString(DATE_DISPLAY),
                        new Date(o.getLong(START_DATE))
                ));
            }
        } catch (JSONException e) {
            Log.e(TAG, "There was a problem parsing the Event JSON.", e);
        }

        return events;
    }
}
