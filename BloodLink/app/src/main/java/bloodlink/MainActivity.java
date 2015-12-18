package samples.exoguru.bloodlink;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Map;

/**
 * Created by Edwin on 15/02/2015.
 */
public class MainActivity extends ActionBarActivity {

    // Declaring Your View and Variables

    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[];
    int Numboftabs = 2;
    AlertsFragment alertsFragment;
    HistoryFragment historyFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        this.Titles = new CharSequence[Numboftabs];

        this.Titles[0] = getString(R.string.alertsTabTitle);
        this.Titles[1] = getString(R.string.historyTabTitle);

        new GcmRegistrationAsyncTask(this).execute();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creating The Toolbar and setting it as the Toolbar for the activity

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assigning the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

        // Set the static reference in IncidentsList
        IncidentsList.mainActivity = this;



        //registerReceiver(messageReceiver, new IntentFilter("updateActivity"));


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register receiver
        registerReceiver(messageReceiver, new IntentFilter("updateActivity"));

        // Check if this is the first use and unset blood type
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Map prefsMap = preferences.getAll();
        boolean isFirstTime = preferences.getBoolean("isFirstTime", true);
        // If it is, prompt the user to set up
        if (isFirstTime || prefsMap.get("bloodType")==null) {

            Log.i("MainActivity", "First time use or bloodType not set!");

            // Show DialogFragment
            FirstSetupFragment firstSetupFragment = new FirstSetupFragment();
            firstSetupFragment.show(getSupportFragmentManager(), "Dialog Fragment");

        } else {
            Log.i("MainActivity", "NOT first time use!");

        }




    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(messageReceiver);
    }

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Extract data included in the Intent
            //String message = intent.getStringExtra("message");

            //Update the incidentsList object and the view
            alertsFragment.alertsExpandableListAdapter.alertsList = IncidentsList.getEligibleNowAlertsList();
            alertsFragment.alertsExpandableListAdapter.notifyDataSetChanged();
        }
    };

}