
package samples.exoguru.bloodlink;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

public class AlertsFragment extends Fragment {

    MainActivity mainActivity;
    protected ExpandableListView expandableListView;
    protected AlertsExpandableListAdapter alertsExpandableListAdapter;
    protected ExpandableListView alertsExpandableListView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        // Inflate (render) the view
        View view = inflater.inflate(R.layout.alerts_fragment, parent, false);

        // Add reference to activity
        this.mainActivity = (MainActivity) getActivity();

        ((MainActivity) getActivity()).alertsFragment = this;

        //this.expandableListView = (ExpandableListView) view;

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        Resources res = getResources();

        this.alertsExpandableListView = (ExpandableListView) getActivity().findViewById(R.id.alertsExpandableListView);

        this.alertsExpandableListAdapter = new AlertsExpandableListAdapter(this, res);

        // Set adapter to view
        alertsExpandableListView.setAdapter(alertsExpandableListAdapter);

        alertsExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener()

                                                          {

                                                              public void onGroupExpand(int groupPosition) {

                                                                  for (int i = 0; i < alertsExpandableListAdapter.getIncidentsList().size(); i++) {
                                                                      if (i != groupPosition) {
                                                                          alertsExpandableListView.collapseGroup(i);
                                                                          //alertsExpandableListView.setBackgroundColor(Color.WHITE);
                                                                      }
                                                                  }
                                                              }

                                                          }
        );

    }

    public void onDonatedButtonClick(Incident incident) {

        ConfirmDonationFragment dFragment = new ConfirmDonationFragment();
        dFragment.setMainActivity(mainActivity);
        dFragment.setIncident(incident);

        // Show DialogFragment
        dFragment.show(getActivity().getSupportFragmentManager(), "Dialog Fragment");
    }

    @Override
    public void onResume() {
        //Update the incidentsList object and the view
        super.onResume();
        alertsExpandableListAdapter.alertsList = IncidentsList.getEligibleNowAlertsList();
        alertsExpandableListAdapter.notifyDataSetChanged();
    }


}



