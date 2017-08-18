package com.lechlitnerd.vividseats.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lechlitnerd.vividseats.R;
import com.lechlitnerd.vividseats.model.Event;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benlechlitner on 1/3/16.
 */
public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {
    private List<Event> mEvents;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View mRow;
        public TextView mName;
        public TextView mDate;
        public ImageView mThumbnail;

        public ViewHolder(View v) {
            super(v);

            mRow = v;
            mThumbnail = (ImageView) v.findViewById(R.id.thumbnail);
            mName = (TextView) v.findViewById(R.id.name);
            mDate = (TextView) v.findViewById(R.id.date);
        }
    }

    public EventListAdapter(Context context, List<Event> events) {
        mContext = context;
        mEvents = new ArrayList<>(events);
    }

    @Override
    public EventListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_event, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final Event e = mEvents.get(position);

        holder.mName.setText(e.getName());
        holder.mDate.setText(e.getDateDisplay());

        if (!e.getPerformerHeroImage().isEmpty() && e.getPerformerHeroImage() != null) {
            Picasso.with(mContext).load(e.getPerformerHeroImage()).error(R.drawable.default_image).into(holder.mThumbnail);
        } else if (!e.getCategoryHeroImage().isEmpty() && e.getCategoryHeroImage() != null) {
            Picasso.with(mContext).load(e.getCategoryHeroImage()).error(R.drawable.default_image).into(holder.mThumbnail);
        }
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    public Object getItem(int position) { return mEvents.get(position); }
}
