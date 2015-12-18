package samples.exoguru.bloodlink;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;

/**
 * ****** Adapter class extends with BaseAdapter and implements with OnClickListener ***********
 */
public class HistoryListViewAdapter extends BaseAdapter {

    /**
     * ******** Declare Used Variables ********
     */

    private HistoryFragment historyFragment;
    private LayoutInflater inflater;
    public IncidentsList historyList;
    public Resources res;

    public HistoryListViewAdapter(HistoryFragment historyFragment, Resources resLocal) {

        // load from file

        this.historyList = IncidentsList.getHistoryList();
        this.historyFragment = historyFragment;
        this.inflater = LayoutInflater.from(historyFragment.getActivity());
        this.res = resLocal;
    }


    @Override
    public boolean isEmpty(){
        return historyList.isEmpty();
    }


    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getCount() {
        return historyList.size();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parentView) {

        Incident incident = historyList.get(position);

        //Log.i("historyGetView", "incident hospital name " + incident.getHospitalName());

        // If there is no view to recycle, make a new one
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.history_row, parentView, false);
        }

        // Set view text
        ((TextView) convertView.findViewById(R.id.historyRowCallerTextView))
                .setText("Call by\n" + incident.getCallerName());

        ((TextView) convertView.findViewById(R.id.historyRowHeaderTextView))
                .setText(incident.getHospitalName());

        ((TextView) convertView.findViewById(R.id.thanksMessageTextView))
                .setText(incident.getThanksMessage());

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = formatter.format(incident.getRequestEndDateInMillis());

        ((TextView) convertView.findViewById(R.id.historyRowDateTextView))
                .setText("Ended\n" + formattedDate);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}

