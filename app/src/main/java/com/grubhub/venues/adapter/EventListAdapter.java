package com.grubhub.venues.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.grubhub.venues.R;
import com.grubhub.venues.model.Event;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EventListAdapter extends BaseAdapter {

    private Context context;
    private List<Event> mEvents;

    public EventListAdapter(Context context, List<Event> events) {
        this.context = context;
        this.mEvents = new ArrayList<>(events);
    }

    @Override
    public int getCount() {
        return mEvents.size();
    }

    @Override
    public Object getItem(int i) {
        return mEvents.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // create a new view
        View v = LayoutInflater.from(context)
                .inflate(R.layout.list_item_event, viewGroup, false);

        final Event e = mEvents.get(i);

        ImageView mThumbnail = (ImageView) v.findViewById(R.id.thumbnail);

        if (!e.getPerformerHeroImage().isEmpty() && e.getPerformerHeroImage() != null) {
            Picasso.with(context).load(e.getPerformerHeroImage()).error(R.drawable.default_image).into(mThumbnail);
        } else if (!e.getCategoryHeroImage().isEmpty() && e.getCategoryHeroImage() != null) {
            Picasso.with(context).load(e.getCategoryHeroImage()).error(R.drawable.default_image).into(mThumbnail);
        }

        ((TextView) v.findViewById(R.id.name)).setText(e.getName());
        ((TextView) v.findViewById(R.id.date)).setText(e.getDateDisplay());

        return v;
    }
}
