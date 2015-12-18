
package com.mycompany.bloodlinkhospital;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AlertsFragment extends Fragment {

    protected ExpandableListView expandableListView;
    //protected samples.exoguru.materialtabs.AlertsExpandableListAdapter alertsListViewAdapter;
    private ArrayList<Incident> alertsList = new ArrayList<>();
    public SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {


        // Inflate (render) the view
        View view = inflater.inflate(R.layout.active_requests_fragment, parent, false);

        //this.expandableListView = (ExpandableListView) view;

        return view;

    }

    private void setAlertsListData() {

        // Create dummy Incident objects
        for (int i = 1; i <= 20; i++) {

            int incidentId = i;
            int hospitalId = 0;
            String bloodType = "B+";
            String requestMessage = "more blood for incident " + Integer.toString(incidentId)
                    + " at hospital " + Integer.toString(hospitalId) + " plz!";
            String thanksMessage = "thanksMessage set in dummy creation method";
            int responsesNeeded = 20;
            int responsesHad = 0;


            Calendar cal = Calendar.getInstance(); // creates calendar
            cal.setTime(new Date()); // sets calendar time/date
            cal.add(Calendar.HOUR_OF_DAY, 1); // adds one hour
            Date requestEndTime = cal.getTime(); // returns new date object, one hour in the future


            Incident incident = new Incident(incidentId, hospitalId,
                    bloodType, responsesNeeded, requestMessage, thanksMessage, requestEndTime, responsesHad);

            this.alertsList.add(incident);


        }

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // Create dummy data if data doesn't already exist
        if (alertsList.isEmpty()) {
            setAlertsListData();
            Toast.makeText(this.getActivity(), "made a new data set", Toast.LENGTH_SHORT).show();
        }

        this.swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        for (int position = 1; position < alertsList.size(); position++) {
                            alertsList.get(position).getView().invalidate();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }

        );


        Resources res = getResources();

        final ExpandableListView alertsExpandableListView = (ExpandableListView) getActivity().findViewById(R.id.alertsExpandableListView);

        final AlertsExpandableListAdapter alertsExpandableListAdapter = new AlertsExpandableListAdapter(this, alertsList, res);

        // Set adapter to view
        alertsExpandableListView.setAdapter(alertsExpandableListAdapter);

        alertsExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener()

                                                          {

                                                              public void onGroupExpand(int groupPosition) {

                                                                  for (int i = 0; i < alertsExpandableListAdapter.getGroupCount(); i++) {
                                                                      if (i != groupPosition) {
                                                                          alertsExpandableListView.collapseGroup(i);
                                                                      }
                                                                  }
                                                              }

                                                          }

        );

    }


    public void onDonatedButtonClick(Incident incident) {

        ForceCompleteFragment dFragment = new ForceCompleteFragment();
        dFragment.setIncident(incident);
        dFragment.setAlertsFragment(this);

        // Show DialogFragment
        dFragment.show(getActivity().getSupportFragmentManager(), "Dialog Fragment");
    }


}



