package com.grubhub.venues.model;

import java.util.Date;

public class Event {
    private int mId;
    private String mName;
    private int mEventCount;
    private int mRank;
    private String mVenue;
    private String mCity;
    private String mState;
    private String mPerformerHeroImage;
    private String mCategoryHeroImage;
    private String mVenueDisplay;
    private String mDateDisplay;
    private Date mStartDate;

    public Event(int id,
                 String name,
                 int eventCount,
                 int rank,
                 String venue,
                 String city,
                 String state,
                 String performerHeroImage,
                 String categoryHeroImage,
                 String venueDisplay,
                 String dateDisplay,
                 Date startDate)
    {
        mId = id;
        mName = name;
        mEventCount = eventCount;
        mRank = rank;
        mVenue = venue;
        mCity = city;
        mState = state;
        mPerformerHeroImage = performerHeroImage;
        mCategoryHeroImage = categoryHeroImage;
        mVenueDisplay = venueDisplay;
        mDateDisplay = dateDisplay;
        mStartDate = startDate;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public int getEventCount() {
        return mEventCount;
    }

    public int getRank() {
        return mRank;
    }

    public String getVenue() {
        return mVenue;
    }

    public String getCity() {
        return mCity;
    }

    public String getState() {
        return mState;
    }

    public String getPerformerHeroImage() {
        return mPerformerHeroImage;
    }

    public String getCategoryHeroImage() {
        return mCategoryHeroImage;
    }

    public String getVenueDisplay() {
        return mVenueDisplay;
    }

    public String getDateDisplay() {
        return mDateDisplay;
    }

    public Date getStartDate() {
        return mStartDate;
    }
}
