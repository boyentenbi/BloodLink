package com.mycompany.bloodlinkhospital;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * ****** Adapter class extends with BaseAdapter and implements with OnClickListener ***********
 */
public class AlertsExpandableListAdapter extends BaseExpandableListAdapter {

    /**
     * ******** Declare Used Variables ********
     */

    private AlertsFragment alertsFragment;
    private LayoutInflater inflater;
    private ArrayList<Incident> alertsList;
    public Resources res;


    public AlertsExpandableListAdapter(AlertsFragment alertsFragment, ArrayList alertsList, Resources resLocal) {

        this.alertsFragment = alertsFragment;
        this.inflater = LayoutInflater.from(alertsFragment.getActivity());
        this.alertsList = alertsList;
        this.res = resLocal;
    }

    // Holder class for child view
    public static class ChildViewHolder {
        public TextView requestMessageTextView;
        public Button directionsButton;
        public Button donatedButton;
    }

    // No 'child' objects. Only the request message is sort of a 'child'.
    @Override
    public Object getChild(int itemPosition, int childPosition) {
        return this.alertsList.get(itemPosition).getRequestMessage();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parentView) {

        // Set child 'fields' even though no child object exists
        final Incident incident = alertsList.get(groupPosition);
        final String requestMessage = incident.getRequestMessage();

        if (convertView == null) {
            // Inflate the row extension, or 'child row'.
            convertView = inflater.inflate(R.layout.active_requests_row_extension, parentView, false);

            // Set the request message text
            ((TextView) convertView.findViewById(R.id.requestMessageTextView)).setText(requestMessage);

            // Set listeners for the buttons in the row extension
            convertView.findViewById(R.id.directionsButton)
                    .setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            Toast.makeText(alertsFragment.getActivity(), "Pressed directions button", Toast.LENGTH_SHORT).show();
                        }
                    });

            convertView.findViewById(R.id.donatedButton)
                    .setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            alertsFragment.onDonatedButtonClick(incident);
                        }
                    });
        }

        return convertView;
    }

    // There is only 1 'child' for each row
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    public Object getGroup(int groupPosition) {
        return alertsList.get(groupPosition);
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public int getGroupCount() {
        return alertsList.size();
    }

    public static class ParentViewHolder {
        public TextView hospitalTextView;
        public TextView responsesHadTextView;
        public TextView bloodTypeTextView;
        public TextView timeLeftTextView;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parentView) {

        final Incident incident = alertsList.get(groupPosition);

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.active_requests_row, parentView, false);

            //int progress = (int)((incident.getResponsesHad()* 100.0f) / incident.getTotalResponsesNeeded());

            //((ProgressBar) convertView.findViewById((R.id.progress_horizontal))).setProgress(progress);

            ((TextView) convertView.findViewById(R.id.hospitalTextView))
                    .setText("Hospital number " + Integer.toString(incident.getHospitalId()));
            ((TextView) convertView.findViewById(R.id.responsesHadTextView))
                    .setText(incident.getResponsesHad() + "/" + incident.getTotalResponsesNeeded() + " responses");
            ((TextView) convertView.findViewById(R.id.bloodTypeTextView))
                    .setText(incident.getBloodType());
        }


        long timeDiffInMillis = incident.getRequestEndDate().getTime() - Calendar.getInstance().getTimeInMillis();
        long hoursDiff = timeDiffInMillis / 3600000;
        long minutesDiff = (timeDiffInMillis % 3600000) / 60000;
        long secondsDiff = (timeDiffInMillis % 60000) / 1000;

        if (hoursDiff == 0) {

            ((TextView) convertView.findViewById(R.id.timeLeftTextView))
                    .setText(minutesDiff + "m " + secondsDiff + "s remaining");

        } else {
            ((TextView) convertView.findViewById(R.id.timeLeftTextView))
                    .setText(hoursDiff + "h " + minutesDiff + "m " + secondsDiff + "s remaining");
        }

        return convertView;

    }


    @Override
    public void notifyDataSetChanged() {
        // Refresh List rows
        super.notifyDataSetChanged();
    }

    public boolean isChildSelectable(int itemPosition, int childPosition) {
        return true;
    }

    public boolean hasStableIds() {
        return true;
    }

}




/*
    // This is the layout id that you use to inflate the view
    protected int getLayoutId(){
        return R.layout.active_requests_fragment;
    }
}
*/