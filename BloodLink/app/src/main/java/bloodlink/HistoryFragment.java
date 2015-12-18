package samples.exoguru.bloodlink;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Edwin on 15/02/2015.
 */
public class HistoryFragment extends Fragment {

    protected ListView listView;
    private ArrayList<Incident> historyList = new ArrayList<Incident>();
    protected HistoryListViewAdapter historyListViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        // Inflate (render) the view
        View view = inflater.inflate(R.layout.history_fragment, parent, false);
        listView = (ListView) view.findViewById(R.id.historyListView);

        // Add reference to activity
        ((MainActivity) getActivity()).historyFragment = this;

        // Create HistoryListViewAdapter
        Resources res = getResources();
        this.historyListViewAdapter = new HistoryListViewAdapter(this, res);
        // Set adapter to view
        listView.setAdapter(historyListViewAdapter);


        return view;
    }


    public void onItemClick(int position) {
        Incident incident = (Incident) historyList.get(position);
    }
}
