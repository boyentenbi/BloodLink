package samples.exoguru.bloodlink;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ****** Adapter class extends with BaseAdapter and implements with OnClickListener ***********
 */
public class AlertsExpandableListAdapter extends BaseExpandableListAdapter {

    /**
     * ******** Declare Used Variables ********
     */

    private AlertsFragment alertsFragment;
    private LayoutInflater inflater;
    public IncidentsList alertsList;
    public Resources res;

    public AlertsExpandableListAdapter(AlertsFragment alertsFragment, Resources resLocal) {

        // load alerts from file
        this.alertsList = IncidentsList.getEligibleNowAlertsList();
        this.alertsFragment = alertsFragment;
        this.inflater = LayoutInflater.from(alertsFragment.getActivity());
        this.res = resLocal;
    }


    // No 'child' objects. Only the request message is sort of a 'child'.
    @Override
    public Object getChild(int itemPosition, int childPosition) {
        return alertsList.get(itemPosition).getRequestMessage();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parentView) {

        //parentView.setBackgroundColor(Color.RED);

        // Check if we can recycle first
        if(convertView == null) {
            // Inflate the row extension, or 'child row'.
            convertView = inflater.inflate(R.layout.alerts_row_extension, parentView, false);
        }
        // Set child 'fields' even though no child object exists
        final Incident incident = alertsList.get(groupPosition);

        // Set the text views
        ((TextView) convertView.findViewById(R.id.requestMessageTextView))
                .setText("Type " + incident.getBloodType() + " blood needed at " + incident.getHospitalName()+ ".\n" + incident.getRequestMessage());

        ((TextView) convertView.findViewById(R.id.alertsRowCallerTextView))
                .setText("Called by: " + incident.getCallerName());

        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");
        String formattedStartDate = formatter.format(incident.getStartDateInMillis());

        ((TextView) convertView.findViewById(R.id.alertsRowCallStartedTextView)).setText("Started " + formattedStartDate);
/*
        // Set listeners for the buttons in the row extension
        convertView.findViewById(R.id.directionsButton)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        Toast.makeText(alertsFragment.getActivity(), "Pressed directions button", Toast.LENGTH_SHORT).show();
                    }
                });
*/
        convertView.findViewById(R.id.donatedButton)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        alertsFragment.onDonatedButtonClick(incident);
                    }
                });


        return convertView;
    }

    // There is only 1 'child' for each row
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return alertsList.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
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

         Incident incident = alertsList.get(groupPosition);

        //String oldText = (String)((TextView)convertView.findViewById(R.id.hospitalTextView)).getText();

        // If there is no view to recycle, make a new one
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.alerts_row, parentView, false);
        }
        // If there is a view to recycle, check the Text is the right size
        else{
            String oldBloodType = (String) ((TextView) convertView.findViewById(R.id.bloodTypeTextView)).getText();

            if (oldBloodType.length() != incident.getBloodType().length()){
                convertView = inflater.inflate(R.layout.alerts_row, parentView, false);
            }
        }

        Date date = new Date();

        long currentTimeInMillis = date.getTime();
        long timeDiffInMillis = incident.getRequestEndDateInMillis() - currentTimeInMillis;

        long hoursDiff = timeDiffInMillis / 3600000;
        long minutesDiff = (timeDiffInMillis % 3600000) / 60000;
        //long secondsDiff = (timeDiffInMillis % 60000) / 1000;

        if (hoursDiff == 0) {

            ((TextView) convertView.findViewById(R.id.timeLeftTextView))
                    .setText(minutesDiff + "m remaining");

        } else {
            ((TextView) convertView.findViewById(R.id.timeLeftTextView))
                    .setText(hoursDiff + "h " + minutesDiff + "m remaining");
        }


            // Check if its a long or short blood type string
            if (incident.getBloodType().length()==3) {
                ((TextView) convertView.findViewById(R.id.bloodTypeTextView)).setTextSize(30);
            }
            ((TextView) convertView.findViewById(R.id.hospitalTextView))
                    .setText(incident.getHospitalName());
            ((TextView) convertView.findViewById(R.id.responsesTextView))
                    .setText(incident.getTotalResponsesNeeded() + " more responses needed");
            ((TextView) convertView.findViewById(R.id.bloodTypeTextView))
                    .setText(incident.getBloodType());

        return convertView;

    }


    @Override
    public boolean isChildSelectable(int itemPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public IncidentsList getIncidentsList() {
        return alertsList;
    }



}

