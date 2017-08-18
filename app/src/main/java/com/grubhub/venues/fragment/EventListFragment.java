package com.grubhub.venues.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grubhub.venues.R;
import com.grubhub.venues.adapter.RecyclerEventListAdapter;
import com.grubhub.venues.constant.RequestConstants;
import com.grubhub.venues.manager.EventCommand;
import com.grubhub.venues.networking.handler.IResponseHandler;
import com.grubhub.venues.model.Event;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventListFragment extends Fragment implements IResponseHandler {
    private static final String TAG = "EventListFragment";

    private Fragment mSelf;
    private ViewGroup mRootView;

    private RecyclerView mEvents;
    private RecyclerEventListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */
    public static EventListFragment newInstance() {
        EventListFragment fragment = new EventListFragment();

        return fragment;
    }

    public EventListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_event_list, container, false);

        mEvents = (RecyclerView) mRootView.findViewById(R.id.events);
        TextView date = (TextView) mRootView.findViewById(R.id.date);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM d", Locale.getDefault());

        date.setText("Today is " + sdf.format(new Date()));

        mSelf = this;

        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");

        showLoading();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * public void processResponse(String action, JSONObject response);
     * <p/>
     * Each class that wants to handle a response from the backend should
     * implement this interface and override this method.
     *
     * @param action
     * @param response
     */
    @Override
    public void processResponse(String action, JSONObject response) {
        Log.d(TAG, "processResponse: " + action);
        if (response != null) {
            if (action.equals(RequestConstants.ACTION_LOAD)) {
                List<Event> events = EventCommand.parseEvents(response);

                /**
                 * Initialize the list of Events
                 */
                setup(events);

                /**
                 * Display the list of Events
                 */
                hideLoading();
            }
        }
    }

    /**
     * Initialize views with Store data
     *
     * @param events
     */
    private void setup(List<Event> events) {
        mEvents.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mEvents.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerEventListAdapter(this.getActivity(), events);
        mEvents.setAdapter(mAdapter);
    }

    /**
     * Show the loading indicator while list of Stores is created
     */
    private void showLoading() {
        final View loading = mRootView.findViewById(R.id.progress);
        loading.setVisibility(View.VISIBLE);
        mEvents.setVisibility(View.GONE);
    }

    /**
     * Hide the loading indicator and show the views which represent Stores
     */
    private void hideLoading() {
        final View loading = mRootView.findViewById(R.id.progress);
        loading.setVisibility(View.GONE);

        mEvents.setVisibility(View.VISIBLE);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }
}
