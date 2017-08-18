package com.lechlitnerd.vividseats.activity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.lechlitnerd.vividseats.R;
import com.lechlitnerd.vividseats.constant.PreferenceConstants;
import com.lechlitnerd.vividseats.constant.RequestConstants;
import com.lechlitnerd.vividseats.fragment.EventListFragment;
import com.lechlitnerd.vividseats.manager.PreferencesManager;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        // Set the Preference Manager
        PreferencesManager.settings = PreferenceManager.getDefaultSharedPreferences(this);

        // Set to Dev
        PreferencesManager.storePreference(PreferenceConstants.PREF_ENVIRONMENT, RequestConstants.SERVER_URL_DEV, false);

        setContentView(R.layout.activity_main);

        /**
         * Show the Event List
         */
        this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, EventListFragment.newInstance())
                .commit();
    }

    /**
     * Swaps in the new Fragment and places the previous Fragment on the backstack
     *
     * @param f - Fragment to be swapped into view
     */
    public void swapFragment(Fragment f) {

        if (f != null) {
            FragmentManager fragmentManager = this.getSupportFragmentManager();

            FragmentTransaction transaction = fragmentManager.beginTransaction();

            transaction.replace(R.id.container, f)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
